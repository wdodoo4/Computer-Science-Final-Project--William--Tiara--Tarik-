package project;

import java.util.ArrayList;

/* 
 * Name: Tiara, William, Tarik
 * Date: 2026.01.12
 * Description: Schedule holds all our school resources and class assignments
 */

public class Schedule {
    
    // Attributes
	
    private String name;
    private ArrayList<Assignment> assignments;
    private ArrayList<Student> students;
    private ArrayList<Teacher> teachers;
    private ArrayList<Room> rooms;
    private ArrayList<Course> courses;
    private ArrayList<TimeBlock> timeBlocks;
    
    // start with an empty schedule
    public Schedule(String name) {
        this.name = name;
        this.assignments = new ArrayList<Assignment>();
        this.students = new ArrayList<Student>();
        this.teachers = new ArrayList<Teacher>();
        this.rooms = new ArrayList<Room>();
        this.courses = new ArrayList<Course>();
        this.timeBlocks = new ArrayList<TimeBlock>();
    }
    
    // Getters
    
    public String getName() {
        return name;
    }
    
    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }
    
    public ArrayList<Student> getStudents() {
        return students;
    }
    
    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }
    
    public ArrayList<Room> getRooms() {
        return rooms;
    }
    
    public ArrayList<Course> getCourses() {
        return courses;
    }
    
    public ArrayList<TimeBlock> getTimeBlocks() {
        return timeBlocks;
    }
    
    // Setters
    
    public void addStudent(Student student) {
        students.add(student);
    }
    
    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }
    
    public void addRoom(Room room) {
        rooms.add(room);
    }
    
    public void addCourse(Course course) {
        courses.add(course);
    }
    
    public void addTimeBlock(TimeBlock timeBlock) {
        timeBlocks.add(timeBlock);
    }
    
    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }
    
    // remove stuff from the schedule
    public void removeStudent(Student student) {
        students.remove(student);
        // also take them out of any classes they're in
        for (Assignment a : assignments) {
            a.removeStudent(student);
        }
    }
    
    public void removeTeacher(Teacher teacher) {
        teachers.remove(teacher);
        // remove any assignments this teacher had
        for (int i = assignments.size() - 1; i >= 0; i--) {
            if (assignments.get(i).getTeacher().equals(teacher)) {
                assignments.remove(i);
            }
        }
    }
    
    public void removeRoom(Room room) {
        rooms.remove(room);
        // remove any assignments in this room
        for (int i = assignments.size() - 1; i >= 0; i--) {
            if (assignments.get(i).getRoom().equals(room)) {
                assignments.remove(i);
            }
        }
    }
    
    public void removeCourse(Course course) {
        courses.remove(course);
        // remove assignments for this course
        for (int i = assignments.size() - 1; i >= 0; i--) {
            if (assignments.get(i).getCourse().equals(course)) {
                assignments.remove(i);
            }
        }
    }
    
    public void removeAssignment(Assignment assignment) {
        assignments.remove(assignment);
    }
}