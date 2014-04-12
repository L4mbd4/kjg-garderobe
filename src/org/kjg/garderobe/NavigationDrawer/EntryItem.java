package org.kjg.garderobe.NavigationDrawer;

public class EntryItem implements ListItem {

	private final String title;

	public EntryItem(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	@Override
	public boolean isHeader() {
		return false;
	}

	@Override
	public boolean isSpinner() {
		return false;
	}

}
