package com.dcascos.connect4.layouts;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dcascos.connect4.R;
import com.dcascos.connect4.utils.GameOptions;

public class MainMenu extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		setContentView(R.layout.ac_main_menu);

		final MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);

		Button bt_play = findViewById(R.id.bt_play);
		Button bt_rules = findViewById(R.id.bt_rules);
		Button bt_register = findViewById(R.id.bt_register);
		Button bt_exit = findViewById(R.id.bt_exit);

		bt_play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickSound.start();
				Intent intent = new Intent(MainMenu.this, BoardGame.class);
				startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainMenu.this).toBundle());
				finish();
			}
		});

		bt_rules.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickSound.start();
				Intent intent = new Intent(MainMenu.this, Rules.class);
				startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainMenu.this).toBundle());
			}
		});

		bt_register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickSound.start();
				Intent intent = new Intent(MainMenu.this, Register.class);
				startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainMenu.this).toBundle());
			}
		});

		bt_exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickSound.start();
				showDialog();
			}
		});
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
				Intent intent = new Intent(MainMenu.this, GameOptions.class);
				startActivity(intent);
				return true;
			default:
				return false;
		}
	}
}