package org.kjg.garderobe.notifications;

import org.kjg.garderobe.R;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationTimeBroadcastReiceiver extends BroadcastReceiver {
	private static final boolean D = true;
	private static final String TAG = "NotificationReceiver";

	private NotificationManager notificationManager;

	private final static int NOTIFICATION_ID_SHIFT = 1;

	public final static String KEY_EXTRA_TITLE = "title_string";
	public final static String KEY_EXTRA_CONTENT = "content_string";
	public final static String KEY_EXTRA_USE_VIBRATION = "vibrate_bool";
	public final static String KEY_EXTRA_USE_SOUND = "sound_bool";
	public final static String KEY_EXTRA_SOUND_URI = "sound_uri";

	@Override
	public void onReceive(Context c, Intent i) {
		if (D) {
			Log.i(TAG, "Broadcast received");
		}

		notificationManager = (NotificationManager) c
				.getSystemService(Context.NOTIFICATION_SERVICE);

		String title = i.getStringExtra(KEY_EXTRA_TITLE);
		String content = i.getStringExtra(KEY_EXTRA_CONTENT);

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

		if (i.getBooleanExtra(KEY_EXTRA_USE_VIBRATION, false)) {
			builder.setVibrate(new long[] { 100, 1000, 200, 1000 });
			if (D)
				Log.i(TAG, "Vibration set");
		}
		if (i.getBooleanExtra(KEY_EXTRA_USE_SOUND, false)
				&& !i.getStringExtra(KEY_EXTRA_SOUND_URI).equals("")) {
			builder.setSound(Uri.parse(i.getStringExtra(KEY_EXTRA_SOUND_URI)));
			if (D)
				Log.i(TAG,
						"Sound set: " + i.getStringExtra(KEY_EXTRA_SOUND_URI));
		}

		builder.setContentIntent(pIntent);
		notificationManager.notify(NOTIFICATION_ID_SHIFT, builder.build());

		if (D)
			Log.i(TAG, "Notification displayed");
	}
}
