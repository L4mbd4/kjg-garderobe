package org.kjg.garderobe;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShiftDetailViewFragment extends Fragment {
	private final boolean D = true;
	private final String TAG = "ShiftDetailFragment";

	private ViewPager pager;

	ShiftPagerAdapter pagerAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (D)
			Log.i(TAG, "***Begin - OnCreate***");

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_shift_detail_view,
				container, false);

		((MainActivity) getActivity()).getActionBar().setTitle(
				((MainActivity) getActivity()).getCurrentParty().getName());

		final int shiftIndex = this.getArguments().getInt("index", 0);

		if (D)
			Log.i(TAG, "Selected Shift: " + (shiftIndex + 1));

		pagerAdapter = new ShiftPagerAdapter(this.getFragmentManager(),
				getActivity(), shiftIndex,
				((MainActivity) getActivity()).getCurrentParty());

		if (D)
			Log.i(TAG, "Selected Party: "
					+ ((MainActivity) getActivity()).getCurrentParty()
							.getName());

		pager = (ViewPager) view.findViewById(R.id.shift_detail_pager);
		pager.setAdapter(pagerAdapter);

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int pos, float arg1, int arg2) {
				String titleSuffix = "";

				// if (D)
				// Log.i(TAG, "Selected page changed: " + pos);

				switch (pos) {
				case 0: // Members
					titleSuffix = getActivity().getResources().getString(
							R.string.shift_detail_members);
					break;
				case 1: // Numbers
					titleSuffix = getActivity().getResources().getString(
							R.string.shift_detail_numbers);
					break;
				case 2: // Bags
					titleSuffix = getActivity().getResources().getString(
							R.string.shift_detail_bags);
					break;
				}

				getActivity()
						.getActionBar()
						.setTitle(
								String.format(
										getActivity()
												.getResources()
												.getString(
														R.string.text_tvw_schedule_item_number),
										shiftIndex + 1)
										+ " - " + titleSuffix);

			}

			@Override
			public void onPageSelected(int pos) {
				// if (D)
				// Log.i(TAG, "Page scrolled: " + pos);
			}

		});

		// Set Members to default
		// pager.setCurrentItem(0);

		this.setRetainInstance(true);

		if (D)
			Log.i(TAG, "***End - OnCreate***");

		return view;
	}
}
