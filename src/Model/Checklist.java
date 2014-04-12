package Model;

import java.io.Serializable;
import java.util.List;

public class Checklist implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2305836421247906246L;
	private final List<ChecklistItem> list;

	public Checklist(List<ChecklistItem> list) {
		super();
		this.list = list;
	}

	public List<ChecklistItem> getList() {
		return list;
	}
}
