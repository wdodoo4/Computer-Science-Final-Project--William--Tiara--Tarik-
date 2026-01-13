package project;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: Coding out Teacher class
 */


public class Teacher {

	// Attributes

	public String teacherId;
	public String fullName;

	// constructor

	public Teacher(String teacherId, String fullName) {
		this.teacherId = teacherId;
		this.fullName = fullName;
	}

	// Getter method

	public String getTeacherId() {
		return teacherId;
	}

	public String getFullName() {
		return fullName;
	}

}
