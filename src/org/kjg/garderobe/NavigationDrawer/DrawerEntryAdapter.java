package org.kjg.garderobe.NavigationDrawer;

import java.util.ArrayList;

import org.kjg.garderobe.MainActivity;
import org.kjg.garderobe.NewPartyFragment;
import org.kjg.garderobe.R;
import org.kjg.garderobe.ScheduleFragment;

import Model.Party;
import Model.Serializer;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class DrawerEntryAdapter extends ArrayAdapter<ListItem> {

	private final boolean D = true;
	private final String TAG = "DrawerAdapter";

	private final Context context;
	private final ArrayList<ListItem> items;
	private final LayoutInflater li;

	private Spinner sp_partys;
	private ArrayAdapter<String> spinnerAdapter;

	public DrawerEntryAdapter(Context context, ArrayList<ListItem> items) {
		super(context, 0, items);

		if (D)
			Log.i(TAG, "Constructor");

		this.context = context;
		this.items = items;
		this.li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// if (D)
		// Log.i(TAG, "getView: " + position);

		View v = convertView;
		final ListItem i = items.get(position);

		if (i != null) {
			if (i.isHeader()) {
				HeaderItem hi = (HeaderItem) i;
				v = li.inflate(R.layout.drawer_list_item_header, null);
				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);

				final TextView headerView = (TextView) v
						.findViewById(R.id.drawer_list_item_header_text);
				headerView.setText(hi.getTitle());
			} else if (i.isSpinner()) {
				v = li.inflate(R.layout.drawer_list_item_spinner, null);

				sp_partys = (Spinner) v.findViewById(R.id.list_spinner_partys);

				ArrayList<String> partys = new ArrayList<String>();

				for (Party p : Serializer.getPartys(context)) {
					partys.add(p.getName());
				}
				if (partys.size() == 0) {
					partys.add(context.getResources().getString(
							R.string.no_party));
				}

				spinnerAdapter = new ArrayAdapter<String>(context,
						R.layout.drawer_spinner_item, partys);

				sp_partys.setAdapter(spinnerAdapter);

				sp_partys
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View selected, int position, long id) {
								sp_partys_ItemSelected();
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {

							}

						});

			} else {
				EntryItem ei = (EntryItem) i;
				v = li.inflate(R.layout.drawer_list_item_entry, null);
				final TextView text = (TextView) v
						.findViewById(R.id.list_item_entry_title);
				text.setText(ei.getTitle());
			}
		}
		return v;
	}

	private void sp_partys_ItemSelected() {
		if (D)
			Log.i(TAG, "Party selected: "
					+ sp_partys.getSelectedItem().toString());

		if (sp_partys.getSelectedItem().toString() == context.getResources()
				.getString(R.string.no_party)) {
			// no party -> new party fragment

			FragmentTransaction ft = ((Activity) context).getFragmentManager()
					.beginTransaction();
			ft.replace(R.id.frame_container, new NewPartyFragment());
			ft.commit();
		} else {
			MainActivity a = (MainActivity) context;

			for (Party p : Serializer.getPartys(context)) {
				if (p.getName().equals(
						this.sp_partys.getSelectedItem().toString())) {
					a.setCurrentParty(p);
					a.closeDrawer();
				}
			}

			FragmentTransaction ft = ((Activity) context).getFragmentManager()
					.beginTransaction();
			ft.replace(R.id.frame_container, new ScheduleFragment());
			ft.commit();
		}
	}

	public void updateSpinnerAdapter() {
		if (D)
			Log.i(TAG, "Update party spinner");

		ArrayList<String> partys = new ArrayList<String>();

		for (Party p : Serializer.getPartys(context)) {
			partys.add(p.getName());
		}
		if (partys.size() == 0) {
			partys.add(context.getResources().getString(R.string.no_party));
			// no party -> new party fragment

			FragmentTransaction ft = ((Activity) context).getFragmentManager()
					.beginTransaction();
			ft.replace(R.id.frame_container, new NewPartyFragment());
			ft.commit();
		}

		spinnerAdapter.clear();
		spinnerAdapter.addAll(partys);
	}

	public void setSpinnerPartyActive(String name) {
		if (D)
			Log.i(TAG, "Spinner set party active :" + name);

		int index = 0;
		for (int i = 0; i < spinnerAdapter.getCount(); i++) {
			if (spinnerAdapter.getItem(i).equals(name)) {
				index = i;
				break;
			}
		}

		sp_partys.setSelection(index);
	}
}
