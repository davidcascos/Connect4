package com.dcascos.connect4.layouts;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dcascos.connect4.R;
import com.dcascos.connect4.logic.Status;
import com.dcascos.connect4.utils.GameOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Results extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
	//EditTexts
	private EditText et_date;
	private EditText et_mail;
	private EditText et_log;
	//Preferences
	private String alias;
	private int gridSize;

	private String timePlayed;
	private String status;
	private String result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_results);

		final MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);

		et_date = findViewById(R.id.et_date);
		et_mail = findViewById(R.id.et_mail);
		et_log = findViewById(R.id.et_log);
		et_mail.requestFocus();

		Button bt_newGame = findViewById(R.id.bt_newGame);
		Button bt_sendMail = findViewById(R.id.bt_sendMail);
		Button bt_register = findViewById(R.id.bt_register);
		Button bt_exit = findViewById(R.id.bt_exit);

		bt_newGame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickSound.start();
				Intent intent = new Intent(Results.this, BoardGame.class);
				startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Results.this).toBundle());
				finish();
			}
		});

		bt_sendMail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickSound.start();
				Intent intent = new Intent(Intent.ACTION_SENDTO);
				intent.setData(Uri.parse("mailto:" + et_mail.getText().toString()));
				intent.putExtra(Intent.EXTRA_SUBJECT, "LOG - " + et_date.getText().toString());
				intent.putExtra(Intent.EXTRA_TEXT, et_log.getText().toString());
				startActivity(intent);
			}
		});

		bt_register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickSound.start();
				Intent intent = new Intent(Results.this, Register.class);
				startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Results.this).toBundle());
			}
		});

		bt_exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickSound.start();
				showDialog();
			}
		});

		getPreferencesAndExtras();
		generateToastResult();
		fillCamps();
	}

	public void getPreferencesAndExtras() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Results.this);
		alias = prefs.getString(getString(R.string.prefEtAlias), getString(R.string.defaultAliasP1));
		gridSize = Integer.parseInt(prefs.getString(getString(R.string.prefListGrid), getString(R.string.defaultGrid)));

		timePlayed = getIntent().getStringExtra(getString(R.string.keyTimePlayed));
		status = getIntent().getStringExtra(getString(R.string.keyStatus));
	}

	private void generateToastResult() {
		final MediaPlayer win = MediaPlayer.create(getApplicationContext(), R.raw.win);
		final MediaPlayer lose = MediaPlayer.create(getApplicationContext(), R.raw.lose);

		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.layout_toast, (ViewGroup) findViewById(R.id.toast_layout));
		ImageView iv_toast = layout.findViewById(R.id.iv_toast);
		TextView tv_toast = layout.findViewById(R.id.tv_toast);

		if (status.equals(Status.PLAYER1_WINS.toString())) {
			result = getString(R.string.victory);
			win.start();
			tv_toast.setText(getString(R.string.toastWin));
			iv_toast.setImageResource(R.drawable.iv_win);
		} else if (status.equals(Status.PLAYER2_WINS.toString())) {
			result = getString(R.string.defeat);
			lose.start();
			tv_toast.setText(getString(R.string.toastLose));
			iv_toast.setImageResource(R.drawable.iv_lose);
		} else if (status.equals(Status.DRAW.toString())) {
			lose.start();
			tv_toast.setText(getString(R.string.toastDraw));
			iv_toast.setImageResource(R.drawable.iv_draw);
		} else if (status.equals(Status.ALL_PLAYER_LOSE.toString())) {
			lose.start();
			tv_toast.setText(getString(R.string.toastEndTime));
			iv_toast.setImageResource(R.drawable.iv_lose);
		}

		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	private void fillCamps() {
		String logText = getString(R.string.textAlias) + ": " + alias + "\n"
				+ getString(R.string.textSizeGrid) + ": " + gridSize + "\n"
				+ result + "\n"
				+ getString(R.string.totalTime) + ": " + timePlayed + " " + getString(R.string.seconds);

		et_date.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
		et_log.setText(logText);
		et_mail.setText(R.string.defaultMail);
	}

	private void showDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.dialogTitle)
				.setMessage(R.string.dialogMessage)
				.setPositiveButton(R.string.True, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				})
				.setNegativeButton(R.string.False, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	public void showConfMenu(View v) {
		PopupMenu popup = new PopupMenu(this, v);
		popup.setOnMenuItemClickListener(this);
		popup.inflate(R.menu.menu_options);
		popup.show();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_options:
				Intent intent = new Intent(Results.this, GameOptions.class);
				startActivity(intent);
				return true;
			default:
				return false;
		}
	}
}
