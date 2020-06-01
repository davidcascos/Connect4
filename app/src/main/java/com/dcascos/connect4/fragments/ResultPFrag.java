package com.dcascos.connect4.fragments;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dcascos.connect4.R;
import com.dcascos.connect4.database.DBHelper;
import com.dcascos.connect4.database.DBManager;
import com.dcascos.connect4.logic.Status;

public class ResultPFrag extends Fragment {
	private ImageView iv_resultado;
	String status;
	byte[] image;
	DBManager dbManager;

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

		if (savedInstanceState != null) {
			dbManager = new DBManager(getActivity());
			dbManager.openRead();
			if (dbManager.getVersion() < 2) {
				status = savedInstanceState.getString(getString(R.string.keyResult));
			} else {
				image = savedInstanceState.getByteArray(getString(R.string.keyImage));
			}
			fillCamps();
		}
	}

	public void showImage(int position) {
		dbManager = new DBManager(getActivity());
		dbManager.openRead();
		String[] columns;
		Cursor cursor;

		if (dbManager.getVersion() < 2) {
			columns = new String[]{DBHelper._ID, DBHelper.RESULT};
			cursor = dbManager.getCursor(columns);

			if (cursor.moveToFirst()) {
				cursor.moveToPosition(position);
			}
			status = cursor.getString(1);

		} else {
			columns = new String[]{DBHelper._ID, DBHelper.IMAGERESULTADO};
			cursor = dbManager.getCursor(columns);

			if (cursor.moveToFirst()) {
				cursor.moveToPosition(position);
			}

			image = cursor.getBlob(1);
		}
		fillCamps();
	}

	private void fillCamps() {
		iv_resultado = getActivity().findViewById(R.id.iv_resultado);

		if (dbManager.getVersion() < 2) {
			if (status.equals(Status.PLAYER1_WINS.toString())) {
				iv_resultado.setImageResource(R.drawable.victoria);
			} else if (status.equals(Status.PLAYER2_WINS.toString())) {
				iv_resultado.setImageResource(R.drawable.derrota);
			} else if (status.equals(Status.DRAW.toString())) {
				iv_resultado.setImageResource(R.drawable.empate);
			} else if (status.equals(Status.ALL_PLAYER_LOSE.toString())) {
				iv_resultado.setImageResource(R.drawable.tiempoagotado);
			}
		} else {
			iv_resultado.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
		}
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		if (dbManager.getVersion() < 2) {
			outState.putString(getString(R.string.keyResult), status);
		} else {
			outState.putByteArray(getString(R.string.keyImage), image);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dbManager.close();
	}
}