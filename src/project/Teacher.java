package project;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: Teacher class
 */

public class Teacher {
	
    private String teacherId;
    private String fullName;
    
    public Teacher(String teacherId, String fullName) {
        this.teacherId = teacherId;
        this.fullName = fullName;
    }
    
    public String getTeacherId() {
        return teacherId;
    }
    
    public String getFullName() {
        return fullName;
    }
}