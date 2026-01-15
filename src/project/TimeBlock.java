package project;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: TimeBlock is a specific time slot like "Monday 9:00-10:00"
 */

public class TimeBlock {
    
    // Attributes
	
	private String id;
    private String day;
    private String startTime;
    private String endTime;
    
    // Constructor
    
    public TimeBlock(String id, String day, String startTime, String endTime) {
        this.id = id;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    // Getters
    
    public String getId() {
        return id;
    }
    
    public String getDay() {
        return day;
    }
    
    public String getStartTime() {
        return startTime;
    }
    
    public String getEndTime() {
        return endTime;
    }
}