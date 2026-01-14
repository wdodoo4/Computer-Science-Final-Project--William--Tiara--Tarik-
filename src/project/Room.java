package project;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: Room is a basic classroom
 */

public class Room extends Resource {

	// Attributes

	private String roomNumber;
	private int capacity;

	// constructor


	public Room(String id, String roomNumber, int capacity) {
		super(id, "Room " + roomNumber);
		this.roomNumber = roomNumber;
	}

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
	
	public String getDescription() {
        return "Room " + roomNumber + " (capacity: " + capacity + ")";
    }
	
    public String toString() {
        return getName() + " (" + roomNumber + ", cap " + capacity + ")";
    }
}
