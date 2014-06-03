package Model;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;

public class CloakroomShift implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2419891465394740635L;
	private final LinkedList<String> members;
	private String comment;
	private int length;
	private int number;
	private Calendar start;
	private Calendar end;

	public CloakroomShift(String comment, LinkedList<String> members) {
		this.comment = comment;
		this.members = members;
	}

	public CloakroomShift() {
		this.comment = "";
		this.members = new LinkedList<String>();
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void addMember(String name) {
		members.add(name);
	}

	public String[] getMembers() {
		return members.toArray(new String[members.size()]);
	}

	public void setMembers(Collection<? extends String> members) {
		this.members.clear();
		this.members.addAll(members);
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Calendar getStart() {
		return start;
	}

	public void setStart(Calendar start) {
		this.start = start;
		this.start.set(Calendar.SECOND, 0);
		this.start.set(Calendar.MILLISECOND, 0);
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
		this.end.set(Calendar.SECOND, 0);
		this.end.set(Calendar.MILLISECOND, 0);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
