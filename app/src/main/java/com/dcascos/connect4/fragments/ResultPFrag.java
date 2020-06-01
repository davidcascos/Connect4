package com.dcascos.connect4.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dcascos.connect4.R;
import com.dcascos.connect4.database.DBHelper;
import com.dcascos.connect4.database.DBManager;
import com.dcascos.connect4.logic.Status;

public class ResultPFrag extends Fragment {
	private ImageView iv_resultado;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fr_result_p_frag, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void showImage(int position) {
		iv_resultado = getActivity().findViewById(R.id.iv_resultado);
		DBManager dbManager = new DBManager(getActivity());
		dbManager.openRead();
		String[] columns = new String[]{DBHelper._ID, DBHelper.RESULT};
		Cursor cursor = dbManager.getCursor(columns);

		if (cursor.moveToFirst()) {
			cursor.moveToPosition(position);
		}

		String status = cursor.getString(1);

		if (status.equals(Status.PLAYER1_WINS.toString())) {
			iv_resultado.setImageResource(R.drawable.victoria);
		} else if (status.equals(Status.PLAYER2_WINS.toString())) {
			iv_resultado.setImageResource(R.drawable.derrota);
		} else if (status.equals(Status.DRAW.toString())) {
			iv_resultado.setImageResource(R.drawable.empate);
		} else if (status.equals(Status.ALL_PLAYER_LOSE.toString())) {
			iv_resultado.setImageResource(R.drawable.tiempoagotado);
		}
		dbManager.close();
	}
}