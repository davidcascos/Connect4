package com.dcascos.connect4.layouts;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.dcascos.connect4.R;

public class Rules extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_rules);

		ImageButton bt_back = findViewById(R.id.bt_back);
		final MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);

		bt_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickSound.start();
				finish();
			}
		});
	}
}