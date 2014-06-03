package org.kjg.garderobe.notifications;

import static ch.lambdaj.Lambda.join;

import java.util.Calendar;

import org.kjg.garderobe.MainActivity;
import org.kjg.garderobe.R;

import Model.CloakroomShift;
import Model.Party;
import Model.Serializer;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationTimeBroadcastReiceiver extends BroadcastReceiver {
	private static final boolean D = true;
	private static final String TAG = "NotificationReceiver";

	private NotificationManager notificationManager;

	private final static int NOTIFICATION_ID_SHIFT = 1;

	public final static String KEY_EXTRA_PARTY = "party_name_string";
	public final static String KEY_EXTRA_CANCEL_NOTIFICATION = "cancel_notification_bool";

	@Override
	public void onReceive(Context c, Intent i) {
		if (D) {
			Log.i(TAG, "Broadcast received");
		}

		notificationManager = (NotificationManager) c
				.getSystemService(Context.NOTIFICATION_SERVICE);

		if (i.getBooleanExtra(KEY_EXTRA_CANCEL_NOTIFICATION, false)) {
			// show notification
			notificationManager.notify(NOTIFICATION_ID_SHIFT,
					buildNotification(c, i.getStringExtra(KEY_EXTRA_PARTY)));
			if (D)
				Log.i(TAG, "Notification displayed");

		} else {
			// cancel notification
			notificationManager.cancel(NOTIFICATION_ID_SHIFT);

			if (D)
				Log.i(TAG, "Notification canceled");
		}

	}

	private Notification buildNotification(Context c, String partyName) {
		Party p = Serializer.deserializeParty(partyName + ".p", c);
		if (D)
			Log.i(TAG, "Party: " + p.getName());

		CloakroomShift currentShift = null;
		for (CloakroomShift s : p.getSchedule()) {
			Calendar curr = Calendar.getInstance();
			if (curr.getTimeInMillis() > s.getStart().getTimeInMillis()
					&& curr.getTimeInMillis() < s.getEnd().getTimeInMillis()) {
				// shift is running
				currentShift = s;
				break;
			}
		}

		if (D)
			Log.i(TAG, "Shift: " + currentShift.getNumber());

		String title = String.format(
				c.getResources().getString(R.string.notification_title),
				currentShift.getNumber());

		String content = "";
		if (currentShift.getMembers().length > 0) {
			content = join(currentShift.getMembers());
		} else {
			content = c.getResources().getString(R.string.no_members);
		}

		PendingIntent pIntent = PendingIntent
				.getActivity(c, 0, new Intent(), 0);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(c);

		Bitmap bm = BitmapFactory.decodeResource(c.getResources(),
				R.drawable.seelenbohrer);

		builder.setLargeIcon(bm);
		builder.setSmallIcon(R.drawable.ic_seelenbohrer_notification);
		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setAutoCancel(false);
		builder.setOngoing(true);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(c);

		// use vibration
		if (prefs.getBoolean(
				MainActivity.KEY_PREF_ENABLE_NOTIFICATION_VIBRATION, true)) {
			builder.setVibrate(new long[] { 100, 1000, 200, 1000 });
			if (D)
				Log.i(TAG, "Vibration set");
		}

		// sound
		if (prefs.getBoolean(MainActivity.KEY_PREF_ENABLE_NOTIFICATION_SOUND,
				false)
				&& !prefs.getString(
						MainActivity.KEY_PREF_NOTIFICATION_SOUND_URI, "")
						.equals("")) {
			builder.setSound(Uri.parse(prefs.getString(
					MainActivity.KEY_PREF_NOTIFICATION_SOUND_URI, "")));
			if (D)
				Log.i(TAG,
						"Sound set: "
								+ prefs.getString(
										MainActivity.KEY_PREF_NOTIFICATION_SOUND_URI,
										""));
		}

		builder.setContentIntent(pIntent);
		return builder.build();
	}
}
