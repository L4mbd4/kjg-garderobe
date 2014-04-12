package org.kjg.garderobe;

import static ch.lambdaj.Lambda.join;

import java.util.ArrayList;
import java.util.Calendar;

import Model.CloakroomShift;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScheduleListAdapter extends ArrayAdapter<CloakroomShift> {
	@SuppressWarnings("unused")
	private final Context context;
	private final ArrayList<CloakroomShift> items;
	private final LayoutInflater li;

	public ScheduleListAdapter(Context context, ArrayList<CloakroomShift> items) {
		super(context, 0, items);

		this.context = context;
		this.items = items;
		this.li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		v = li.inflate(R.layout.schedule_list_item, null);
		final CloakroomShift i = items.get(position);

		if (i != null) {
			final TextView tvw_number = (TextView) v
					.findViewById(R.id.tvw_schedule_item_number);
			final TextView tvw_time = (TextView) v
					.findViewById(R.id.tvw_schedule_item_time);
			final TextView tvw_members = (TextView) v
					.findViewById(R.id.tvw_schedule_item_members);
			// final TextView tvw_members_header = (TextView) v
			// .findViewById(R.id.tvw_schedule_item_members_header);
			final ImageView ivw_status = (ImageView) v
					.findViewById(R.id.ivw_schedule_item_status);

			// Check if shift is running
			Calendar curr = Calendar.getInstance();
			if (curr.getTimeInMillis() > i.getStart().getTimeInMillis()
					&& curr.getTimeInMillis() < i.getEnd().getTimeInMillis()) {
				// shift is running -> set background of item green
				final RelativeLayout layout = (RelativeLayout) v
						.findViewById(R.id.layout_schedule_item);
				layout.setBackgroundColor(Color.parseColor("#BCF5A9"));
			}
			if (i.getMembers().length < 4) {
				// less than 4 members -> red
				// tvw_members_header.setTextColor(Color.RED);
				ivw_status.setImageResource(R.drawable.cross);
			} else if (i.getMembers().length == 4) {
				// 4 members -> yellow
				// tvw_members_header.setTextColor(Color.parseColor("#0040FF"));
				ivw_status.setImageResource(R.drawable.exclamation);
			} else if (i.getMembers().length > 4 && i.getMembers().length <= 6) {
				// 5 or 6 members -> green
				ivw_status.setImageResource(R.drawable.ok);
				// tvw_members_header.setTextColor(Color.parseColor("#008a17"));
			} else {
				// more than 6 members -> orange
				ivw_status.setImageResource(R.drawable.exclamation);
				// tvw_members_header.setTextColor(Color.parseColor("#DF7401"));
			}

			tvw_number.setText(String.format(
					v.getResources().getString(
							R.string.text_tvw_schedule_item_number),
					String.valueOf(i.getNumber())));
			tvw_number.setPaintFlags(tvw_number.getPaintFlags()
					| Paint.UNDERLINE_TEXT_FLAG);

			tvw_time.setText(String.format("%02d:%02d",
					i.getStart().get(Calendar.HOUR_OF_DAY),
					i.getStart().get(Calendar.MINUTE))
					+ " - "
					+ String.format("%02d:%02d",
							i.getEnd().get(Calendar.HOUR_OF_DAY), i.getEnd()
									.get(Calendar.MINUTE)));

			String mem = "";

			if (i.getMembers().length > 0) {
				mem = join(i.getMembers());
			} else {
				mem += v.getResources().getString(R.string.no_members);
			}

			tvw_members.setText(mem);
		}

		return v;
	}
}
