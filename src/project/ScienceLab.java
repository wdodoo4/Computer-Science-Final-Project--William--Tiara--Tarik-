package project;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: ScienceLab is a special type of room that can be booked
 */

public class ScienceLab extends Room {
	 public ScienceLab(String id, String roomNumber, int capacity) {
	        super(id, roomNumber, capacity);
	    }
	    
	    public String getDescription() {
	        return "Sience Lab " + getRoomNumber() + " (capacity: " + getCapacity() + ")";
	    }
}
