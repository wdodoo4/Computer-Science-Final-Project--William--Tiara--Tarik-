package project;

import java.util.ArrayList;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.19
 * Description: Controller, handles all logic and validation. Keeps logic separate from the GUI.
 */

public class ScheduleController {
    
    private Schedule schedule;
    private PersistenceManager persistence;
    private ScheduleValidator validator;
    
    public ScheduleController() {
        schedule = new Schedule("Schedule");
        persistence = new PersistenceManager();
        validator = new ScheduleValidator();
    }
    
    // Getters for schedule data (read-only access for UI)
    
    public Schedule getSchedule() {
        return schedule;
    }
    
    public ArrayList<Student> getStudents() {
        return schedule.getStudents();
    }
    
    public ArrayList<Teacher> getTeachers() {
        return schedule.getTeachers();
    }
    
    public ArrayList<Course> getCourses() {
        return schedule.getCourses();
    }
    
    public ArrayList<Resource> getResources() {
        return schedule.getResources();
    }
    
    public ArrayList<Assignment> getAssignments() {
        return schedule.getAssignments();
    }
    
    public ArrayList<TimeBlock> getTimeBlocks() {
        return schedule.getTimeBlocks();
    }
    
    // Student operations
    
    public String addStudent(String id, String name) {
        String error = validateIdAndName(id, name);
        if (error != null) {
            return error;
        }
        
        Student newStudent = new Student(id, name);
        schedule.addStudent(newStudent);
        return null; // success
    }
    
    // Teacher operations
    
    public String addTeacher(String id, String name) {
        String error = validateIdAndName(id, name);
        if (error != null) {
            return error;
        }
        
        Teacher newTeacher = new Teacher(id, name);
        schedule.addTeacher(newTeacher);
        return null; // success
    }
    
    // Course operations
    
    public String addCourse(String code, String title) {
        String error = validateCodeAndTitle(code, title);
        if (error != null) {
            return error;
        }
        
        Course newCourse = new Course(code, title);
        schedule.addCourse(newCourse);
        return null; // success
    }
    
    // Resource operations
    
    public String addResource(String type, String id, String name) {
        String error = validateIdAndName(id, name);
        if (error != null) {
            return error;
        }
        
        Resource r;
        if (type.equals("ComputerLab")) {
            r = new ComputerLab(id, name);
        } else if (type.equals("ScienceLab")) {
            r = new ScienceLab(id, name);
        } else if (type.equals("Gym")) {
            r = new Gym(id, name);
        } else if (type.equals("Equipment")) {
            r = new Equipment(id, name);
        } else {
            r = new Room(id, name);
        }
        
        schedule.addResource(r);
        return null; 
    }
    
    // Assignment operations
    
    public String createAssignment(String id, String courseCode, String teacherId, 
                                   String timeId, ArrayList<Resource> resources,
                                   Assignment editingAssignment) {
        
        // Validate inputs
        if (id == null || id.trim().isEmpty()) {
            return "Assignment ID cannot be empty";
        }
        if (courseCode == null) {
            return "Please select a course";
        }
        if (teacherId == null) {
            return "Please select a teacher";
        }
        if (timeId == null) {
            return "Please select a time";
        }
        if (resources == null || resources.isEmpty()) {
            return "Please select at least one resource";
        }
        
        // Find the actual objects
        Course course = findCourse(courseCode);
        Teacher teacher = findTeacher(teacherId);
        TimeBlock time = findTimeBlock(timeId);
        
        if (course == null || teacher == null || time == null) {
            return "Error finding selected items";
        }
        
        // If editing, remove old assignment first
        if (editingAssignment != null) {
            schedule.removeAssignment(editingAssignment);
        }
        
        // Create new assignment
        Assignment a = new Assignment(id, course, teacher, resources.get(0), time);
        
        // Add additional resources if more than one selected
        for (int i = 1; i < resources.size(); i++) {
            a.addResource(resources.get(i));
        }
        
        schedule.addAssignment(a);
        return null; // success
    }
    
    public void removeAssignment(Assignment assignment) {
        if (assignment != null) {
            schedule.removeAssignment(assignment);
        }
    }
    
    public void updateAssignmentStudents(Assignment assignment, ArrayList<Student> selectedStudents) {
        if (assignment == null) {
            return;
        }
        
        assignment.getStudents().clear();
        for (Student s : selectedStudents) {
            assignment.addStudent(s);
        }
    }
    
    // Conflict checking
    
    public ArrayList<String> getConflicts() {
        return validator.checkConflicts(schedule);
    }
    
    // Save and Load
    
    public void save(String filename) {
        String fullFilename = filename + ".csv";
        persistence.save(schedule, fullFilename);
    }
    
    public void load(String filename) {
        String fullFilename = filename + ".csv";
        Schedule loaded = persistence.load(fullFilename);
        schedule = loaded;
    }
    
    // Helper methods to find objects
    
    public Course findCourse(String code) {
        for (Course c : schedule.getCourses()) {
            if (c.getCourseCode().equals(code)) {
                return c;
            }
        }
        return null;
    }
    
    public Teacher findTeacher(String id) {
        for (Teacher t : schedule.getTeachers()) {
            if (t.getTeacherId().equals(id)) {
                return t;
            }
        }
        return null;
    }
    
    public Resource findResource(String id) {
        for (Resource r : schedule.getResources()) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }
    
    public TimeBlock findTimeBlock(String id) {
        for (TimeBlock tb : schedule.getTimeBlocks()) {
            if (tb.getId().equals(id)) {
                return tb;
            }
        }
        return null;
    }
    
    public Assignment findAssignment(String id) {
        for (Assignment a : schedule.getAssignments()) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }
    
    // Validation methods (private - internal use only)
    
    private String validateIdAndName(String id, String name) {
        if (id == null || id.trim().isEmpty()) {
            return "ID cannot be empty";
        }
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be empty";
        }
        return null;
    }
    
    private String validateCodeAndTitle(String code, String title) {
        if (code == null || code.trim().isEmpty()) {
            return "Code cannot be empty";
        }
        if (title == null || title.trim().isEmpty()) {
            return "Title cannot be empty";
        }
        return null;
    }
    
    // Demo data (for testing - comment out for final submission)
    
    public void loadDemoData() {
        schedule.addStudent(new Student("S001", "Alice Brown"));
        schedule.addStudent(new Student("S002", "Bob Chen"));
        
        schedule.addTeacher(new Teacher("T001", "Mr. Smith"));
        schedule.addTeacher(new Teacher("T002", "Ms. Jones"));
        
        schedule.addCourse(new Course("ENG3U", "Grade 11 English"));
        schedule.addCourse(new Course("MCR3U", "Grade 11 Math"));
        
        schedule.addResource(new Room("R1", "201"));
        schedule.addResource(new ComputerLab("CL1", "301"));
    }
}