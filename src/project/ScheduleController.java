package project;

import java.util.ArrayList;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.19
 * Description: Handles all the logic and validation. App calls these methods.
 */

public class ScheduleController {
    
    // Main schedule that holds all the data
    private Schedule schedule;
    
    // Handles saving and loading files
    private PersistenceManager persistence;
    
    // Checks for conflicts like double-booked teachers
    private ScheduleValidator validator;
    
    public ScheduleController() {
        // Make a new empty schedule
        schedule = new Schedule("Schedule");
        
        // Create the helper objects
        persistence = new PersistenceManager();
        validator = new ScheduleValidator();
    }
    
    // These getters let the UI read data without changing it
    
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
    
    // Try to add a student. Returns error message or null if ok.
    public String addStudent(String id, String name) {
        // Check if ID and name are valid
        String error = validateIdAndName(id, name);
        if (error != null) {
            return error;
        }
        
        // Make the student and add it
        Student newStudent = new Student(id, name);
        schedule.addStudent(newStudent);
        return null;
    }
    
    // Try to add a teacher. Returns error message or null if ok.
    public String addTeacher(String id, String name) {
        // Check if ID and name are valid
        String error = validateIdAndName(id, name);
        if (error != null) {
            return error;
        }
        
        // Make the teacher and add it
        Teacher newTeacher = new Teacher(id, name);
        schedule.addTeacher(newTeacher);
        return null;
    }
    
    // Try to add a course. Returns error message or null if ok.
    public String addCourse(String code, String title) {
        // Check if code and title are valid
        String error = validateCodeAndTitle(code, title);
        if (error != null) {
            return error;
        }
        
        // Make the course and add it
        Course newCourse = new Course(code, title);
        schedule.addCourse(newCourse);
        return null;
    }
    
    // Try to add a resource. Returns error message or null if ok.
    public String addResource(String type, String id, String name) {
        // Check if ID and name are valid
        String error = validateIdAndName(id, name);
        if (error != null) {
            return error;
        }
        
        // Make the right type of resource based on what they picked
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
            // Default to regular room
            r = new Room(id, name);
        }
        
        // Add it to the schedule
        schedule.addResource(r);
        return null;
    }
    
    // Try to create an assignment. Returns error message or null if ok.
    public String createAssignment(String id, String courseCode, String teacherId, 
                                   String timeId, ArrayList<Resource> resources, 
                                   Assignment editingAssignment) {
        
        // Check if assignment ID is filled in
        if (id == null || id.trim().isEmpty()) {
            return "Assignment ID cannot be empty";
        }
        
        // Check if they picked a course
        if (courseCode == null) {
            return "Please select a course";
        }
        
        // Check if they picked a teacher
        if (teacherId == null) {
            return "Please select a teacher";
        }
        
        // Check if they picked a time
        if (timeId == null) {
            return "Please select a time";
        }
        
        // Check if they picked at least one resource
        if (resources == null || resources.isEmpty()) {
            return "Please select at least one resource";
        }
        
        // Look up the actual course object
        Course course = findCourse(courseCode);
        
        // Look up the actual teacher object
        Teacher teacher = findTeacher(teacherId);
        
        // Look up the actual time block object
        TimeBlock time = findTimeBlock(timeId);
        
        // Make sure we found everything
        if (course == null || teacher == null || time == null) {
            return "Error finding selected items";
        }
        
        // If we're editing an existing assignment, delete the old one first
        if (editingAssignment != null) {
            schedule.removeAssignment(editingAssignment);
        }
        
        // Create new assignment with first resource
        Assignment a = new Assignment(id, course, teacher, resources.get(0), time);
        
        // Add any extra resources they picked
        for (int i = 1; i < resources.size(); i++) {
            a.addResource(resources.get(i));
        }
        
        // Add assignment to schedule
        schedule.addAssignment(a);
        return null;
    }
    
    // Delete an assignment
    public void removeAssignment(Assignment assignment) {
        if (assignment != null) {
            schedule.removeAssignment(assignment);
        }
    }
    
    // Change which students are in an assignment
    public void updateAssignmentStudents(Assignment assignment, ArrayList<Student> selectedStudents) {
        if (assignment == null) {
            return;
        }
        
        // Clear current students
        assignment.getStudents().clear();
        
        // Add the new list
        for (Student s : selectedStudents) {
            assignment.addStudent(s);
        }
    }
    
    // Get list of all conflicts
    public ArrayList<String> getConflicts() {
        return validator.checkConflicts(schedule);
    }
    
    // Save schedule to a CSV file
    public void save(String filename) {
        // Add .csv to the filename
        String fullFilename = filename + ".csv";
        
        // Tell persistence manager to save it
        persistence.save(schedule, fullFilename);
    }
    
    // Load schedule from a CSV file
    public void load(String filename) {
        // Add .csv to the filename
        String fullFilename = filename + ".csv";
        
        // Load the file
        Schedule loaded = persistence.load(fullFilename);
        
        // Replace current schedule with loaded one
        schedule = loaded;
    }
    
    // Find a course by its code
    public Course findCourse(String code) {
        // Go through all courses
        for (Course c : schedule.getCourses()) {
            if (c.getCourseCode().equals(code)) {
                return c;
            }
        }
        // Not found
        return null;
    }
    
    // Find a teacher by their ID
    public Teacher findTeacher(String id) {
        // Go through all teachers
        for (Teacher t : schedule.getTeachers()) {
            if (t.getTeacherId().equals(id)) {
                return t;
            }
        }
        // Not found
        return null;
    }
    
    // Find a resource by its ID
    public Resource findResource(String id) {
        // Go through all resources
        for (Resource r : schedule.getResources()) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        // Not found
        return null;
    }
    
    // Find a time block by its ID
    public TimeBlock findTimeBlock(String id) {
        // Go through all time blocks
        for (TimeBlock tb : schedule.getTimeBlocks()) {
            if (tb.getId().equals(id)) {
                return tb;
            }
        }
        // Not found
        return null;
    }
    
    // Find an assignment by its ID
    public Assignment findAssignment(String id) {
        // Go through all assignments
        for (Assignment a : schedule.getAssignments()) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        // Not found
        return null;
    }
    
    // Check if ID and name are both filled in
    private String validateIdAndName(String id, String name) {
        // Check ID
        if (id == null || id.trim().isEmpty()) {
            return "ID cannot be empty";
        }
        
        // Check name
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be empty";
        }
        
        // Both good
        return null;
    }
    
    // Check if code and title are both filled in
    private String validateCodeAndTitle(String code, String title) {
        // Check code
        if (code == null || code.trim().isEmpty()) {
            return "Code cannot be empty";
        }
        
        // Check title
        if (title == null || title.trim().isEmpty()) {
            return "Title cannot be empty";
        }
        
        // Both good
        return null;
    }
    
    // Add some test data so the app isn't empty when it starts
    public void loadDemoData() {
        // Add two students
        schedule.addStudent(new Student("S001", "Alice Brown"));
        schedule.addStudent(new Student("S002", "Bob Chen"));
        
        // Add two teachers
        schedule.addTeacher(new Teacher("T001", "Mr. Smith"));
        schedule.addTeacher(new Teacher("T002", "Ms. Jones"));
        
        // Add two courses
        schedule.addCourse(new Course("ENG3U", "Grade 11 English"));
        schedule.addCourse(new Course("MCR3U", "Grade 11 Math"));
        
        // Add a room and a computer lab
        schedule.addResource(new Room("R1", "201"));
        schedule.addResource(new ComputerLab("CL1", "301"));
    }
}