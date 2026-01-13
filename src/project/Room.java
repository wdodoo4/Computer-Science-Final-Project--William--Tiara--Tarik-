package project;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: Coding Room 
 */

public class Room {

	// Attributes

	public String roomNumber;
	public int capacity;

	// constructor

	public Room(String roomNumber, int capacity) {
		this.roomNumber = roomNumber;
		this.capacity = capacity;
	}

	// Getter method

	public String getRoomNumber() {
		return roomNumber;
	}

	public int getCapacity() {
		return capacity;
	}

}
