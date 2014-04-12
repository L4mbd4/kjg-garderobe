package org.kjg.garderobe;

import static ch.lambdaj.Lambda.filter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;

import Model.CloakroomNumber;
import Model.CloakroomShift;
import Model.Party;
import Model.Serializer;
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

public class ShiftDetailNumbersFragment extends Fragment {
	private final boolean D = false;
	private final String TAG = "DetailFragment - Numbers";

	private ListView lvw_numbers;
	private NumberListAdapter listAdapter;

	private SwipeDismissList swipeList_Numbers;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (D)
			Log.i(TAG, "***Begin - OnCreate***");

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_numbers, container,
				false);

		this.lvw_numbers = (ListView) view.findViewById(R.id.lvw_numbers);

		this.lvw_numbers.setEmptyView(view
				.findViewById(R.id.tvw_empty_lvw_numbers));

		final CloakroomShift s = ((MainActivity) getActivity())
				.getCurrentParty().getSchedule()
				.get(getArguments().getInt("index"));

		if (D)
			Log.i(TAG, "Selected Shift: "
					+ (getArguments().getInt("index") + 1));

		Party p = ((MainActivity) getActivity()).getCurrentParty();

		if (D)
			Log.i(TAG, "Selected Party: " + p.getName());

		Matcher<CloakroomNumber> timeMatcher = new Predicate<CloakroomNumber>() {

			@Override
			public boolean apply(CloakroomNumber n) {
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

		List<CloakroomNumber> l = filter(timeMatcher, p.getNumbers());

		ArrayList<CloakroomNumber> items = new ArrayList<CloakroomNumber>(l);

		if (D)
			Log.i(TAG, "Filtered Numbers: " + items.size());

		listAdapter = new NumberListAdapter(getActivity(), items);
		this.lvw_numbers.setAdapter(listAdapter);

		// Set onClick of ListView
		this.lvw_numbers
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View current, int pos, long id) {
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
				final CloakroomNumber itemToDelete = listAdapter
						.getItem(position);
				listAdapter.remove(itemToDelete);

				if (D)
					Log.i(TAG, "Removed Number: " + itemToDelete.getNumber());

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
						LinkedList<CloakroomNumber> l = new LinkedList<CloakroomNumber>();
						for (int i = 0; i < listAdapter.getCount(); i++) {
							l.add(listAdapter.getItem(i));
						}

						((MainActivity) getActivity()).getCurrentParty()
								.setNumbers(l);

						((MainActivity) getActivity()).saveCurrentParty();

						if (D)
							Log.i(TAG,
									"Discarded Number: "
											+ itemToDelete.getNumber());
					}
				};
			}
		};

		swipeList_Numbers = new SwipeDismissList(this.lvw_numbers, callback);

		swipeList_Numbers.setRequireTouchBeforeDismiss(false);
		swipeList_Numbers.setAutoHideDelay(3000);

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
		inflater.inflate(R.menu.fragment_numbers, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case R.id.action_add_number: // add new number
			if (D)
				Log.i(TAG, "Add Number requested");

			Intent i = new Intent(getActivity(), AddNumbagActivity.class);
			i.putExtra("mode", AddNumbagActivity.GET_NUMBER);
			this.startActivityForResult(i, AddNumbagActivity.GET_NUMBER);
			return true;
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == AddNumbagActivity.GET_NUMBER) {
			if (data.hasExtra("number")) {
				CloakroomNumber n = (CloakroomNumber) data
						.getSerializableExtra("number");
				listAdapter.add(n);
				((MainActivity) getActivity()).getCurrentParty().addNumber(n);
				Serializer.serializeParty(
						((MainActivity) getActivity()).getCurrentParty(),
						getActivity());

				if (D)
					Log.i(TAG, "Added Number: " + n.getNumber());
			}
		}

	}

	@Override
	public void onStop() {
		super.onStop();
		swipeList_Numbers.discardUndo();
	}
}
