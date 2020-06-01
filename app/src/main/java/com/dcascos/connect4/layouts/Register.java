package com.dcascos.connect4.layouts;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.dcascos.connect4.R;
import com.dcascos.connect4.fragments.QueryDetailFrag;
import com.dcascos.connect4.fragments.ResultPFrag;

import java.util.Objects;

public class Register extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_register);
		final MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);

		ImageButton bt_back = findViewById(R.id.bt_back);
		ListView lv_register = findViewById(R.id.lv_register);

		bt_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickSound.start();
				finish();
			}
		});

		lv_register.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				QueryDetailFrag queryDetailFrag = (QueryDetailFrag) getSupportFragmentManager().findFragmentById(R.id.fr_query_detail);
				ResultPFrag resultPFrag = (ResultPFrag) getSupportFragmentManager().findFragmentById(R.id.fr_result_p_frag);
				if (queryDetailFrag != null && queryDetailFrag.isInLayout()) {
					queryDetailFrag.showDetails(position);
					Objects.requireNonNull(resultPFrag).showImage(position);
				} else {
					Intent intent = new Intent(Register.this, QueryDetail.class);
					intent.putExtra(getString(R.string.keyItemPosition), position);
					startActivity(intent);
				}
			}
		});
	}
}