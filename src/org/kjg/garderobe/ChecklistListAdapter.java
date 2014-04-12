package org.kjg.garderobe;

import java.util.ArrayList;

import Model.ChecklistItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChecklistListAdapter extends ArrayAdapter<ChecklistItem> {
	@SuppressWarnings("unused")
	private final Context context;
	private final ArrayList<ChecklistItem> items;
	private final LayoutInflater li;

	public ChecklistListAdapter(Context context, ArrayList<ChecklistItem> items) {
		super(context, 0, items);

		this.context = context;
		this.items = items;
		this.li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		v = li.inflate(R.layout.checklist_list_item, null);
		final ChecklistItem i = items.get(position);

		if (i != null) {
			final TextView tvw_title = (TextView) v
					.findViewById(R.id.tvw_checklist_item_title);
			final ImageView ivw_checked = (ImageView) v
					.findViewById(R.id.ivw_checklist_item_checked);

			tvw_title.setText(i.getTitle());

			if (i.isChecked()) {
				ivw_checked.setImageResource(R.drawable.ok);
			} else {
				ivw_checked.setImageResource(R.drawable.cross);
			}

		}

		return v;
	}
}
