package org.kjg.garderobe;

import java.util.Calendar;

import org.kjg.garderobe.notifications.NotificationTimeBroadcastReiceiver;

import Model.Party;
import Model.Serializer;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

public class NewPartyFragment extends Fragment {
	private final boolean D = true;
	private final String TAG = "NewPartyFragment";

	private Button btn_party_date;
	private Button btn_party_start;
	private Button btn_party_create;
	private Button btn_cancel;
	private Spinner sp_shift_count;
	private Spinner sp_shift_length;
	private EditText txt_party_name;

	private Calendar calParty;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (D)
			Log.i(TAG, "***Begin - OnCreate***");

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_new_party, container,
				false);

		calParty = Calendar.getInstance();
		this.sp_shift_count = (Spinner) view.findViewById(R.id.sp_shift_count);
		this.sp_shift_length = (Spinner) view
				.findViewById(R.id.sp_shift_length);
		this.txt_party_name = (EditText) view.findViewById(R.id.txt_party_name);

		// time picker button
		this.btn_party_start = (Button) view.findViewById(R.id.btn_party_start);
		btn_party_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btn_party_start_Clicked(v);

			}

		});

		// date picker button
		this.btn_party_date = (Button) view.findViewById(R.id.btn_party_date);
		btn_party_date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_party_date_Clicked(v);
			}
		});

		// create button
		this.btn_party_create = (Button) view
				.findViewById(R.id.btn_create_new_party);
		btn_party_create.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btn_party_create_Clicked(v);
			}

		});

		// cancel button
		this.btn_cancel = (Button) view.findViewById(R.id.btn_new_party_cancel);
		this.btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btn_cancel_Clicked(v);
			}

		});

		getActivity().getActionBar().setTitle(
				getActivity().getResources()
						.getString(R.string.title_new_party));

		this.setHasOptionsMenu(true);
		this.setRetainInstance(true);

		if (D)
			Log.i(TAG, "***End - OnCreate***");

		return view;
	}

	public void btn_cancel_Clicked(View v) {
		if (D)
			Log.i(TAG, "Canceled");

		this.getActivity().setResult(Activity.RESULT_CANCELED, new Intent());
		this.getActivity().finish();

		// getActivity().getFragmentManager().beginTransaction().remove(this)
		// .commit();
		// getActivity().getFragmentManager().popBackStack();
	}

	public void btn_party_create_Clicked(View view) {
		if (D)
			Log.i(TAG, "***Begin - Create Clicked***");

		// party

		// shift length in minutes
		int shiftLen = 120;
		switch (this.sp_shift_length.getSelectedItemPosition()) {
		case 0: // 2 hours
			shiftLen = 120;
			break;
		case 1: // 1 hour
			shiftLen = 60;
			break;
		case 2: // 30 minutes
			shiftLen = 30;
			break;
		}

		if (D) {
			Log.i(TAG, "Name: "
					+ this.txt_party_name.getText().toString().trim());
			Log.i(TAG, "Shift count: "
					+ this.sp_shift_count.getSelectedItem().toString());
			Log.i(TAG, "Shift length: " + shiftLen);
			Log.i(TAG, "Time: " + calParty.getTimeInMillis());
		}

		Party p = new Party(this.txt_party_name.getText().toString().trim(),
				Integer.valueOf(this.sp_shift_count.getSelectedItem()
						.toString()), shiftLen);

		p.setDate(calParty.get(Calendar.YEAR), calParty.get(Calendar.MONTH),
				calParty.get(Calendar.DATE));
		p.setTime(calParty.get(Calendar.HOUR_OF_DAY),
				calParty.get(Calendar.MINUTE));

		// Save party to file
		Serializer.serializeParty(p, getActivity());

		if (D)
			Log.i(TAG, "Serialized Party");

		// update drawer party selection spinner
		// ((MainActivity) getActivity()).getDrawerAdapter()
		// .updateSpinnerAdapter();

		// set new party as active
		// ((MainActivity) getActivity()).setCurrentParty(p);

		// set time for notifications
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());

		if (prefs.getBoolean(MainActivity.KEY_PREF_ENABLE_NOTIFICATIONS, true)) {
			AlarmManager aManager = (AlarmManager) getActivity()
					.getSystemService(Context.ALARM_SERVICE);

			// set notification for each shift
			for (int i = 0; i < p.getShiftCount(); i++) {
				Intent intent = new Intent(getActivity(),
						NotificationTimeBroadcastReiceiver.class);

				// party name
				intent.putExtra(
						NotificationTimeBroadcastReiceiver.KEY_EXTRA_PARTY,
						p.getName());

				// set cancel false
				intent.putExtra(
						NotificationTimeBroadcastReiceiver.KEY_EXTRA_CANCEL_NOTIFICATION,
						false);

				PendingIntent pIntent = PendingIntent.getBroadcast(
						getActivity(), (int) System.currentTimeMillis(),
						intent, PendingIntent.FLAG_ONE_SHOT);

				aManager.set(AlarmManager.RTC_WAKEUP, p.getSchedule().get(i)
						.getStart().getTimeInMillis(), pIntent);

				if (D) {
					Log.i(TAG, "Notification Alarm set for shift " + (i + 1));
					Log.i(TAG,
							"Time: "
									+ String.format("%02d:%02d", p
											.getSchedule().get(i).getStart()
											.get(Calendar.HOUR_OF_DAY), p
											.getSchedule().get(i).getStart()
											.get(Calendar.MINUTE)));
				}
			}

			// set time to cancel notifications
			Intent intent = new Intent(getActivity(),
					NotificationTimeBroadcastReiceiver.class);

			// cancel
			intent.putExtra(
					NotificationTimeBroadcastReiceiver.KEY_EXTRA_CANCEL_NOTIFICATION,
					true);

			PendingIntent pIntent = PendingIntent.getBroadcast(getActivity(),
					0, intent, PendingIntent.FLAG_ONE_SHOT);

			aManager.set(AlarmManager.RTC_WAKEUP, p.getSchedule().getLast()
					.getEnd().getTimeInMillis(), pIntent);

			if (D) {
				Log.i(TAG, "Cancel notifications Alarm set");
				Log.i(TAG,
						"Time: "
								+ String.format(
										"%02d:%02d",
										p.getSchedule().getLast().getEnd()
												.get(Calendar.HOUR_OF_DAY), p
												.getSchedule().getLast()
												.getEnd().get(Calendar.MINUTE)));
			}
		}

		Intent result = new Intent();
		result.putExtra(NewPartyActivity.KEY_PARTY, p);
		this.getActivity().setResult(Activity.RESULT_OK, result);

		if (D)
			Log.i(TAG, "***End - Create Clicked***");

		this.getActivity().finish();

		// FragmentTransaction ft = getFragmentManager().beginTransaction();
		// ft.replace(R.id.frame_container, new ScheduleFragment());
		// ft.commit();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.fragment_new_party, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case R.id.action_create_party: // create party
			btn_party_create_Clicked(null);
			return true;
		case R.id.action_cancel_party:
			btn_cancel_Clicked(null);
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}

	}

	public void btn_party_start_Clicked(View view) {
		TimePickerDialog tpd = new TimePickerDialog(getActivity(),
				new OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker tp, int h, int m) {
						btn_party_start.setText(String
								.format("%02d:%02d", h, m));

						if (D)
							Log.i(TAG,
									"Time Selected: "
											+ String.format("%02d:%02d", h, m));

						calParty.set(Calendar.HOUR_OF_DAY, h);
						calParty.set(Calendar.MINUTE, m);
					}

				}, 19, 0, true);
		tpd.show();
	}

	public void btn_party_date_Clicked(View view) {

		final Calendar c = Calendar.getInstance();

		DatePickerDialog dpd = new DatePickerDialog(getActivity(),
				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker dp, int y, int m, int d) {

						c.set(y, m, d);

						btn_party_date.setText(DateUtils.formatDateTime(
								getActivity(), c.getTimeInMillis(),
								DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_NUMERIC_DATE
										| DateUtils.FORMAT_SHOW_YEAR));

						if (D)
							Log.i(TAG,
									"Date selected: "
											+ DateUtils.formatDateTime(
													getActivity(),
													c.getTimeInMillis(),
													DateUtils.FORMAT_SHOW_DATE
															| DateUtils.FORMAT_NUMERIC_DATE
															| DateUtils.FORMAT_SHOW_YEAR));

						calParty.set(y, m, d);
					}

				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
						.get(Calendar.DATE));
		dpd.show();
	}
}
