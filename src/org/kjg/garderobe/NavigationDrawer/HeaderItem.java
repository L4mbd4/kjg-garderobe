package org.kjg.garderobe.NavigationDrawer;

public class HeaderItem implements ListItem {

	private final String title;

	public HeaderItem(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	@Override
	public boolean isHeader() {
		return true;
	}

	@Override
	public boolean isSpinner() {
		return false;
	}

}
