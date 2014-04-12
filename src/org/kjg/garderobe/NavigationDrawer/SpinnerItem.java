package org.kjg.garderobe.NavigationDrawer;

public class SpinnerItem implements ListItem {

	@Override
	public boolean isHeader() {
		return false;
	}

	@Override
	public boolean isSpinner() {
		return true;
	}

}
