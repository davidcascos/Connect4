package com.dcascos.connect4.fragments;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dcascos.connect4.R;
import com.dcascos.connect4.database.DBHelper;
import com.dcascos.connect4.database.DBManager;
import com.dcascos.connect4.logic.Status;

public class QueryFrag extends Fragment {
	private SimpleCursorAdapter simpleCursorAdapter;
	ListView lv_register;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fr_query, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		lv_register = getView().findViewById(R.id.lv_register);
		lv_register.setTextFilterEnabled(true);

		DBManager dbManager = new DBManager(getActivity());
		dbManager.openRead();
		String[] columns = new String[]{DBHelper._ID, DBHelper.ALIAS, DBHelper.DATE, DBHelper.RESULT, DBHelper.IMAGE};
		Cursor cursor = dbManager.getCursor(columns);

		final String[] from = new String[]{DBHelper.ALIAS, DBHelper.DATE, DBHelper.RESULT, DBHelper.IMAGE};
		final int[] to = new int[]{R.id.tv_alias, R.id.tv_date, R.id.tv_result, R.id.iv_register};

		simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.layout_card, cursor, from, to, 0);

		putValuesInView();
		lv_register.setAdapter(simpleCursorAdapter);
		dbManager.close();


		EditText editText = getActivity().findViewById(R.id.et_filter);

		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				lv_register = getView().findViewById(R.id.lv_register);
				SimpleCursorAdapter filterAdapter = (SimpleCursorAdapter) lv_register.getAdapter();
				filterAdapter.getFilter().filter(s.toString());
			}
		});

		simpleCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
			@Override
			public Cursor runQuery(CharSequence constraint) {
				return getDirectoryList(constraint);
			}
		});
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

	public Cursor getDirectoryList(CharSequence constraint) {
		DBManager dbManager = new DBManager(getActivity());
		dbManager.openRead();
		String[] columns = new String[]{DBHelper._ID, DBHelper.ALIAS, DBHelper.DATE, DBHelper.RESULT, DBHelper.IMAGE};
		if (constraint == null || constraint.length() == 0) {
			return dbManager.getCursor(columns);
		} else {
			String value = "%" + constraint.toString() + "%";
			return dbManager.getCursorByName(columns, value);
		}
	}
}

