package org.kjg.garderobe;

import static ch.lambdaj.Lambda.filter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;

import Model.CloakroomBag;
import Model.CloakroomShift;
import Model.Party;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import ch.lambdaj.function.matcher.Predicate;
import de.timroes.swipetodismiss.SwipeDismissList;

public class ShiftDetailBagsFragment extends Fragment {
	private final boolean D = true;
	private final String TAG = "DetailFragment - Bags";

	private ListView lvw_bags;
	private BagListAdapter listAdapter;

	private SwipeDismissList swipeList_Bags;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (D)
			Log.i(TAG, "***Begin - OnCreate***");

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_bags, container, false);

		this.lvw_bags = (ListView) view.findViewById(R.id.lvw_bags);

		this.lvw_bags.setEmptyView(view.findViewById(R.id.tvw_empty_lvw_bags));

		final CloakroomShift s = ((MainActivity) getActivity())
				.getCurrentParty().getSchedule()
				.get(getArguments().getInt("index"));

		if (D)
			Log.i(TAG, "Selected Shift: "
					+ (getArguments().getInt("index") + 1));

		Party p = ((MainActivity) getActivity()).getCurrentParty();

		if (D)
			Log.i(TAG, "Selected Party: " + p.getName());

		Matcher<CloakroomBag> timeMatcher = new Predicate<CloakroomBag>() {

			@Override
			public boolean apply(CloakroomBag n) {
				if (n.getTimeAdded().getTimeInMillis() >= s.getStart()
						.getTimeInMillis()
						&& n.getTimeAdded().getTimeInMillis() <= s.getEnd()
								.getTimeInMillis()) {
					return true;

				} else {
					return false;
				}
			}

		};

		List<CloakroomBag> l = filter(timeMatcher, p.getBags());

		ArrayList<CloakroomBag> items = new ArrayList<CloakroomBag>(l);

		if (D)
			Log.i(TAG, "Filtered Bags: " + items.size());

		listAdapter = new BagListAdapter(getActivity(), items);
		this.lvw_bags.setAdapter(listAdapter);

		// Set onClick of ListView
		this.lvw_bags.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View current,
					int pos, long id) {
				// Show toast with comment
				Toast.makeText(getActivity(),
						listAdapter.getItem(pos).getComment(),
						Toast.LENGTH_LONG).show();
				return false;
			}

		});

		// Configure swipe delete for ListView
		SwipeDismissList.OnDismissCallback callback = new SwipeDismissList.OnDismissCallback() {
			@Override
			public SwipeDismissList.Undoable onDismiss(AbsListView listView,
					final int position) {
				// Delete the item :
				final CloakroomBag itemToDelete = listAdapter.getItem(position);
				listAdapter.remove(itemToDelete);

				if (D)
					Log.i(TAG, "Removed Bag: " + itemToDelete.getNumber());

				return new SwipeDismissList.Undoable() {
					@Override
					public void undo() {
						// Return the item at its previous position again
						listAdapter.insert(itemToDelete, position);

						if (D)
							Log.i(TAG,
									"Undone deletion: "
											+ itemToDelete.getNumber());
					}

					@Override
					public String getTitle() {
						return String.format(getActivity().getResources()
								.getString(R.string.undo_message), itemToDelete
								.getNumber());
					}

					@Override
					public void discard() {
						// item finally deleted -> update file
						LinkedList<CloakroomBag> l = new LinkedList<CloakroomBag>();
						for (int i = 0; i < listAdapter.getCount(); i++) {
							l.add(listAdapter.getItem(i));
						}

						((MainActivity) getActivity()).getCurrentParty()
								.setBags(l);

						((MainActivity) getActivity()).saveCurrentParty();

						if (D)
							Log.i(TAG,
									"Discarded Bag: "
											+ itemToDelete.getNumber());
					}
				};
			}
		};

		swipeList_Bags = new SwipeDismissList(this.lvw_bags, callback);

		swipeList_Bags.setRequireTouchBeforeDismiss(false);
		swipeList_Bags.setAutoHideDelay(3000);

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
		inflater.inflate(R.menu.fragment_bags, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case R.id.action_add_bag: // add new number
			if (D)
				Log.i(TAG, "Add Bag requested");

			Intent i = new Intent(getActivity(), AddNumbagActivity.class);
			i.putExtra("mode", AddNumbagActivity.GET_BAG);
			this.startActivityForResult(i, AddNumbagActivity.GET_BAG);
			return true;
		case R.id.action_settings:
			((MainActivity) getActivity()).settingsClicked();
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == AddNumbagActivity.GET_BAG) {
			if (data.hasExtra("bag")) {
				CloakroomBag n = (CloakroomBag) data
						.getSerializableExtra("bag");
				listAdapter.add(n);
				((MainActivity) getActivity()).getCurrentParty().addBag(n);
				((MainActivity) getActivity()).saveCurrentParty();

				if (D)
					Log.i(TAG, "Added Bag: " + n.getNumber());
			}
		}

	}

	@Override
	public void onStop() {
		super.onStop();
		swipeList_Bags.discardUndo();
	}
}
