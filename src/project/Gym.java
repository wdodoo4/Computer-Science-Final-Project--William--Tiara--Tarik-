package project;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: Gym is a special type of room that can be booked
 */

public class Gym extends Room {
    public Gym(String id, String roomNumber, int capacity) {
        super(id, roomNumber, capacity);
    }
    
    public String getDescription() {
        return "Gym " + getRoomNumber() + " (capacity: " + getCapacity() + ")";
    }
}