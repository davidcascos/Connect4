package com.dcascos.connect4.layouts;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.dcascos.connect4.R;
import com.dcascos.connect4.fragments.QueryDetailFrag;

public class QueryDetail extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_query_detail);
		final MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);

		ImageButton bt_back = findViewById(R.id.bt_back);

		bt_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickSound.start();
				finish();
			}
		});

		long itemId = getIntent().getLongExtra(getString(R.string.keyItemId), 1);

		QueryDetailFrag queryDetailFrag = (QueryDetailFrag) getSupportFragmentManager().findFragmentById(R.id.fr_query_detail);
		queryDetailFrag.showDetails(itemId);
	}
}
