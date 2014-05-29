package org.kjg.garderobe.notifications;

import org.kjg.garderobe.R;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationTimeBroadcastReiceiver extends BroadcastReceiver {
	private static final boolean D = true;
	private static final String TAG = "NotificationReceiver";

	private NotificationManager notificationManager;

	private final static int NOTIFICATION_ID_SHIFT = 1;

	// public final static String KEY_EXTRA_PARTY = "party";

	@Override
	public void onReceive(Context c, Intent i) {
		if (D) {
			Log.i(TAG, "Broadcast received!");
		}

		notificationManager = (NotificationManager) c
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Party p = (Party) i.getSerializableExtra(KEY_EXTRA_PARTY);

		PendingIntent pIntent = PendingIntent
				.getActivity(c, 0, new Intent(), 0);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(c);

		builder.setSmallIcon(R.drawable.seelenbohrer);
		builder.setContentTitle("TestTitle");
		builder.setContentText("TestText");

		builder.setContentIntent(pIntent);
		notificationManager.notify(NOTIFICATION_ID_SHIFT, builder.build());

	}
}
