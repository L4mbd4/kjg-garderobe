package org.kjg.garderobe;

import java.util.ArrayList;
import java.util.LinkedList;

import Model.CloakroomShift;
import Model.Serializer;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import de.timroes.swipetodismiss.SwipeDismissList;

public class ShiftDetailMembersFragment extends Fragment {
	private final boolean D = false;
	private final String TAG = "DetailFragment - Members";

	private ListView lvw_members;

	private SwipeDismissList swipeList_Members;
	private MemberListAdapter listAdapter;

	private static CloakroomShift shift;
	private static int shiftIndex;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (D)
			Log.i(TAG, "***Begin - OnCreate***");

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_shift_detail_members,
				container, false);

		this.lvw_members = (ListView) view
				.findViewById(R.id.lvw_shift_detail_members);

		this.lvw_members.setEmptyView(view
				.findViewById(R.id.tvw_empty_lvw_shift_detail_members));

		shiftIndex = this.getArguments().getInt("index");
		shift = (CloakroomShift) this.getArguments().getSerializable("shift");

		if (D) {
			Log.i(TAG, "Selected ShiftIndex: " + (shiftIndex + 1));
			Log.i(TAG, "Selected Shift: " + (shift.getNumber()));
		}

		// Set adapter for ListView
		ArrayList<String> members = new ArrayList<String>();

		for (String memberName : shift.getMembers()) {
			members.add(memberName);
		}

		// listAdapter = new ArrayAdapter<String>(getActivity(),
		// android.R.layout.simple_list_item_1, members);

		if (D)
			Log.i(TAG, "Members: " + members.size());

		listAdapter = new MemberListAdapter(this.getActivity(), members);
		this.lvw_members.setAdapter(listAdapter);

		// Configure swipe delete for ListView
		SwipeDismissList.OnDismissCallback callback = new SwipeDismissList.OnDismissCallback() {
			@Override
			public SwipeDismissList.Undoable onDismiss(AbsListView listView,
					final int position) {
				// Delete the item :
				final String itemToDelete = listAdapter.getItem(position);
				listAdapter.remove(itemToDelete);

				if (D)
					Log.i(TAG, "Removed Member: " + itemToDelete);

				return new SwipeDismissList.Undoable() {
					@Override
					public void undo() {
						// Return the item at its previous position again
						listAdapter.insert(itemToDelete, position);

						if (D)
							Log.i(TAG, "Undone deletion: " + itemToDelete);
					}

					@Override
					public String getTitle() {
						return String
								.format(getActivity().getResources().getString(
										R.string.undo_message), itemToDelete);
					}

					@Override
					public void discard() {
						// item finally deleted -> update file
						LinkedList<String> l = new LinkedList<String>();
						for (int i = 0; i < listAdapter.getCount(); i++) {
							l.add(listAdapter.getItem(i));
						}
						shift.setMembers(l);

						((MainActivity) getActivity()).getCurrentParty()
								.updateShift(shiftIndex, shift);

						((MainActivity) getActivity()).saveCurrentParty();

						if (D)
							Log.i(TAG, "Discarded Member: " + itemToDelete);
					}
				};
			}
		};

		swipeList_Members = new SwipeDismissList(this.lvw_members, callback);

		swipeList_Members.setRequireTouchBeforeDismiss(false);
		swipeList_Members.setAutoHideDelay(3000);

		this.setHasOptionsMenu(true);
		this.setRetainInstance(true);

		if (D)
			Log.i(TAG, "***End - OnCreate***");

		return view;
	}

	@Override
	public void onStop() {
		super.onStop();
		swipeList_Members.discardUndo();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.fragment_shift_detail_view, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case R.id.action_add_member: // add new member
			showAddMemberDialog();
			return true;
		case R.id.action_settings: // settings

			return true;
		default:
			return super.onOptionsItemSelected(item);

		}

	}

	private void showAddMemberDialog() {

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		final InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		alert.setTitle(getActivity().getResources().getString(
				R.string.action_add_member));

		// Set an EditText view to get user input
		final EditText input = new EditText(getActivity());
		input.setHint(getActivity().getResources().getString(
				R.string.member_name));
		alert.setView(input);

		alert.setPositiveButton(
				getActivity().getResources().getString(R.string.dialog_add),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {

						shift.addMember(input.getText().toString().trim());
						listAdapter.add(input.getText().toString().trim());

						((MainActivity) getActivity()).getCurrentParty()
								.updateShift(shiftIndex, shift);

						Serializer.serializeParty(
								((MainActivity) getActivity())
										.getCurrentParty(), getActivity());

						if (D)
							Log.i(TAG, "Added Member "
									+ input.getText().toString().trim()
									+ " to shift " + (shiftIndex + 1));

						updateAdpater();

						imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
					}
				});

		alert.setNegativeButton(
				getActivity().getResources().getString(R.string.dialog_cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
						imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

						if (D)
							Log.i(TAG, "Add member cancelled");

						Log.i(TAG, "SI: " + shiftIndex);
					}
				});

		alert.show();
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

	private void updateAdpater() {
		listAdapter.clear();
		for (String memberName : shift.getMembers()) {
			listAdapter.add(memberName);
		}

		if (D)
			Log.i(TAG, "Adapter updated");
	}
}
