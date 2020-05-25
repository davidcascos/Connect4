package com.dcascos.connect4.fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.dcascos.connect4.R;
import com.dcascos.connect4.database.DBHelper;
import com.dcascos.connect4.database.DBManager;
import com.dcascos.connect4.logic.Status;

public class QueryFrag extends ListFragment {
	private SimpleCursorAdapter simpleCursorAdapter;

	private OnItemSelectedListener onItemSelectedListener;

	public interface OnItemSelectedListener {
		void onItemSelected(long id);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		DBManager dbManager = new DBManager(getActivity());
		dbManager.openRead();
		String[] columns = new String[]{DBHelper._ID, DBHelper.ALIAS, DBHelper.DATE, DBHelper.RESULT, DBHelper.IMAGE};
		Cursor cursor = dbManager.getCursor(columns);

		final String[] from = new String[]{DBHelper.ALIAS, DBHelper.DATE, DBHelper.RESULT, DBHelper.IMAGE};
		final int[] to = new int[]{R.id.tv_alias, R.id.tv_date, R.id.tv_result, R.id.iv_register};

		simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.fr_query, cursor, from, to, 0);

		putValuesInView();
		setListAdapter(simpleCursorAdapter);
		dbManager.close();
	}

	public void putValuesInView() {
		simpleCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

				if (view.getId() == R.id.tv_alias) {
					int getResultIndex = cursor.getColumnIndex(DBHelper.ALIAS);
					String alias = cursor.getString(getResultIndex);

					TextView tv_alias = (TextView) view;
					tv_alias.setText(alias);
					tv_alias.setSelected(true);
					return true;
				}

				if (view.getId() == R.id.tv_date) {
					int getResultIndex = cursor.getColumnIndex(DBHelper.DATE);
					String date = cursor.getString(getResultIndex);

					TextView tv_date = (TextView) view;
					tv_date.setText(date);
					tv_date.setSelected(true);
					return true;
				}

				if (view.getId() == R.id.tv_result) {
					int getResultIndex = cursor.getColumnIndex(DBHelper.RESULT);
					String status = cursor.getString(getResultIndex);
					String result;

					if (status.equals(Status.PLAYER1_WINS.toString())) {
						result = getString(R.string.victory);
					} else if (status.equals(Status.PLAYER2_WINS.toString())) {
						result = getString(R.string.defeat);
					} else if (status.equals(Status.DRAW.toString())) {
						result = getString(R.string.draw);
					} else {
						result = getString(R.string.toastEndTime);
					}

					TextView tv_result = (TextView) view;
					tv_result.setText(result);
					tv_result.setSelected(true);
					return true;
				}

				if (view.getId() == R.id.iv_register) {
					int getResultIndex = cursor.getColumnIndex(DBHelper.IMAGE);
					byte[] image = cursor.getBlob(getResultIndex);

					ImageView iv_register = (ImageView) view;
					iv_register.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		onItemSelectedListener.onItemSelected(id);
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		onItemSelectedListener = (OnItemSelectedListener) context;
	}
}

