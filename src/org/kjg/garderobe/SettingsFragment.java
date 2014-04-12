package org.kjg.garderobe;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragment {
	private final boolean D = true;
	private final String TAG = "SettingsFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	public void onStop() {
		super.onStop();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		String locale = prefs
				.getString(MainActivity.KEY_PREF_LOCALE, "DEFAULT");
		if (locale != null) {
			if (D)
				Log.i(TAG, "Set locale to '" + locale + "'. Restarting...");

			Toast.makeText(getActivity(), R.string.restarting,
					Toast.LENGTH_SHORT).show();

			Intent mStartActivity = new Intent(getActivity(),
					MainActivity.class);
			int mPendingIntentId = 123456;
			PendingIntent mPendingIntent = PendingIntent.getActivity(
					getActivity(), mPendingIntentId, mStartActivity,
					PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager mgr = (AlarmManager) getActivity().getSystemService(
					Context.ALARM_SERVICE);
			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100,
					mPendingIntent);
			System.exit(0);
		}
	}
}
