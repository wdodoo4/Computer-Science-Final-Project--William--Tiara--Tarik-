package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.15
 * A lot of UI stuff and logic.
 */

public class SchedulerApp extends JFrame {

    private Schedule schedule;
    private PersistenceManager persistence;
    private ScheduleValidator validator;

    // Lists for displaying data
    private DefaultListModel<String> studentListModel;
    private DefaultListModel<String> teacherListModel;
    private DefaultListModel<String> courseListModel;
    private DefaultListModel<String> resourceListModel;
    private DefaultListModel<String> assignmentListModel;

    // Dropdowns for creating assignments
    private JComboBox<String> courseCombo;
    private JComboBox<String> teacherCombo;
    private JComboBox<String> timeCombo;

    // Conflict display area
    private JTextArea conflictArea;
    
    // For editing assignments
    private JTextField assignIdField;
    private JButton selectResourcesBtn;
    private ArrayList<Resource> selectedResources;
    private Assignment editingAssignment;

    public SchedulerApp() {
        // Create the schedule and helpers
        schedule = new Schedule("Schedule");
        persistence = new PersistenceManager();
        validator = new ScheduleValidator();

        // Initialize selected resources list
        selectedResources = new ArrayList<>();
        editingAssignment = null;

        // Add some demo data to start
        
        addDemoData();  //Comment out for final submittion

        // Setup the window
        setTitle("School Scheduler");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Create the UI
        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createRightPanel(), BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Refresh everything to show demo data
        refreshAll();
    }

    // Add some demo data so it's not empty
    private void addDemoData() {
        schedule.addStudent(new Student("S001", "Alice Brown"));
        schedule.addStudent(new Student("S002", "Bob Chen"));

        schedule.addTeacher(new Teacher("T001", "Mr. Smith"));
        schedule.addTeacher(new Teacher("T002", "Ms. Jones"));

        schedule.addCourse(new Course("ENG3U", "Grade 11 English"));
        schedule.addCourse(new Course("MCR3U", "Grade 11 Math"));

        schedule.addResource(new Room("R1", "201"));
        schedule.addResource(new ComputerLab("CL1", "301"));
    }

    // Top panel - add students, teachers, courses, resources
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Add Resources"));

        // Students section
        JPanel studentPanel = new JPanel(new BorderLayout(5, 5));
        JPanel studentForm = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField studentIdField = new JTextField(8);
        JTextField studentNameField = new JTextField(12);
        JButton addStudentBtn = new JButton("Add Student");

        studentForm.add(new JLabel("ID:"));
        studentForm.add(studentIdField);
        studentForm.add(new JLabel("Name:"));
        studentForm.add(studentNameField);
        studentForm.add(new JLabel(""));
        studentForm.add(addStudentBtn);

        studentListModel = new DefaultListModel<>();
        JList<String> studentList = new JList<>(studentListModel);
        JScrollPane studentScroll = new JScrollPane(studentList);
        studentScroll.setPreferredSize(new Dimension(200, 100));

        studentPanel.add(studentForm, BorderLayout.NORTH);
        studentPanel.add(studentScroll, BorderLayout.CENTER);

