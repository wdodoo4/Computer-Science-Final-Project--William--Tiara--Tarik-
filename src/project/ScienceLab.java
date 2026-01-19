package project;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: ScienceLab is a special type of room that can be booked
 */

public class ScienceLab extends Room {
	 public ScienceLab(String id, String roomNumber) {
	        super(id, roomNumber);
	    }
	    
	    public String getDescription() {
	        return "Science Lab " + getRoomNumber();
	    }
}
