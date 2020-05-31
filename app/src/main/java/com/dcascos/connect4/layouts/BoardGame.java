package com.dcascos.connect4.layouts;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.dcascos.connect4.R;
import com.dcascos.connect4.fragments.GridFrag;
import com.dcascos.connect4.fragments.LogFrag;
import com.dcascos.connect4.logic.Position;

public class BoardGame extends AppCompatActivity implements GridFrag.OnStateChangedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_board_game);
	}

	@Override
	public void onStateChanged(Position position, String initThrow, String finishThrow, String remainingTime) {
		LogFrag logFrag = (LogFrag) getSupportFragmentManager().findFragmentById(R.id.fr_log);

		if (logFrag != null && logFrag.isInLayout()) {
			logFrag.addToLog(position, initThrow, finishThrow, remainingTime);
		}
	}
}