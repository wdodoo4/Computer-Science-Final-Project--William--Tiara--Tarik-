package project;

import java.util.ArrayList;

/*
 * Name(s): Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: Assignment links together a course with its teacher, room, time, and students
 */

public class Assignment {
	
	// Attribute
    
    private String id;
    private Course course;
    private Teacher teacher;
    private Resource resource;
    private TimeBlock timeBlock;
    private ArrayList<Student> students;
    
    // Constructor
    
    public Assignment(String id, Course course, Teacher teacher, Room room, TimeBlock timeBlock) {
        this.id = id;
        this.course = course;
        this.teacher = teacher;
        this.resource = resource;
        this.timeBlock = timeBlock;
        this.students = new ArrayList<Student>();
    }
    
    // Getters
    
    public String getId() {
        return id;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public Teacher getTeacher() {
        return teacher;
    }
    
    public Resource getResource() {  // Changed from getRoom
        return resource;   
    }
    
    // Helper method to get Room if resource is a Room
    public Room getRoom() {
        if (resource instanceof Room) {
            return (Room) resource;
        }
        return null;
    }
    
    public TimeBlock getTimeBlock() {
        return timeBlock;
    }
    
    public ArrayList<Student> getStudents() {
        return students;
    }
    
    // Setters
    
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
   
    public void setResource(Resource resource) {  // Changed from setRoom
        this.resource = resource;
    }
    
    public void setTimeBlock(TimeBlock timeBlock) {
        this.timeBlock = timeBlock;
    }
    public void addStudent(Student student) {
        students.add(student);
    }
    
    public void removeStudent(Student student) {
        students.remove(student);
    }
}