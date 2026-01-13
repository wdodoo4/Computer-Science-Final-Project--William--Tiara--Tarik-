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
    private Room room;
    private TimeBlock timeBlock;
    private ArrayList<Student> students;
    
    // Constructor
    
    public Assignment(String id, Course course, Teacher teacher, Room room, TimeBlock timeBlock) {
        this.id = id;
        this.course = course;
        this.teacher = teacher;
        this.room = room;
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
    
    public Room getRoom() {
        return room;
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
    
    public void setRoom(Room room) {
        this.room = room;
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