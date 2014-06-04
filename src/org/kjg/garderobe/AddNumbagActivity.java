package org.kjg.garderobe;

import java.util.Calendar;

import Model.CloakroomBag;
import Model.CloakroomNumber;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

public class AddNumbagActivity extends Activity {
	private final boolean D = true;
	private final String TAG = "NumBagActivity";

	public final static int GET_NUMBER = 1;
	public final static int GET_BAG = 2;

	public final static String KEY_EXTRA_MODE = "mode";

	private EditText txt_number;
	private EditText txt_comment;
	private Spinner sp_reason;
	private Button btn_time;
	private Button btn_add;
	private Button btn_cancel;

	private CloakroomNumber num;
	private CloakroomBag bag;
	private Intent returnIntent;

	private int mode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (D)
			Log.i(TAG, "***Begin - OnCreate***");

		setContentView(R.layout.activity_add_numbag);

		this.txt_number = (EditText) findViewById(R.id.txt_add_numbag_number);
		this.txt_comment = (EditText) findViewById(R.id.txt_add_numbag_comment);
		this.sp_reason = (Spinner) findViewById(R.id.sp_add_numbag_reason);
		this.btn_time = (Button) findViewById(R.id.btn_add_numbag_time);
		this.btn_add = (Button) findViewById(R.id.btn_add_numbag_add);
		this.btn_cancel = (Button) findViewById(R.id.btn_add_numbag_cancel);

		mode = this.getIntent().getIntExtra(KEY_EXTRA_MODE,
				AddNumbagActivity.GET_NUMBER);
		returnIntent = new Intent();
		this.setResult(mode, returnIntent);

		// Set entry array to spinner
		String[] array = null;
		if (mode == AddNumbagActivity.GET_NUMBER) {
			this.setTitle(getResources().getString(
					R.string.title_activity_add_number));
			array = getResources().getStringArray(R.array.reasons_number);

			if (D)
				Log.i(TAG, "Number mode");
		} else if (mode == AddNumbagActivity.GET_BAG) {
			this.setTitle(getResources().getString(
					R.string.title_activity_add_bag));
			array = getResources().getStringArray(R.array.reasons_bag);

			if (D)
				Log.i(TAG, "Bag mode");
		}

		ArrayAdapter<String> a = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, array);
		this.sp_reason.setAdapter(a);

		// Initiate time Button to current time
		this.btn_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btn_time_Clicked(v);
			}

		});

		this.btn_time.setText(String.format("%02d:%02d", Calendar.getInstance()
				.get(Calendar.HOUR_OF_DAY),
				Calendar.getInstance().get(Calendar.MINUTE)));

		// btn_add
		this.btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btn_add_Clicked(v);
			}

		});

		// btn_cancel
		this.btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btn_cancel_Clicked(v);
			}

		});

		num = new CloakroomNumber();
		bag = new CloakroomBag();

		if (D)
			Log.i(TAG, "***End - OnCreate***");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_numbag, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void btn_time_Clicked(View view) {
		TimePickerDialog tpd = new TimePickerDialog(this,
				new OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker tp, int h, int m) {
						btn_time.setText(String.format("%02d:%02d", h, m));
						num.setTime(h, m);
						bag.setTimeAdded(h, m);

						if (D)
							Log.i(TAG,
									"Time selected: "
											+ String.format("%02d:%02d", h, m));
					}

				}, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar
						.getInstance().get(Calendar.MINUTE), true);
		tpd.show();
	}

	private void btn_add_Clicked(View v) {
		int reason = 0;

		if (mode == AddNumbagActivity.GET_NUMBER) {
			switch (this.sp_reason.getSelectedItemPosition()) {
			case 0:
				reason = CloakroomNumber.FOUND;
				break;
			case 1:
				reason = CloakroomNumber.LEFT;
				break;
			case 2:
				reason = CloakroomNumber.MISTAKE;
				break;
			case 3:
				reason = CloakroomNumber.OTHER;
				break;
			}
			num.setReason(reason);
			num.setComment(this.txt_comment.getText().toString());
			num.setNumber(Integer
					.parseInt(this.txt_number.getText().toString()));

			returnIntent.putExtra("number", num);
		} else if (mode == AddNumbagActivity.GET_BAG) {
			switch (this.sp_reason.getSelectedItemPosition()) {
			case 0:
				reason = CloakroomBag.LOST;
				break;
			case 1:
				reason = CloakroomBag.LEFT;
				break;
			case 2:
				reason = CloakroomBag.WRONG_CONTENT;
				break;
			case 3:
				reason = CloakroomBag.MISTAKE;
				break;
			case 4:
				reason = CloakroomBag.OTHER;
				break;
			}

			bag.setReason(reason);
			bag.setComment(this.txt_comment.getText().toString().trim());
			bag.setNumber(Integer.parseInt(this.txt_number.getText().toString()
					.trim()));
			returnIntent.putExtra("bag", bag);
		}

		if (D) {
			Log.i(TAG, "Reason: " + reason);
			Log.i(TAG, "Comment: "
					+ this.txt_comment.getText().toString().trim());
			Log.i(TAG, "Number: " + this.txt_number.getText().toString().trim());
		}

		this.finish();
	}

	private void btn_cancel_Clicked(View v) {
		if (D)
			Log.i(TAG, "Cancelled");

		this.finish();
	}

}
