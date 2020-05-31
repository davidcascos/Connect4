package com.dcascos.connect4.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dcascos.connect4.R;
import com.dcascos.connect4.layouts.Results;
import com.dcascos.connect4.logic.Game;
import com.dcascos.connect4.logic.Position;
import com.dcascos.connect4.logic.Status;
import com.dcascos.connect4.database.DBManager;
import com.dcascos.connect4.utils.ImageAdapter;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class GridFrag extends Fragment implements AdapterView.OnItemClickListener {
	//Grid
	private ImageAdapter imageAdapter;
	private GridView gv_game;
	private ImageView iv_token;
	int[] tokens;
	//Preferences
	private String alias;
	private int gridSize;
	private boolean timeControl;
	private int maximumTime;
	//Logic
	Game game;
	//Time control
	private TextView tv_time;
	private Chronometer ch_time;
	private long timeLeftInMilliseconds;
	private long timePlayed;
	//Time throw control (LOG)
	private String initThrow;
	private String finishThrow;

	private boolean gameEnd = false;

	private OnStateChangedListener onStateChangeListener;

	public interface OnStateChangedListener {
		void onStateChanged(Position position, String initThrow, String finishThrow, String remain);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		getPreferences();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fr_grid, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		gv_game = Objects.requireNonNull(getView()).findViewById(R.id.grid_view);
		tv_time = getView().findViewById(R.id.tv_time);
		ch_time = getView().findViewById(R.id.ch_time);
		iv_token = getView().findViewById(R.id.iv_token);

		if (savedInstanceState != null) {
			game = (Game) savedInstanceState.getSerializable(getString(R.string.keyGame));
			tokens = savedInstanceState.getIntArray(getString(R.string.keyTokenPositions));
			timeLeftInMilliseconds = savedInstanceState.getLong(getString(R.string.keyMillisLeft));
			timePlayed = savedInstanceState.getLong(getString(R.string.keyTimePlayed));
		} else {
			game = new Game(gridSize);

			tokens = new int[gridSize * gridSize];
			Arrays.fill(tokens, R.drawable.token_empty);

			timeLeftInMilliseconds = (maximumTime * 1000) + 1000; //30 seg
		}
		startChronometer();
		preprareGrid();
		initGame();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final MediaPlayer tokenSound = MediaPlayer.create(Objects.requireNonNull(getActivity()).getApplicationContext(), R.raw.token);
		tokenSound.start();

		//Calculate column where put new token and move it
		int column = position % gridSize;
		Position selectedPosition = game.drop(column);

		if (selectedPosition == null) {
			Toast.makeText(getActivity(), getString(R.string.columnFilled), Toast.LENGTH_LONG).show();
		} else {
			finishThrow = new SimpleDateFormat("HH:mm:ss").format(new Date());
			tokens[gridSize * selectedPosition.getRow() + selectedPosition.getColumn()] = R.drawable.token_red;
			imageAdapter.refresh(tokens);

			notifyLog(selectedPosition, initThrow, finishThrow);

			if (game.checkForFinish()) {
				checkEndGame();
			} else {
				playsOpposite();
				iv_token.setImageResource(R.drawable.token_red);
				initThrow = new SimpleDateFormat("HH:mm:ss").format(new Date());
			}
		}
	}

	public void getPreferences() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		alias = prefs.getString(getString(R.string.prefEtAlias), getString(R.string.defaultAliasP1));
		gridSize = Integer.parseInt(prefs.getString(getString(R.string.prefListGrid), getString(R.string.defaultGrid)));
		timeControl = prefs.getBoolean(getString(R.string.prefCbTime), false);
		maximumTime = Integer.parseInt(prefs.getString(getString(R.string.prefTime), getString(R.string.defaultTime)));
	}

	public void preprareGrid() {
		imageAdapter = new ImageAdapter(getActivity(), tokens);
		gv_game.setAdapter(imageAdapter);
		gv_game.setNumColumns(gridSize);
		gv_game.setOnItemClickListener(this);
	}

	public void initGame() {
		iv_token.setImageResource(R.drawable.token_red);
		initThrow = new SimpleDateFormat("HH:mm:ss").format(new Date());

		if (timeControl) {
			startCountDownTimer();
		} else {
			tv_time.setVisibility(View.INVISIBLE);
			ch_time.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorNoTiming));
		}
	}

	private void startCountDownTimer() {
		ch_time.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.invisible));
		tv_time.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTiming));
		CountDownTimer countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				timeLeftInMilliseconds = millisUntilFinished;

				int minutes = (int) timeLeftInMilliseconds / 60000;
				int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

				String timeleft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
				tv_time.setText(timeleft);
			}

			@Override
			public void onFinish() {
				game.setStatus(Status.ALL_PLAYER_LOSE);
				if (!gameEnd) {
					checkEndGame();
				}
			}
		}.start();
	}

	private void startChronometer() {
		if (timePlayed != 0) {
			ch_time.setBase(timePlayed);
		}
		ch_time.start();
	}

	private void playsOpposite() {
		iv_token.setImageResource(R.drawable.token_yellow);
		final MediaPlayer tokenSound = MediaPlayer.create(Objects.requireNonNull(getActivity()).getApplicationContext(), R.raw.token);
		tokenSound.start();
		Position opponentPosition = game.playOpponent();
		tokens[gridSize * opponentPosition.getRow() + opponentPosition.getColumn()] = R.drawable.token_yellow;
		imageAdapter.refresh(tokens);
		checkEndGame();
	}

	private void notifyLog(Position position, String initThrow, String finishThrow) {
		String remainingTime = timeControl ? tv_time.getText().toString() : "";
		onStateChangeListener.onStateChanged(position, initThrow, finishThrow, remainingTime);
	}

	private void checkEndGame() {
		if (game.checkForFinish()) {
			gameEnd = true;
			ch_time.stop();
			saveOnDB();
			Intent intent = new Intent(getActivity(), Results.class);
			intent.putExtra(getString(R.string.keyTimePlayed), ch_time.getText().toString());
			intent.putExtra(getString(R.string.keyStatus), game.getStatus().toString());
			startActivity(intent);
			Objects.requireNonNull(getActivity()).finish();
		}
	}

	private void saveOnDB() {
		String actualDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
		String timePlayed = ch_time.getText().toString();
		DBManager dbManager = new DBManager(getActivity());
		String result = game.getStatus().toString();
		byte[] imageInByte = setImageOfState();

		dbManager.openWrite();
		try {
			dbManager.insert(alias, actualDate, gridSize, timeControl, timePlayed, result, imageInByte);
		} catch (Exception e) {
			Toast.makeText(getActivity(), getString(R.string.toastErrorSave), Toast.LENGTH_SHORT).show();
		}
		dbManager.close();
	}

	private byte[] setImageOfState() {
		Bitmap icon;

		if (game.getStatus() == Status.PLAYER1_WINS) {
			icon = BitmapFactory.decodeResource(getResources(), R.drawable.iv_win);
		} else if (game.getStatus() == Status.DRAW) {
			icon = BitmapFactory.decodeResource(getResources(), R.drawable.iv_draw);
		} else if (game.getStatus() == Status.ALL_PLAYER_LOSE) {
			icon = BitmapFactory.decodeResource(getResources(), R.drawable.iv_lose);
		} else {
			icon = BitmapFactory.decodeResource(getResources(), R.drawable.iv_lose);
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		icon.compress(Bitmap.CompressFormat.PNG, 0, stream);
		return stream.toByteArray();
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		onStateChangeListener = (OnStateChangedListener) context;
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		//Save game
		outState.putSerializable(getString(R.string.keyGame), game);
		outState.putIntArray(getString(R.string.keyTokenPositions), tokens);
		//Save time
		outState.putLong(getString(R.string.keyMillisLeft), timeLeftInMilliseconds);
		outState.putLong(getString(R.string.keyTimePlayed), ch_time.getBase());
	}
}


