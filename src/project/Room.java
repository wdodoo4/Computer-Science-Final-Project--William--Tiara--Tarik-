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

	public Room(String id, String name, String roomNumber, int capacity) {
        super(id, name);
        this.roomNumber = roomNumber;
		this.capacity = capacity;
	}

	// Getter methods

	public String getRoomNumber() {
		return roomNumber;
	}

	public int getCapacity() {
		return capacity;
	}

    //Clean output
    public String toString() {
        return getName() + " (" + roomNumber + ", cap " + capacity + ")";
    }
}
