package Model;

import java.util.Calendar;

public class CloakroomNumber implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2603799243418890674L;
	public static final int OTHER = 0;
	public static final int FOUND = 1;
	public static final int LEFT = 2;
	public static final int MISTAKE = 3;

	private int number;
	private String comment;
	private final Calendar timeAdded;
	private int reason;

	public CloakroomNumber(int num, String comment, int reason,
			Calendar timeAdded) {
		setNumber(num);
		setComment(comment);
		setReason(reason);
		this.timeAdded = timeAdded;
	}

	public CloakroomNumber() {
		this(0, "", 0, Calendar.getInstance());
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

	public void setTime(int h, int m) {
		this.timeAdded.set(Calendar.HOUR_OF_DAY, h);
		this.timeAdded.set(Calendar.MINUTE, m);
	}

	public int getReason() {
		return reason;
	}

	public void setReason(int reason) {
		this.reason = reason;
	}

}
