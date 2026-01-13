package project;

/*
 * Name: Tiara,Tarik,William
 * Date: 2026.01.13
 * Description: Coding TimeBlcok
 */

public class TimeBlock {

	// Attributes

	public String day;
	public String startTime;
	public String endTime;

	// Constructor

	public TimeBlock(String day, String startTime, String endTime) {
		this.day = day;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	// Getter method

	public String getDay() {
		return day;
	}

	public String getStartDay() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

}
