package project;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: ComputerLab is a special type of room that can be booked
 */

public class ComputerLab extends Room {
	 public ComputerLab(String id, String roomNumber) {
	        super(id, roomNumber);
	    }
	    
	    public String getDescription() {
	        return "Computer Lab " + getRoomNumber();
	    }
}
