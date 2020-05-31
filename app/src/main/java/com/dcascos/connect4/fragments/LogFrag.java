package com.dcascos.connect4.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dcascos.connect4.R;
import com.dcascos.connect4.logic.Position;

import java.util.ArrayList;
import java.util.Objects;

public class LogFrag extends Fragment {
	//Preferences
	private String alias;
	private int gridSize;
	private boolean timeControl;
	//title
	private TextView tv_logInfo;
	//log
	private ListView lv_log;
	private ArrayAdapter<String> arrayAdapter;
	private ArrayList<String> logArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferences();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fr_log, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tv_logInfo = Objects.requireNonNull(getView()).findViewById(R.id.tv_logInfo);
		lv_log = getView().findViewById(R.id.lv_log);

		logArray = savedInstanceState != null ? savedInstanceState.getStringArrayList(getString(R.string.keyLog)) : new ArrayList<String>();

		arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_expandable_list_item_1, logArray);
		lv_log.setAdapter(arrayAdapter);
		setTitle();
	}

	public void getPreferences() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		alias = prefs.getString(getString(R.string.prefEtAlias), getString(R.string.defaultAliasP1));
		gridSize = Integer.parseInt(prefs.getString(getString(R.string.prefListGrid), getString(R.string.defaultGrid)));
		timeControl = prefs.getBoolean(getString(R.string.prefCbTime), false);
	}

	public void setTitle() {
		String logInfo = getString(R.string.textAlias) + ": " + alias + " "
				+ getString(R.string.textSizeGrid) + ": " + gridSize + " "
				+ (timeControl ? getString(R.string.timeControl) : getString(R.string.noTimeControl));
		tv_logInfo.setText(logInfo);
	}

	public void addToLog(Position position, String initThrow, String finishThrow, String remain) {

		String logItem = getString(R.string.cellFilled) + (gridSize - position.getRow()) + "," + (position.getColumn() + 1) + ")\n"
				+ getString(R.string.startChoice) + initThrow + " - "
				+ getString(R.string.endChoice) + finishThrow + "\n"
				+ (timeControl ? getString(R.string.timeRemain) + remain + getString(R.string.seconds) + "\n" : "");

		logArray.add(logItem);
		arrayAdapter.notifyDataSetChanged();
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putStringArrayList(getString(R.string.keyLog), logArray);
	}
}