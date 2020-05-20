package com.dcascos.connect4.layouts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.dcascos.connect4.R;

public class MainMenu extends AppCompatActivity {

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
}
