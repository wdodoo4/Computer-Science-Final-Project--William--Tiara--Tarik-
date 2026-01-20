package project;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: Room is a basic classroom
 */

public class Room extends Resource {

	// Attributes

	private String roomNumber;

	// constructor

	public Room(String id, String roomNumber) {
		super(id, "Room " + roomNumber);
		this.roomNumber = roomNumber; 
	}

	public Room(String id, String name, String roomNumber) {
        super(id, name);
        this.roomNumber = roomNumber;
	}

	// Getter methods

	public String getRoomNumber() {
		return roomNumber;
	}
	
	public String getDescription() {
        return "Room " + roomNumber;
    }
	
    public String toString() {
        return getName() + " (" + roomNumber + ")";
    }
}