package project;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: Student class
 */

public class Student {
    
    // changed from public to private
	
    private String studentId;
    private String fullName;
    
    public Student(String studentId, String fullName) {
        this.studentId = studentId;
        this.fullName = fullName;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public String getFullName() {
        return fullName;
    }
}