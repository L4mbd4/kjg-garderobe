package Model;

import java.util.Calendar;
import java.util.LinkedList;

public class Party implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5367206849080459128L;
	private String name;
	private final Calendar date;
	private int shiftCount;
	private int shiftLength;
	private LinkedList<CloakroomNumber> numbers;
	private LinkedList<CloakroomBag> bags;
	private final LinkedList<CloakroomShift> schedule;

	public Party(String name, int shiftCount, int shiftLength) {
		super();
		this.name = name;
		this.date = Calendar.getInstance();
		this.shiftCount = shiftCount;
		this.shiftLength = shiftLength;

		this.schedule = new LinkedList<CloakroomShift>();
		this.numbers = new LinkedList<CloakroomNumber>();
		this.bags = new LinkedList<CloakroomBag>();

		updateShifts();
	}

	private void updateShifts() {
		this.schedule.clear();

		Calendar start = (Calendar) date.clone();
		Calendar end = (Calendar) date.clone();

		for (int i = 0; i < shiftCount; i++) {
			CloakroomShift s = new CloakroomShift();

			s.setLength(shiftLength);
			s.setNumber(i + 1);

			start.add(Calendar.MINUTE, i * shiftLength);
			s.setStart((Calendar) start.clone());

			end.add(Calendar.MINUTE, (i + 1) * shiftLength);
			s.setEnd((Calendar) end.clone());

			this.schedule.add(s);
			start = (Calendar) date.clone();
			end = (Calendar) date.clone();
		}
	}

	public void updateShift(int shiftIndex, CloakroomShift shift) {
		schedule.set(shiftIndex, shift);
	}

	public void addNumber(CloakroomNumber n) {
		numbers.add(n);
	}

	public void addBag(CloakroomBag b) {
		bags.add(b);
	}

	public int getShiftCount() {
		return shiftCount;
	}

	public void setShiftCount(int shiftCount) {
		this.shiftCount = shiftCount;
	}

	public int getShiftLength() {
		return shiftLength;
	}

	public void setShiftLength(int shiftLength) {
		this.shiftLength = shiftLength;
	}

	public LinkedList<CloakroomShift> getSchedule() {
		return schedule;
	}

	public Calendar getDate() {
		return date;
	}

	public void setNumbers(LinkedList<CloakroomNumber> numbers) {
		this.numbers = numbers;
	}

	public void setBags(LinkedList<CloakroomBag> bags) {
		this.bags = bags;
	}

	public void setDate(int y, int m, int d) {
		date.set(y, m, d);
	}

	public void setTime(int h, int m) {
		date.set(Calendar.HOUR_OF_DAY, h);
		date.set(Calendar.MINUTE, m);

		updateShifts();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedList<CloakroomNumber> getNumbers() {
		return numbers;
	}

	public LinkedList<CloakroomBag> getBags() {
		return bags;
	}
}
