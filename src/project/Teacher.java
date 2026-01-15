package project;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: Teacher class
 */

class Teacher {
    
    // changed from public to private
	
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