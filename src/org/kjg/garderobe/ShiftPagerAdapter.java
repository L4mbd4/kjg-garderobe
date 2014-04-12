package org.kjg.garderobe;

import Model.Party;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.util.Log;

public class ShiftPagerAdapter extends FragmentStatePagerAdapter {
	private final boolean D = false;
	private final String TAG = "PagerAdapter";

	private final Context context;

	private final Party party;
	private final int shiftIndex;

	public ShiftPagerAdapter(FragmentManager fragmentManager, Context c,
			int shiftIndex, Party p) {
		super(fragmentManager);
		context = c;
		this.shiftIndex = shiftIndex;
		this.party = p;

		if (D)
			Log.i(TAG,
					"Created (Shift " + (shiftIndex + 1) + "; Party: "
							+ p.getName() + ")");
	}

	@Override
	public Fragment getItem(int pos) {
		Fragment f = null;
		Bundle args = new Bundle();
		args.putInt("index", shiftIndex);

		if (D)
			Log.i(TAG, "GetItem ShiftIndex: " + shiftIndex);

		switch (pos) {
		case 0: // Members
			f = new ShiftDetailMembersFragment();
			args.putSerializable("shift", party.getSchedule().get(shiftIndex));
			break;
		case 1: // Numbers
			f = new ShiftDetailNumbersFragment();
			args.putSerializable("party", party);
			break;
		case 2:// Bags
			f = new ShiftDetailBagsFragment();
			args.putSerializable("party", party);
			break;
		}

		if (D)
			Log.i(TAG, "GetItem Party: " + party.getName());

		f.setArguments(args);
		return f;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return context.getResources().getString(
					R.string.shift_detail_members);

		case 1:
			return context.getResources().getString(
					R.string.shift_detail_numbers);
		case 2:
			return context.getResources().getString(R.string.shift_detail_bags);
		}
		return null;
	}
}
