package org.kjg.garderobe;

import java.util.ArrayList;

import Model.CloakroomShift;
import Model.Party;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ScheduleFragment extends Fragment {
	private final boolean D = true;
	private final String TAG = "ScheduleFragment";

	private ListView lvw_shifts;

	private Party p;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (D)
			Log.i(TAG, "***Begin - OnCreate***");

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_schedule, container,
				false);

		((MainActivity) getActivity()).getActionBar().setTitle(
				getActivity().getResources().getString(
						R.string.drawer_item_schedule));

		p = ((MainActivity) getActivity()).getCurrentParty();

		if (D)
			Log.i(TAG, "Selected Party: " + p.getName());

		this.lvw_shifts = (ListView) view.findViewById(R.id.lvw_shifts);

		ScheduleListAdapter a = new ScheduleListAdapter(getActivity(),
				new ArrayList<CloakroomShift>(p.getSchedule()));

		this.lvw_shifts.setAdapter(a);
		this.lvw_shifts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View current,
					int pos, long id) {
				if (D)
					Log.i(TAG, "Clicked on Shift " + pos + 1);

				Fragment f = new ShiftDetailViewFragment();
				Bundle args = new Bundle();
				args.putInt("index", pos);
				f.setArguments(args);
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.setCustomAnimations(R.animator.slide_in_left,
						R.animator.slide_out_right, 0, 0)
						.replace(R.id.frame_container, f).addToBackStack(null)
						.commit();

			}

		});

		this.setHasOptionsMenu(true);
		this.setRetainInstance(true);

		if (D)
			Log.i(TAG, "***End - OnCreate***");

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.fragment_schedule, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case R.id.action_delete_party: // delete Party
			AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

			alert.setTitle(getActivity().getResources().getString(
					R.string.action_delete_party));
			alert.setMessage(String.format(getActivity().getResources()
					.getString(R.string.confirm_delete_party),
					((MainActivity) getActivity()).getCurrentParty().getName()));

			alert.setPositiveButton(
					getActivity().getResources().getString(R.string.dialog_yes),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {

							String name = ((MainActivity) getActivity())
									.getCurrentParty().getName();
							getActivity()
									.deleteFile(
											((MainActivity) getActivity())
													.getCurrentParty()
													.getName()
													+ ".p");
							((MainActivity) getActivity()).getDrawerAdapter()
									.updateSpinnerAdapter();

							Toast.makeText(
									getActivity(),
									String.format(
											getActivity()
													.getResources()
													.getString(
															R.string.toast_party_deleted),
											name), Toast.LENGTH_SHORT).show();
							if (D)
								Log.i(TAG, "Deleted party: " + name);
						}
					});

			alert.setNegativeButton(
					getActivity().getResources().getString(R.string.dialog_no),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
							if (D)
								Log.i(TAG, "Delete canceled");
						}
					});

			alert.show();
			return true;
		case R.id.action_settings: // settings
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}

	}
}
