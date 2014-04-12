package Model;

import java.util.Calendar;

public class CloakroomBag implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -625568636371655706L;
	public static final int OTHER = 0;
	public static final int LOST = 5;
	public static final int MISTAKE = 3;
	public static final int LEFT = 2;
	public static final int WRONG_CONTENT = 4;

	private int number;
	private String comment;
	private final Calendar timeAdded;
	private int reason;

	public int getReason() {
		return reason;
	}

	public void setReason(int reason) {
		this.reason = reason;
	}

	public CloakroomBag(int number, String comment, Calendar timeAdded) {
		this.number = number;
		this.comment = comment;
		this.timeAdded = timeAdded;
	}

	public CloakroomBag() {
		this(0, "", Calendar.getInstance());
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Calendar getTimeAdded() {
		return timeAdded;
	}

	public void setTimeAdded(int h, int m) {
		this.timeAdded.set(Calendar.HOUR_OF_DAY, h);
		this.timeAdded.set(Calendar.MINUTE, m);
	}
}
