package project;

import java.util.ArrayList;

/*
 * Name(s): Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: Assignment links together a course with its teacher, rooms/resources, time, and students
 *
 */

public class Assignment {
    
    private String id;
    private Course course;
    private Teacher teacher;
    private ArrayList<Resource> resources;
    private TimeBlock timeBlock;
    private ArrayList<Student> students;
    
    public Assignment(String id, Course course, Teacher teacher, Resource resource, TimeBlock timeBlock) {
        this.id = id;
        this.course = course;
        this.teacher = teacher;
        this.resources = new ArrayList<Resource>();
        this.resources.add(resource);
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
    
    // Return the first resource for places that expect a single resource
    public Resource getResource() {
        return resources.get(0);   
    }
    
    // Return all resources attached to this assignment
    public ArrayList<Resource> getResources() {
        return resources;
    }
    
    // Helper method to get Room if one of the resources is a Room (returns the first Room found)
    
    public Room getRoom() {
        for (Resource r : resources) {
            if (r instanceof Room) {
                return (Room) r;
            }
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
   
    // Replace primary resource (keeps it as the first resource)
    public void setResource(Resource resource) {
        if (resource == null) return;
        if (resources.isEmpty()) resources.add(resource);
        else resources.set(0, resource);
    }
    
    // Add / remove additional resources
    public void addResource(Resource resource) {
        if (resource == null) return;
        for (Resource r : resources) {
            if (r.getId().equals(resource.getId())) return;
        }
        resources.add(resource);
    }
    
    public void removeResource(Resource resource) {
        if (resource == null) return;
        for (int i = resources.size() - 1; i >= 0; i--) {
            if (resources.get(i).getId().equals(resource.getId())) {
                resources.remove(i);
            }
        }
    }
    
    public void setTimeBlock(TimeBlock timeBlock) {
        this.timeBlock = timeBlock;
    }
    public void addStudent(Student student) {
        for (Student s : students) {
            if (s.getStudentId().equals(student.getStudentId())) return;
        }
        students.add(student);
    }
    
    public void removeStudent(Student student) {
        for (int i = students.size() - 1; i >= 0; i--) {
            if (students.get(i).getStudentId().equals(student.getStudentId())) {
                students.remove(i);
            }
        }
    }
}