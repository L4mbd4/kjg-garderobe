package Model;

import java.io.Serializable;

public class ChecklistItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7429132592681394063L;

	private String title;

	private boolean isChecked;

	public ChecklistItem(String title, boolean isChecked) {
		super();
		this.title = title;
		this.isChecked = isChecked;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String s) {
		this.title = s;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean b) {
		this.isChecked = b;
	}

}
