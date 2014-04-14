package org.kjg.garderobe;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class NewPartyActivity extends Activity {
	private final boolean D = false;
	private final String TAG = "NewPartyActivity";

	public static final String KEY_PARTY = "party";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (D)
			Log.i(TAG, "***Begin - OnCreate***");

		setContentView(R.layout.activity_new_party);

		if (savedInstanceState == null) {
			if (D)
				Log.i(TAG, "Creating new fragment...");
			getFragmentManager().beginTransaction()
					.add(R.id.frame_new_party, new NewPartyFragment()).commit();
		}

		if (D)
			Log.i(TAG, "***End - OnCreate***");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
}
