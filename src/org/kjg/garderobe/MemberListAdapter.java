package org.kjg.garderobe;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberListAdapter extends ArrayAdapter<String> {
	private final boolean D = false;
	private final String TAG = "MemberAdapter";

	@SuppressWarnings("unused")
	private final Context context;
	private final ArrayList<String> items;
	private final LayoutInflater li;

	public MemberListAdapter(Context context, ArrayList<String> items) {
		super(context, 0, items);

		this.context = context;
		this.items = items;
		this.li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (D)
			Log.i(TAG, "Created with " + items.size() + " items");
	}

	@SuppressLint("DefaultLocale")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		v = li.inflate(R.layout.member_list_item, null);
		final String s = items.get(position);

		if (s != null) {
			final TextView tvw_name = (TextView) v
					.findViewById(R.id.tvw_member_item_name);
			final ImageView ivw_icon = (ImageView) v
					.findViewById(R.id.ivw_member_item_icon);

			if (s.trim().toUpperCase().equals("STEFFEN")) {

				if (D)
					Log.i(TAG, "Member 'Steffen' found!");

				ivw_icon.setImageResource(R.drawable.steffen);
				tvw_name.setShadowLayer(15.0f, 0.0f, 0.0f,
						Color.parseColor("#ffe766"));
			} else {
				ivw_icon.setImageResource(R.drawable.person);
			}

			tvw_name.setText(s);
		}

		return v;
	}
}
