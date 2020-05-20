package com.dcascos.connect4.layouts;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.dcascos.connect4.R;
import com.dcascos.connect4.utils.GameOptions;

public class MainMenu extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		setContentView(R.layout.ac_main_menu);

		final MediaPlayer click = MediaPlayer.create(this, R.raw.click);

		Button bt_play = findViewById(R.id.bt_play);
		Button bt_rules = findViewById(R.id.bt_rules);
		Button bt_register = findViewById(R.id.bt_register);
		Button bt_exit = findViewById(R.id.bt_exit);

		bt_play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				click.start();
			}
		});

		bt_rules.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				click.start();
			}
		});

		bt_register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				click.start();
			}
		});

		bt_exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				click.start();
				finish();
			}
		});
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
