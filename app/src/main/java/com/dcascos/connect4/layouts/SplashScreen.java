package com.dcascos.connect4.layouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.dcascos.connect4.R;

public class SplashScreen extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_splash_screen);

		timerSplashScreen();
	}

	private void timerSplashScreen() {
		long time = 2000L;

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(SplashScreen.this, MainMenu.class);
				startActivity(intent);
				finish();
			}
		}, time);
	}
}
