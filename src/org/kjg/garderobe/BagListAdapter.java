package org.kjg.garderobe;

import java.util.ArrayList;
import java.util.Calendar;

import Model.CloakroomBag;
import Model.CloakroomNumber;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BagListAdapter extends ArrayAdapter<CloakroomBag> {
	private final Context context;
	private final ArrayList<CloakroomBag> items;
	private final LayoutInflater li;

	public BagListAdapter(Context context, ArrayList<CloakroomBag> items) {
		super(context, 0, items);

		this.context = context;
		this.items = items;
		this.li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		v = li.inflate(R.layout.numbag_list_item, null);
		final CloakroomBag i = items.get(position);

		final TextView txt_number = (TextView) v
				.findViewById(R.id.txt_numbag_item_number);
		final TextView txt_time = (TextView) v
				.findViewById(R.id.txt_numbag_item_time);
		final TextView txt_reason = (TextView) v
				.findViewById(R.id.txt_numbag_item_reason);
		final TextView txt_comment = (TextView) v
				.findViewById(R.id.txt_numbag_item_comment);

		if (i != null) {
			// Number
			txt_number.setText(String.valueOf(i.getNumber()));

			// Reason
			String reason = "";
			switch (i.getReason()) {
			case CloakroomNumber.FOUND:
				reason = context.getResources()
						.getString(R.string.reason_found);
				break;
			case CloakroomBag.LEFT:
				reason = context.getResources().getString(R.string.reason_left);
				break;
			case CloakroomBag.MISTAKE:
				reason = context.getResources().getString(
						R.string.reason_mistake);
				break;
			case CloakroomBag.OTHER:
				reason = context.getResources()
						.getString(R.string.reason_other);
			case CloakroomBag.WRONG_CONTENT:
				reason = context.getResources()
						.getString(R.string.reason_wrong);
				break;
			}
			txt_reason.setText(String.format(
					context.getResources().getString(
							R.string.text_txt_numbag_reason), reason));

			// Time
			txt_time.setText(String.format(
					context.getResources().getString(
							R.string.text_txt_numbag_time), String.format(
							"%02d:%02d",
							i.getTimeAdded().get(Calendar.HOUR_OF_DAY), i
									.getTimeAdded().get(Calendar.MINUTE))));

			// Comment
			if (i.getComment().equals("")) {
				txt_comment.setText(String.format(context.getResources()
						.getString(R.string.text_txt_numbag_comment), context
						.getResources().getString(R.string.no_comment)));
			} else {
				txt_comment.setText(String.format(context.getResources()
						.getString(R.string.text_txt_numbag_comment), i
						.getComment()));
			}

		}
		return v;
	}
}