        // Add student button action
        addStudentBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = studentIdField.getText().trim();
                String name = studentNameField.getText().trim();
                
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Enter ID and name");
                    return;
                }
                
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Enter ID and name");
                    return;
                }
                
                Student newStudent = new Student(id, name);
                schedule.addStudent(newStudent);
                studentIdField.setText("");
                studentNameField.setText("");
                refreshAll();
            }
        });

        // Teachers section
        JPanel teacherPanel = new JPanel(new BorderLayout(5, 5));
        JPanel teacherForm = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField teacherIdField = new JTextField(8);
        JTextField teacherNameField = new JTextField(12);
        JButton addTeacherBtn = new JButton("Add Teacher");

        teacherForm.add(new JLabel("ID:"));
        teacherForm.add(teacherIdField);
        teacherForm.add(new JLabel("Name:"));
        teacherForm.add(teacherNameField);
        teacherForm.add(new JLabel(""));
        teacherForm.add(addTeacherBtn);

        teacherListModel = new DefaultListModel<>();
        JList<String> teacherList = new JList<>(teacherListModel);
        JScrollPane teacherScroll = new JScrollPane(teacherList);
        teacherScroll.setPreferredSize(new Dimension(200, 100));

        teacherPanel.add(teacherForm, BorderLayout.NORTH);
        teacherPanel.add(teacherScroll, BorderLayout.CENTER);

        // Add teacher button action
        addTeacherBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = teacherIdField.getText().trim();
                String name = teacherNameField.getText().trim();
                
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Enter ID and name");
                    return;
                }
                
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Enter ID and name");
                    return;
                }
                
                Teacher newTeacher = new Teacher(id, name);
                schedule.addTeacher(newTeacher);
                teacherIdField.setText("");
                teacherNameField.setText("");
                refreshAll();
            }
        });

        // Courses section
        JPanel coursePanel = new JPanel(new BorderLayout(5, 5));
        JPanel courseForm = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField courseCodeField = new JTextField(8);
        JTextField courseTitleField = new JTextField(12);
        JButton addCourseBtn = new JButton("Add Course");

        courseForm.add(new JLabel("Code:"));
        courseForm.add(courseCodeField);
        courseForm.add(new JLabel("Title:"));
        courseForm.add(courseTitleField);
        courseForm.add(new JLabel(""));
        courseForm.add(addCourseBtn);

        courseListModel = new DefaultListModel<>();
        JList<String> courseList = new JList<>(courseListModel);
        JScrollPane courseScroll = new JScrollPane(courseList);
        courseScroll.setPreferredSize(new Dimension(200, 100));

        coursePanel.add(courseForm, BorderLayout.NORTH);
        coursePanel.add(courseScroll, BorderLayout.CENTER);

        // Add course button action
        addCourseBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String code = courseCodeField.getText().trim();
                String title = courseTitleField.getText().trim();
                
                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Enter code and title");
                    return;
                }
                
                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Enter code and title");
                    return;
                }
                
                Course newCourse = new Course(code, title);
                schedule.addCourse(newCourse);
                courseCodeField.setText("");
                courseTitleField.setText("");
                refreshAll();
            }
        });

        // Resources section
        JPanel resourcePanel = new JPanel(new BorderLayout(5, 5));
        JPanel resourceForm = new JPanel(new GridLayout(4, 2, 5, 5));
        String[] resourceTypes = {"Room", "ComputerLab", "ScienceLab", "Gym", "Equipment"};
        JComboBox<String> resourceTypeCombo = new JComboBox<>(resourceTypes);
        JTextField resourceIdField = new JTextField(8);
        JTextField resourceNameField = new JTextField(12);
        JButton addResourceBtn = new JButton("Add Resource");

        resourceForm.add(new JLabel("Type:"));
        resourceForm.add(resourceTypeCombo);
        resourceForm.add(new JLabel("ID:"));
        resourceForm.add(resourceIdField);
        resourceForm.add(new JLabel("Name/Number:"));
        resourceForm.add(resourceNameField);
        resourceForm.add(new JLabel(""));
        resourceForm.add(addResourceBtn);

        resourceListModel = new DefaultListModel<>();
        JList<String> resourceList = new JList<>(resourceListModel);
        JScrollPane resourceScroll = new JScrollPane(resourceList);
        resourceScroll.setPreferredSize(new Dimension(200, 100));

        resourcePanel.add(resourceForm, BorderLayout.NORTH);
        resourcePanel.add(resourceScroll, BorderLayout.CENTER);

        // Add resource button action
        addResourceBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String type = (String) resourceTypeCombo.getSelectedItem();
                String id = resourceIdField.getText().trim();
                String name = resourceNameField.getText().trim();
                
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Enter ID and name");
                    return;
                }
                
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Enter ID and name");
                    return;
                }

                Resource r;
                
                if (type.equals("ComputerLab")) {
                    r = new ComputerLab(id, name);
                } 
                else if (type.equals("ScienceLab")) {
                    r = new ScienceLab(id, name);
                } 
                else if (type.equals("Gym")) {
                    r = new Gym(id, name);
                } 
                else if (type.equals("Equipment")) {
                    r = new Equipment(id, name);
                } 
                else {
                    r = new Room(id, name);
                }

                schedule.addResource(r);
                resourceIdField.setText("");
                resourceNameField.setText("");
                refreshAll();
            }
        });

        panel.add(studentPanel);
        panel.add(teacherPanel);
        panel.add(coursePanel);
        panel.add(resourcePanel);

        return panel;
    }

    // Center panel - create assignments
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Create Assignment"));

        // Form for creating assignments
        JPanel form = new JPanel(new GridLayout(7, 2, 5, 5));
        assignIdField = new JTextField(10);
        courseCombo = new JComboBox<>();
        teacherCombo = new JComboBox<>();
        timeCombo = new JComboBox<>();
        selectResourcesBtn = new JButton("Select Resources");
        JButton createBtn = new JButton("Create Assignment");
        JButton cancelEditBtn = new JButton("Cancel Edit");
        cancelEditBtn.setVisible(false);

        form.add(new JLabel("Assignment ID:"));
        form.add(assignIdField);
        form.add(new JLabel("Course:"));
        form.add(courseCombo);
        form.add(new JLabel("Teacher:"));
        form.add(teacherCombo);
        form.add(new JLabel("Time:"));
        form.add(timeCombo);
        form.add(new JLabel("Resources:"));
        form.add(selectResourcesBtn);
        form.add(cancelEditBtn);
        form.add(createBtn);

        panel.add(form, BorderLayout.NORTH);

        // Select resources button action
        selectResourcesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (schedule.getResources().isEmpty()) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "No resources available");
                    return;
                }

                JPanel resourcePanel = new JPanel();
                resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.Y_AXIS));
                ArrayList<JCheckBox> checkboxes = new ArrayList<>();

                for (Resource r : schedule.getResources()) {
                    String checkboxText = r.getId() + " - " + r.getName();
                    JCheckBox cb = new JCheckBox(checkboxText);
                    
                    // Check if resource is already selected
                    for (Resource selected : selectedResources) {
                        if (selected.getId().equals(r.getId())) {
                            cb.setSelected(true);
                            break;
                        }
                    }
                    
                    checkboxes.add(cb);
                    resourcePanel.add(cb);
                }

                JScrollPane scrollPane = new JScrollPane(resourcePanel);
                int result = JOptionPane.showConfirmDialog(SchedulerApp.this, scrollPane, 
                    "Select Resources", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    selectedResources.clear();
                    
                    for (int i = 0; i < checkboxes.size(); i++) {
                        if (checkboxes.get(i).isSelected()) {
                            selectedResources.add(schedule.getResources().get(i));
                        }
                    }
                    
                    // Update button text to show count
                    if (selectedResources.isEmpty()) {
                        selectResourcesBtn.setText("Select Resources");
                    } 
                    else {
                        String buttonText = selectedResources.size() + " Resource(s) Selected";
                        selectResourcesBtn.setText(buttonText);
                    }
                }
            }
        });

        // Assignments list
        assignmentListModel = new DefaultListModel<>();
        JList<String> assignmentList = new JList<>(assignmentListModel);
        JScrollPane assignScroll = new JScrollPane(assignmentList);
        panel.add(assignScroll, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout());
        JButton editStudentsBtn = new JButton("Edit Students");
        JButton editAssignmentBtn = new JButton("Edit Assignment");
        JButton removeBtn = new JButton("Remove Assignment");
        buttons.add(editStudentsBtn);
        buttons.add(editAssignmentBtn);
        buttons.add(removeBtn);
        panel.add(buttons, BorderLayout.SOUTH);

        // Create assignment button action
        createBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = assignIdField.getText().trim();
                String courseStr = (String) courseCombo.getSelectedItem();
                String teacherStr = (String) teacherCombo.getSelectedItem();
                String timeStr = (String) timeCombo.getSelectedItem();

                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Fill all fields");
                    return;
                }
                
                if (courseStr == null) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Fill all fields");
                    return;
                }
                
                if (teacherStr == null) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Fill all fields");
                    return;
                }
                
                if (timeStr == null) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Fill all fields");
                    return;
                }

                if (selectedResources.isEmpty()) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Select at least one resource");
                    return;
                }

                // Find the actual objects
                String[] courseParts = courseStr.split(" - ");
                String courseCode = courseParts[0];
                Course course = findCourse(courseCode);
                
                String[] teacherParts = teacherStr.split(" - ");
                String teacherId = teacherParts[0];
                Teacher teacher = findTeacher(teacherId);
                
                TimeBlock time = findTimeBlock(timeStr);

                if (course == null) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Error finding selected items");
                    return;
                }
                
                if (teacher == null) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Error finding selected items");
                    return;
                }
                
                if (time == null) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Error finding selected items");
                    return;
                }

                if (editingAssignment != null) {
                    // We're editing an existing assignment
                    schedule.removeAssignment(editingAssignment);
                }

                Assignment a = new Assignment(id, course, teacher, selectedResources.get(0), time);
                
                // Add additional resources if more than one selected
                for (int i = 1; i < selectedResources.size(); i++) {
                    a.addResource(selectedResources.get(i));
                }
                
                schedule.addAssignment(a);
                assignIdField.setText("");
                selectedResources.clear();
                selectResourcesBtn.setText("Select Resources");
                editingAssignment = null;
                createBtn.setText("Create Assignment");
                cancelEditBtn.setVisible(false);
                refreshAll();
            }
        });

        // Cancel edit button action
        cancelEditBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                assignIdField.setText("");
                selectedResources.clear();
                selectResourcesBtn.setText("Select Resources");
                editingAssignment = null;
                createBtn.setText("Create Assignment");
                cancelEditBtn.setVisible(false);
                
                // Reset dropdowns to first item
                if (courseCombo.getItemCount() > 0) {
                    courseCombo.setSelectedIndex(0);
                }
                
                if (teacherCombo.getItemCount() > 0) {
                    teacherCombo.setSelectedIndex(0);
                }
                
                if (timeCombo.getItemCount() > 0) {
                    timeCombo.setSelectedIndex(0);
                }
            }
        });

        // Edit assignment button action
        editAssignmentBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (assignmentListModel.getSize() == 0) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "No assignments to edit");
                    return;
                }

                // Create a JList for selection
                JList<String> assignList = new JList<>(assignmentListModel);
                assignList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                assignList.setSelectedIndex(0);
                
                JScrollPane scrollPane = new JScrollPane(assignList);
                int result = JOptionPane.showConfirmDialog(SchedulerApp.this, scrollPane, 
                    "Select Assignment to Edit", JOptionPane.OK_CANCEL_OPTION);

                if (result != JOptionPane.OK_OPTION) {
                    return;
                }
                
                if (assignList.getSelectedValue() == null) {
                    return;
                }

                String selected = assignList.getSelectedValue();
                String[] parts = selected.split(":");
                String assignId = parts[0].trim();
                Assignment assignment = findAssignment(assignId);
                
                if (assignment == null) {
                    return;
                }

                // Load assignment data into form
                editingAssignment = assignment;
                assignIdField.setText(assignment.getId());
                
                // Set course combo
                for (int i = 0; i < courseCombo.getItemCount(); i++) {
                    String item = courseCombo.getItemAt(i);
                    if (item.startsWith(assignment.getCourse().getCourseCode() + " - ")) {
                        courseCombo.setSelectedIndex(i);
                        break;
                    }
                }
                
                // Set teacher combo
                for (int i = 0; i < teacherCombo.getItemCount(); i++) {
                    String item = teacherCombo.getItemAt(i);
                    if (item.startsWith(assignment.getTeacher().getTeacherId() + " - ")) {
                        teacherCombo.setSelectedIndex(i);
                        break;
                    }
                }
                
                // Set time combo
                for (int i = 0; i < timeCombo.getItemCount(); i++) {
                    String item = timeCombo.getItemAt(i);
                    if (item.startsWith(assignment.getTimeBlock().getId() + " ")) {
                        timeCombo.setSelectedIndex(i);
                        break;
                    }
                }
                
                // Set resources
                selectedResources.clear();
                for (Resource r : assignment.getResources()) {
                    selectedResources.add(r);
                }
                
                String buttonText = selectedResources.size() + " Resource(s) Selected";
                selectResourcesBtn.setText(buttonText);
                
                // Update UI to show we're editing
                createBtn.setText("Update Assignment");
                cancelEditBtn.setVisible(true);
            }
        });

        // Edit students button action
        editStudentsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (assignmentListModel.getSize() == 0) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "No assignments to edit");
                    return;
                }

                // Create a JList for selection
                JList<String> assignList = new JList<>(assignmentListModel);
                assignList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                assignList.setSelectedIndex(0);
                
                JScrollPane scrollPane = new JScrollPane(assignList);
                int result = JOptionPane.showConfirmDialog(SchedulerApp.this, scrollPane, 
                    "Select Assignment to Edit Students", JOptionPane.OK_CANCEL_OPTION);

                if (result != JOptionPane.OK_OPTION) {
                    return;
                }
                
                if (assignList.getSelectedValue() == null) {
                    return;
                }

                String selected = assignList.getSelectedValue();
                String[] parts = selected.split(":");
                String assignId = parts[0].trim();
                Assignment assignment = findAssignment(assignId);
                
                if (assignment == null) {
                    return;
                }

                // Show checkboxes for students
                JPanel studentPanel = new JPanel();
                studentPanel.setLayout(new BoxLayout(studentPanel, BoxLayout.Y_AXIS));
                ArrayList<JCheckBox> checkboxes = new ArrayList<>();

                for (Student s : schedule.getStudents()) {
                    String checkboxText = s.getStudentId() + " - " + s.getFullName();
                    JCheckBox cb = new JCheckBox(checkboxText);
                    
                    // Check if student is already enrolled
                    for (Student enrolled : assignment.getStudents()) {
                        if (enrolled.getStudentId().equals(s.getStudentId())) {
                            cb.setSelected(true);
                            break;
                        }
                    }
                    
                    checkboxes.add(cb);
                    studentPanel.add(cb);
                }

                JScrollPane studentScrollPane = new JScrollPane(studentPanel);
                String dialogTitle = "Select Students for " + assignId;
                result = JOptionPane.showConfirmDialog(SchedulerApp.this, studentScrollPane, 
                    dialogTitle, JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    assignment.getStudents().clear();
                    
                    for (int i = 0; i < checkboxes.size(); i++) {
                        if (checkboxes.get(i).isSelected()) {
                            Student student = schedule.getStudents().get(i);
                            assignment.addStudent(student);
                        }
                    }
                    
                    refreshAll();
                }
            }
        });

        // Remove assignment button action
        removeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (assignmentListModel.getSize() == 0) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "No assignments to remove");
                    return;
                }

                JList<String> assignList = new JList<>(assignmentListModel);
                assignList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                assignList.setSelectedIndex(0);
                
                JScrollPane scrollPane = new JScrollPane(assignList);
                int result = JOptionPane.showConfirmDialog(SchedulerApp.this, scrollPane, 
                    "Select Assignment to Remove", JOptionPane.OK_CANCEL_OPTION);

                if (result != JOptionPane.OK_OPTION) {
                    return;
                }
                
                if (assignList.getSelectedValue() == null) {
                    return;
                }

                String selected = assignList.getSelectedValue();
                String[] parts = selected.split(":");
                String assignId = parts[0].trim();
                Assignment assignment = findAssignment(assignId);
                
                if (assignment != null) {
                    schedule.removeAssignment(assignment);
                    refreshAll();
                }
            }
        });

        return panel;
    }

    // Right panel - conflicts and save/load
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Conflicts"));

        conflictArea = new JTextArea(20, 30);
        conflictArea.setEditable(false);
        conflictArea.setLineWrap(true);
        conflictArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(conflictArea);
        panel.add(scroll, BorderLayout.CENTER);

        // Save and load buttons
        JPanel buttons = new JPanel(new FlowLayout());
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");
        buttons.add(saveBtn);
        buttons.add(loadBtn);
        panel.add(buttons, BorderLayout.SOUTH);

        // Save button action
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String filename = JOptionPane.showInputDialog("Enter filename (no extension):");
                
                if (filename == null) {
                    return;
                }
                
                if (filename.isEmpty()) {
                    return;
                }
                
                String fullFilename = filename + ".csv";
                persistence.save(schedule, fullFilename);
                JOptionPane.showMessageDialog(SchedulerApp.this, "Saved!");
            }
        });

        // Load button action
        loadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String filename = JOptionPane.showInputDialog("Enter filename (no extension):");
                
                if (filename == null) {
                    return;
                }
                
                if (filename.isEmpty()) {
                    return;
                }
                
                String fullFilename = filename + ".csv";
                Schedule loaded = persistence.load(fullFilename);
                schedule = loaded;
                refreshAll();
                JOptionPane.showMessageDialog(SchedulerApp.this, "Loaded!");
            }
        });

        return panel;
    }

    // Helper methods to find objects
    private void refreshAll() {
        // Refresh resource lists
        studentListModel.clear();
        for (Student s : schedule.getStudents()) {
            String studentText = s.getStudentId() + " - " + s.getFullName();
            studentListModel.addElement(studentText);
        }

        teacherListModel.clear();
        for (Teacher t : schedule.getTeachers()) {
            String teacherText = t.getTeacherId() + " - " + t.getFullName();
            teacherListModel.addElement(teacherText);
        }

        courseListModel.clear();
        for (Course c : schedule.getCourses()) {
            String courseText = c.getCourseCode() + " - " + c.getTitle();
            courseListModel.addElement(courseText);
        }

        resourceListModel.clear();
        for (Resource r : schedule.getResources()) {
            String resourceText = r.getId() + " - " + r.getName();
            resourceListModel.addElement(resourceText);
        }

        // Refresh assignment list
        assignmentListModel.clear();
        for (Assignment a : schedule.getAssignments()) {
            String resourcesStr = "";
            for (int i = 0; i < a.getResources().size(); i++) {
                if (i > 0) {
                    resourcesStr = resourcesStr + ", ";
                }
                resourcesStr = resourcesStr + a.getResources().get(i).getId();
            }
            
            String line = a.getId() + ": " + a.getCourse().getCourseCode();
            line = line + " | " + a.getTeacher().getFullName();
            line = line + " | " + resourcesStr;
            line = line + " | " + a.getTimeBlock().getId();
            line = line + " | Students: " + a.getStudents().size();
            
            assignmentListModel.addElement(line);
        }

        // Refresh dropdowns
        courseCombo.removeAllItems();
        for (Course c : schedule.getCourses()) {
            String itemText = c.getCourseCode() + " - " + c.getTitle();
            courseCombo.addItem(itemText);
        }

        teacherCombo.removeAllItems();
        for (Teacher t : schedule.getTeachers()) {
            String itemText = t.getTeacherId() + " - " + t.getFullName();
            teacherCombo.addItem(itemText);
        }

        timeCombo.removeAllItems();
        for (TimeBlock tb : schedule.getTimeBlocks()) {
            String itemText = tb.getId() + " (" + tb.getDay();
            itemText = itemText + " " + tb.getStartTime();
            itemText = itemText + "-" + tb.getEndTime() + ")";
            timeCombo.addItem(itemText);
        }

        // Refresh conflicts
        ArrayList<String> conflicts = validator.checkConflicts(schedule);
        
        if (conflicts.isEmpty()) {
            conflictArea.setText("No conflicts!");
        } 
        else {
            String text = "";
            
            for (String c : conflicts) {
                text = text + c;
                text = text + "\n\n";
            }
            
            conflictArea.setText(text);
        }
    }

    // Helper methods to find objects
    private Course findCourse(String code) {
        for (Course c : schedule.getCourses()) {
            if (c.getCourseCode().equals(code)) {
                return c;
            }
        }
        
        return new Course("UNKNOWN", "Unknown Course");
    }

    private Teacher findTeacher(String id) {
        for (Teacher t : schedule.getTeachers()) {
            if (t.getTeacherId().equals(id)) {
                return t;
            }
        }
        
        return new Teacher("UNKNOWN", "Unknown Teacher");
    }

    private Resource findResource(String id) {
        for (Resource r : schedule.getResources()) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        
        return new Room("UNKNOWN", "Unknown Room");
    }

    private TimeBlock findTimeBlock(String display) {
        // Extract ID from display like "P1-Monday (Monday 8:50-10:05)"
        String[] parts = display.split(" \\(");
        String id = parts[0];
        
        for (TimeBlock tb : schedule.getTimeBlocks()) {
            if (tb.getId().equals(id)) {
                return tb;
            }
        }
        
        return new TimeBlock("UNKNOWN", "Monday", "00:00", "00:00");
    }

    private Assignment findAssignment(String id) {
        for (Assignment a : schedule.getAssignments()) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        
        return null;
    }

    public static void main(String[] args) {
        new SchedulerApp();
    }
}