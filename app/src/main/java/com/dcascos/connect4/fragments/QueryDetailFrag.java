package com.dcascos.connect4.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dcascos.connect4.R;
import com.dcascos.connect4.database.DBHelper;
import com.dcascos.connect4.database.DBManager;
import com.dcascos.connect4.logic.Status;

import java.util.Objects;

public class QueryDetailFrag extends Fragment {
	private Cursor cursor;
	private TextView tv_alias;
	private TextView tv_date;
	private TextView tv_gridSize;
	private TextView tv_timeControl;
	private TextView tv_titleTime;
	private TextView tv_time;
	private TextView tv_result;
	//Camps
	private String alias;
	private String date;
	private String gridSize;
	private String check;
	private String time;
	private String result;
	private String status;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fr_query_detail, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			alias = savedInstanceState.getString(getString(R.string.keyAlias));
			date = savedInstanceState.getString(getString(R.string.keyDate));
			gridSize = savedInstanceState.getString(getString(R.string.keyGridSize));
			check = savedInstanceState.getString(getString(R.string.keyCheck));
			time = savedInstanceState.getString(getString(R.string.keyTime));
			result = savedInstanceState.getString(getString(R.string.keyResult));
			initTextViews();
			fillCamps();
		}
	}

	public void showDetails(long id) {
		DBManager dbManager = new DBManager(getActivity());
		dbManager.openRead();
		String[] columns = new String[]{DBHelper._ID, DBHelper.ALIAS, DBHelper.DATE, DBHelper.SIZE, DBHelper.TIMING, DBHelper.TIME, DBHelper.RESULT};
		cursor = dbManager.getCursor(columns);

		if (cursor.moveToFirst()) {
			cursor.moveToPosition((int) id - 1);
		}

		alias = cursor.getString(1);
		date = cursor.getString(2);
		gridSize = String.valueOf(cursor.getInt(3));
		check = cursor.getInt(4) != 0 ? getString(R.string.True) : getString(R.string.False);
		time = cursor.getString(5);
		status = cursor.getString(6);

		if (status.equals(Status.PLAYER1_WINS.toString())) {
			result = getString(R.string.victory);
		} else if (status.equals(Status.PLAYER2_WINS.toString())) {
			result = getString(R.string.defeat);
		} else if (status.equals(Status.DRAW.toString())) {
			result = getString(R.string.draw);
		} else {
			result = getString(R.string.toastEndTime);
		}

		initTextViews();
		fillCamps();
		dbManager.close();
	}

	private void initTextViews() {
		tv_alias = Objects.requireNonNull(getView()).findViewById(R.id.tv_alias);
		tv_date = Objects.requireNonNull(getView()).findViewById(R.id.tv_date);
		tv_gridSize = Objects.requireNonNull(getView()).findViewById(R.id.tv_gridSize);
		tv_timeControl = Objects.requireNonNull(getView()).findViewById(R.id.tv_timeControl);
		tv_titleTime = Objects.requireNonNull(getView()).findViewById(R.id.tv_titleTime);
		tv_time = Objects.requireNonNull(getView()).findViewById(R.id.tv_time);
		tv_result = Objects.requireNonNull(getView()).findViewById(R.id.tv_result);
	}

	private void fillCamps() {
		if (check.equals(getString(R.string.False))) {
			tv_titleTime.setVisibility(View.INVISIBLE);
			tv_time.setVisibility(View.INVISIBLE);
		}

		tv_alias.setText(alias);
		tv_date.setText(date);
		tv_gridSize.setText(gridSize);
		tv_timeControl.setText(check);
		tv_time.setText(time);
		tv_result.setText(result);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(getString(R.string.keyAlias), alias);
		outState.putString(getString(R.string.keyDate), date);
		outState.putString(getString(R.string.keyGridSize), gridSize);
		outState.putString(getString(R.string.keyCheck), check);
		outState.putString(getString(R.string.keyTime), time);
		outState.putString(getString(R.string.keyResult), result);
	}
}
