package org.kjg.garderobe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Model.ChecklistItem;
import Model.Serializer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class ChecklistActivity extends Activity {
	private final boolean D = false;
	private final String TAG = "Checklist";

	private ListView lvw_items;

	private ChecklistListAdapter listAdapter;
	private SwipeDetector swipe;
	private List<ChecklistItem> items;

	private boolean removeMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (D)
			Log.i(TAG, "***Begin - OnCreate***");

		setContentView(R.layout.activity_checklist);

		this.lvw_items = (ListView) findViewById(R.id.lvw_checklist_list);
		this.lvw_items.setEmptyView(this
				.findViewById(R.id.tvw_empty_lvw_checklist_list));

		removeMode = false;

		items = Serializer.deserializeChecklist(this);
		if (items == null) {
			items = new ArrayList<ChecklistItem>();
		}

		if (D)
			Log.i(TAG, "Deserialized Checklist (" + items.size() + " items)");

		ArrayList<ChecklistItem> array = new ArrayList<ChecklistItem>(items);
		listAdapter = new ChecklistListAdapter(this, array);

		swipe = new SwipeDetector();
		this.lvw_items.setAdapter(listAdapter);
		this.lvw_items.setOnTouchListener(swipe);
		this.lvw_items.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {

				if (!removeMode) {
					if (swipe.swipeDetected()
							&& swipe.getAction() == SwipeDetector.Action.LR) {
						// Left-To-Right -> check
						if (D)
							Log.i(TAG, "LR Swipe on item at position " + pos);
						listAdapter.getItem(pos).setChecked(true);
					} else if (swipe.swipeDetected()
							&& swipe.getAction() == SwipeDetector.Action.RL) {
						// Right-To-Left -> uncheck
						if (D)
							Log.i(TAG, "RL Swipe on item at position " + pos);
						listAdapter.getItem(pos).setChecked(false);
					} else if (swipe.swipeDetected()
							&& swipe.getAction() == SwipeDetector.Action.BT) {
						// Bottom-To-Top -> move up
						if (D)
							Log.i(TAG, "BT Swipe on item at position " + pos);

						if (pos > 0) {
							if (D)
								Log.i(TAG, "Swapped items " + pos + " & "
										+ (pos + 1));
							Collections.swap(items, pos, pos - 1);
							listAdapter.notifyDataSetChanged();
						}

					} else if (swipe.swipeDetected()
							&& swipe.getAction() == SwipeDetector.Action.TB) {
						// Top-To-Bottom -> move down
						if (D)
							Log.i(TAG, "TB Swipe on item at position " + pos);
					}

					listAdapter.notifyDataSetChanged();
				} else {
					removeItem(pos);
				}
			}
		});

		this.lvw_items
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> p, View v,
							int pos, long id) {
						listAdapter.getItem(pos).setChecked(
								!listAdapter.getItem(pos).isChecked());

						if (D)
							Log.i(TAG, "LongClick on Item at position " + pos);

						listAdapter.notifyDataSetChanged();
						return false;
					}

				});

		if (D)
			Log.i(TAG, "***End - OnCreate***");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.checklist, menu);
		return true;
	}

	@Override
	public void onStop() {
		super.onStop();
		Serializer.serializeChecklist(items, this);
		if (D)
			Log.i(TAG, "Checklist serialized");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_add_check_item) {
			addItem();
			return true;
		} else if (id == R.id.action_remove_check_item) {
			removeMode = !removeMode;

			if (D)
				Log.i(TAG, "Toggled remove mode: " + removeMode);

			if (removeMode) {
				item.setTitle(R.string.action_remove_check_item_deactivated);
			} else {
				item.setTitle(R.string.action_remove_check_item_activated);
			}
			return true;
		} else if (id == R.id.action_reset_check_items) {
			resetItems();
		}
		return super.onOptionsItemSelected(item);
	}

	private void resetItems() {
		for (int i = 0; i < listAdapter.getCount(); i++) {
			listAdapter.getItem(i).setChecked(false);
		}

		listAdapter.notifyDataSetChanged();

		if (D)
			Log.i(TAG, "Items reset");
	}

	private void addItem() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		alert.setTitle(getResources()
				.getString(R.string.dialog_add_check_title));

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		input.setHint(getResources().getString(R.string.dialog_add_check_hint));
		alert.setView(input);

		alert.setPositiveButton(getResources().getString(R.string.dialog_add),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {

						ChecklistItem itm = new ChecklistItem(input.getText()
								.toString(), false);
						listAdapter.add(itm);
						items.add(itm);
						listAdapter.notifyDataSetChanged();

						imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
					}
				});

		alert.setNegativeButton(getResources()
				.getString(R.string.dialog_cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
						imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
					}
				});

		alert.show();
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

	private void removeItem(int pos) {
		listAdapter.remove(listAdapter.getItem(pos));
		items.remove(pos);
		listAdapter.notifyDataSetChanged();

		if (D)
			Log.i(TAG, "Removed item at position " + pos);
	}
}
