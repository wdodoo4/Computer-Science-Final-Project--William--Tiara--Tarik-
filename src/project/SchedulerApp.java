package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.19
 * Description: GUI for the scheduler. Shows all the forms, buttons, and lists. All the actual logic is in ScheduleController.
 */

public class SchedulerApp extends JFrame {

    // Controller handles all the logic
    private ScheduleController controller;

    // List models hold the data that appears in JLists
    private DefaultListModel<String> studentListModel;
    private DefaultListModel<String> teacherListModel;
    private DefaultListModel<String> courseListModel;
    private DefaultListModel<String> resourceListModel;
    private DefaultListModel<String> assignmentListModel;

    // Dropdown boxes for creating assignments
    private JComboBox<String> courseCombo;
    private JComboBox<String> teacherCombo;
    private JComboBox<String> timeCombo;

    // Text area that shows conflicts
    private JTextArea conflictArea;
    
    // Fields for assignment creation
    private JTextField assignIdField;
    private JButton selectResourcesBtn;
    
    // Keep track of which resources user picked
    private ArrayList<Resource> selectedResources;
    
    // If editing an assignment, store it here
    private Assignment editingAssignment;

    public SchedulerApp() {
        // Make the controller that does all the work
        controller = new ScheduleController();

        // Start with empty lists
        selectedResources = new ArrayList<>();
        editingAssignment = null;

        // Load some test data so app isn't empty
        // controller.loadDemoData();  // Comment out for final submission

        // Set up the main window
        setTitle("Scheduler");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Build the three main sections
        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createRightPanel(), BorderLayout.EAST);

        // Size everything and show it
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Fill in all the lists with current data
        refreshAll();
    }

    // Top panel has four sections: students, teachers, courses, resources
    private JPanel createTopPanel() {
        // Make a panel with 4 columns
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Add Resources"));

        // Add each section
        panel.add(createStudentPanel());
        panel.add(createTeacherPanel());
        panel.add(createCoursePanel());
        panel.add(createResourcePanel());

        return panel;
    }

    // Student section: form to add students and list to show them
    private JPanel createStudentPanel() {
        JPanel studentPanel = new JPanel(new BorderLayout(5, 5));
        
        // Form with ID and name fields
        JPanel studentForm = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField studentIdField = new JTextField(8);
        JTextField studentNameField = new JTextField(12);
        JButton addStudentBtn = new JButton("Add Student");

        // Put labels and fields in the form
        studentForm.add(new JLabel("ID:"));
        studentForm.add(studentIdField);
        studentForm.add(new JLabel("Name:"));
        studentForm.add(studentNameField);
        studentForm.add(new JLabel(""));
        studentForm.add(addStudentBtn);

        // List to show all students
        studentListModel = new DefaultListModel<>();
        JList<String> studentList = new JList<>(studentListModel);
        JScrollPane studentScroll = new JScrollPane(studentList);
        studentScroll.setPreferredSize(new Dimension(200, 100));

        studentPanel.add(studentForm, BorderLayout.NORTH);
        studentPanel.add(studentScroll, BorderLayout.CENTER);

        // When button is clicked, try to add the student
        addStudentBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get what user typed
                String id = studentIdField.getText().trim();
                String name = studentNameField.getText().trim();
                
                // Ask controller to add it
                String error = controller.addStudent(id, name);
                
                // If there was an error, show it
                if (error != null) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, error);
                } else {
                    // Success, clear the fields and refresh
                    studentIdField.setText("");
                    studentNameField.setText("");
                    refreshAll();
                }
            }
        });

        return studentPanel;
    }

    // Teacher section: same idea as students
    private JPanel createTeacherPanel() {
        JPanel teacherPanel = new JPanel(new BorderLayout(5, 5));
        
        // Form for adding teachers
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

        // List showing all teachers
        teacherListModel = new DefaultListModel<>();
        JList<String> teacherList = new JList<>(teacherListModel);
        JScrollPane teacherScroll = new JScrollPane(teacherList);
        teacherScroll.setPreferredSize(new Dimension(200, 100));

        teacherPanel.add(teacherForm, BorderLayout.NORTH);
        teacherPanel.add(teacherScroll, BorderLayout.CENTER);

        // Add teacher when button clicked
        addTeacherBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = teacherIdField.getText().trim();
                String name = teacherNameField.getText().trim();
                
                // Controller validates and adds
                String error = controller.addTeacher(id, name);
                
                if (error != null) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, error);
                } else {
                    teacherIdField.setText("");
                    teacherNameField.setText("");
                    refreshAll();
                }
            }
        });

        return teacherPanel;
    }

    // Course section: code and title
    private JPanel createCoursePanel() {
        JPanel coursePanel = new JPanel(new BorderLayout(5, 5));
        
        // Form for course code and title
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

        // List of all courses
        courseListModel = new DefaultListModel<>();
        JList<String> courseList = new JList<>(courseListModel);
        JScrollPane courseScroll = new JScrollPane(courseList);
        courseScroll.setPreferredSize(new Dimension(200, 100));

        coursePanel.add(courseForm, BorderLayout.NORTH);
        coursePanel.add(courseScroll, BorderLayout.CENTER);

        // Add course when clicked
        addCourseBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String code = courseCodeField.getText().trim();
                String title = courseTitleField.getText().trim();
                
                String error = controller.addCourse(code, title);
                
                if (error != null) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, error);
                } else {
                    courseCodeField.setText("");
                    courseTitleField.setText("");
                    refreshAll();
                }
            }
        });

        return coursePanel;
    }

    // Resource section: rooms, labs, gyms, equipment
    private JPanel createResourcePanel() {
        JPanel resourcePanel = new JPanel(new BorderLayout(5, 5));
        
        // Form with type dropdown, ID, and name
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

        // List showing all resources
        resourceListModel = new DefaultListModel<>();
        JList<String> resourceList = new JList<>(resourceListModel);
        JScrollPane resourceScroll = new JScrollPane(resourceList);
        resourceScroll.setPreferredSize(new Dimension(200, 100));

        resourcePanel.add(resourceForm, BorderLayout.NORTH);
        resourcePanel.add(resourceScroll, BorderLayout.CENTER);

        // Add resource when clicked
        addResourceBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get type, ID, and name
                String type = (String) resourceTypeCombo.getSelectedItem();
                String id = resourceIdField.getText().trim();
                String name = resourceNameField.getText().trim();
                
                // Controller creates the right type and adds it
                String error = controller.addResource(type, id, name);
                
                if (error != null) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, error);
                } else {
                    resourceIdField.setText("");
                    resourceNameField.setText("");
                    refreshAll();
                }
            }
        });

        return resourcePanel;
    }

    // Center panel is for creating and managing assignments
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Create Assignment"));

        // Form to create an assignment
        JPanel form = new JPanel(new GridLayout(7, 2, 5, 5));
        assignIdField = new JTextField(10);
        courseCombo = new JComboBox<>();
        teacherCombo = new JComboBox<>();
        timeCombo = new JComboBox<>();
        selectResourcesBtn = new JButton("Select Resources");
        JButton createBtn = new JButton("Create Assignment");
        JButton cancelEditBtn = new JButton("Cancel Edit");
        cancelEditBtn.setVisible(false);

        // Add all fields to form
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

        // Button to pick resources opens a dialog
        selectResourcesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSelectResources();
            }
        });

        // List showing all assignments
        assignmentListModel = new DefaultListModel<>();
        JList<String> assignmentList = new JList<>(assignmentListModel);
        JScrollPane assignScroll = new JScrollPane(assignmentList);
        panel.add(assignScroll, BorderLayout.CENTER);

        // Bottom buttons for editing
        JPanel buttons = new JPanel(new FlowLayout());
        JButton editStudentsBtn = new JButton("Edit Students");
        JButton editAssignmentBtn = new JButton("Edit Assignment");
        JButton removeBtn = new JButton("Remove Assignment");
        buttons.add(editStudentsBtn);
        buttons.add(editAssignmentBtn);
        buttons.add(removeBtn);
        panel.add(buttons, BorderLayout.SOUTH);

        // Create button tries to make the assignment
        createBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleCreateAssignment(createBtn, cancelEditBtn);
            }
        });

        // Cancel button clears the form
        cancelEditBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleCancelEdit(createBtn, cancelEditBtn);
            }
        });

        // Edit button loads an assignment into the form
        editAssignmentBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleEditAssignment(createBtn, cancelEditBtn);
            }
        });

        // Edit students button lets you pick which students are in a class
        editStudentsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleEditStudents();
            }
        });

        // Remove button deletes an assignment
        removeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleRemoveAssignment();
            }
        });

        return panel;
    }

    // Right panel shows conflicts and has save/load buttons
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Conflicts"));

        // Text area that lists all conflicts
        conflictArea = new JTextArea(20, 30);
        conflictArea.setEditable(false);
        conflictArea.setLineWrap(true);
        conflictArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(conflictArea);
        panel.add(scroll, BorderLayout.CENTER);

        // Save and load buttons at bottom
        JPanel buttons = new JPanel(new FlowLayout());
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");
        buttons.add(saveBtn);
        buttons.add(loadBtn);
        panel.add(buttons, BorderLayout.SOUTH);

        // Save asks for filename then saves
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSave();
            }
        });

        // Load asks for filename then loads
        loadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLoad();
            }
        });

        return panel;
    }

    // Show dialog with checkboxes for each resource
    private void handleSelectResources() {
        // Make sure there are resources to pick from
        if (controller.getResources().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No resources available");
            return;
        }

        // Make a panel with checkboxes
        JPanel resourcePanel = new JPanel();
        resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.Y_AXIS));
        ArrayList<JCheckBox> checkboxes = new ArrayList<>();

        // Create checkbox for each resource
        for (Resource r : controller.getResources()) {
            String checkboxText = r.getId() + " - " + r.getName();
            JCheckBox cb = new JCheckBox(checkboxText);
            
            // If resource was already picked, check the box
            for (Resource selected : selectedResources) {
                if (selected.getId().equals(r.getId())) {
                    cb.setSelected(true);
                    break;
                }
            }
            
            checkboxes.add(cb);
            resourcePanel.add(cb);
        }

        // Show dialog with checkboxes
        JScrollPane scrollPane = new JScrollPane(resourcePanel);
        int result = JOptionPane.showConfirmDialog(this, scrollPane, 
            "Select Resources", JOptionPane.OK_CANCEL_OPTION);

        // If user clicked OK, save their choices
        if (result == JOptionPane.OK_OPTION) {
            selectedResources.clear();
            
            // Go through each checkbox
            for (int i = 0; i < checkboxes.size(); i++) {
                if (checkboxes.get(i).isSelected()) {
                    // Add the resource they checked
                    selectedResources.add(controller.getResources().get(i));
                }
            }
            
            // Update button to show how many picked
            if (selectedResources.isEmpty()) {
                selectResourcesBtn.setText("Select Resources");
            } else {
                selectResourcesBtn.setText(selectedResources.size() + " Resource(s) Selected");
            }
        }
    }

    // Try to create an assignment with the form data
    private void handleCreateAssignment(JButton createBtn, JButton cancelEditBtn) {
        // Get everything from the form
        String id = assignIdField.getText().trim();
        String courseStr = (String) courseCombo.getSelectedItem();
        String teacherStr = (String) teacherCombo.getSelectedItem();
        String timeStr = (String) timeCombo.getSelectedItem();

        // Pull out just the IDs from the dropdown text
        String courseCode = null;
        String teacherId = null;
        String timeId = null;

        // Dropdown shows "ENG3U - Grade 11 English", we want "ENG3U"
        if (courseStr != null) {
            courseCode = courseStr.split(" - ")[0];
        }
        // Dropdown shows "T001 - Mr. Smith", we want "T001"
        if (teacherStr != null) {
            teacherId = teacherStr.split(" - ")[0];
        }
        // Dropdown shows "P1-Monday (Monday 8:50-10:05)", we want "P1-Monday"
        if (timeStr != null) {
            timeId = timeStr.split(" \\(")[0];
        }

        // Ask controller to create it
        String error = controller.createAssignment(id, courseCode, teacherId, 
                                                   timeId, selectedResources, editingAssignment);

        // Show error or reset form
        if (error != null) {
            JOptionPane.showMessageDialog(this, error);
        } else {
            // Success, clear everything
            assignIdField.setText("");
            selectedResources.clear();
            selectResourcesBtn.setText("Select Resources");
            editingAssignment = null;
            createBtn.setText("Create Assignment");
            cancelEditBtn.setVisible(false);
            refreshAll();
        }
    }

    // Clear the assignment form
    private void handleCancelEdit(JButton createBtn, JButton cancelEditBtn) {
        assignIdField.setText("");
        selectedResources.clear();
        selectResourcesBtn.setText("Select Resources");
        editingAssignment = null;
        createBtn.setText("Create Assignment");
        cancelEditBtn.setVisible(false);
        
        // Reset all dropdowns to first item
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

    // Load an assignment into the form for editing
    private void handleEditAssignment(JButton createBtn, JButton cancelEditBtn) {
        // Check if there are any assignments
        if (assignmentListModel.getSize() == 0) {
            JOptionPane.showMessageDialog(this, "No assignments to edit");
            return;
        }

        // Show list of assignments to pick from
        JList<String> assignList = new JList<>(assignmentListModel);
        assignList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignList.setSelectedIndex(0);
        
        JScrollPane scrollPane = new JScrollPane(assignList);
        int result = JOptionPane.showConfirmDialog(this, scrollPane, 
            "Select Assignment to Edit", JOptionPane.OK_CANCEL_OPTION);

        // If they cancelled or didn't pick anything, stop
        if (result != JOptionPane.OK_OPTION || assignList.getSelectedValue() == null) {
            return;
        }

        // Get the assignment ID from the list text
        String selected = assignList.getSelectedValue();
        String assignId = selected.split(":")[0].trim();
        Assignment assignment = controller.findAssignment(assignId);
        
        if (assignment == null) {
            return;
        }

        // Load assignment data into form fields
        editingAssignment = assignment;
        assignIdField.setText(assignment.getId());
        
        // Find and select the right course in dropdown
        for (int i = 0; i < courseCombo.getItemCount(); i++) {
            if (courseCombo.getItemAt(i).startsWith(assignment.getCourse().getCourseCode() + " - ")) {
                courseCombo.setSelectedIndex(i);
                break;
            }
        }
        
        // Find and select the right teacher in dropdown
        for (int i = 0; i < teacherCombo.getItemCount(); i++) {
            if (teacherCombo.getItemAt(i).startsWith(assignment.getTeacher().getTeacherId() + " - ")) {
                teacherCombo.setSelectedIndex(i);
                break;
            }
        }
        
        // Find and select the right time in dropdown
        for (int i = 0; i < timeCombo.getItemCount(); i++) {
            if (timeCombo.getItemAt(i).startsWith(assignment.getTimeBlock().getId() + " ")) {
                timeCombo.setSelectedIndex(i);
                break;
            }
        }
        
        // Load the resources
        selectedResources.clear();
        for (Resource r : assignment.getResources()) {
            selectedResources.add(r);
        }
        selectResourcesBtn.setText(selectedResources.size() + " Resource(s) Selected");
        
        // Change button text to show we're editing
        createBtn.setText("Update Assignment");
        cancelEditBtn.setVisible(true);
    }

    // Pick which students are in an assignment
    private void handleEditStudents() {
        // Check if there are assignments
        if (assignmentListModel.getSize() == 0) {
            JOptionPane.showMessageDialog(this, "No assignments to edit");
            return;
        }

        // Show list to pick which assignment
        JList<String> assignList = new JList<>(assignmentListModel);
        assignList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignList.setSelectedIndex(0);
        
        JScrollPane scrollPane = new JScrollPane(assignList);
        int result = JOptionPane.showConfirmDialog(this, scrollPane, 
            "Select Assignment to Edit Students", JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION || assignList.getSelectedValue() == null) {
            return;
        }

        // Get the assignment
        String selected = assignList.getSelectedValue();
        String assignId = selected.split(":")[0].trim();
        Assignment assignment = controller.findAssignment(assignId);
        
        if (assignment == null) {
            return;
        }

        // Make checkboxes for each student
        JPanel studentPanel = new JPanel();
        studentPanel.setLayout(new BoxLayout(studentPanel, BoxLayout.Y_AXIS));
        ArrayList<JCheckBox> checkboxes = new ArrayList<>();

        for (Student s : controller.getStudents()) {
            String checkboxText = s.getStudentId() + " - " + s.getFullName();
            JCheckBox cb = new JCheckBox(checkboxText);
            
            // Check the box if student is already in this assignment
            for (Student enrolled : assignment.getStudents()) {
                if (enrolled.getStudentId().equals(s.getStudentId())) {
                    cb.setSelected(true);
                    break;
                }
            }
            
            checkboxes.add(cb);
            studentPanel.add(cb);
        }

        // Show dialog with student checkboxes
        JScrollPane studentScrollPane = new JScrollPane(studentPanel);
        result = JOptionPane.showConfirmDialog(this, studentScrollPane, 
            "Select Students for " + assignId, JOptionPane.OK_CANCEL_OPTION);

        // If they clicked OK, update the assignment
        if (result == JOptionPane.OK_OPTION) {
            ArrayList<Student> selectedStudents = new ArrayList<>();
            
            // Go through checkboxes and collect selected students
            for (int i = 0; i < checkboxes.size(); i++) {
                if (checkboxes.get(i).isSelected()) {
                    selectedStudents.add(controller.getStudents().get(i));
                }
            }
            
            // Tell controller to update the assignment
            controller.updateAssignmentStudents(assignment, selectedStudents);
            refreshAll();
        }
    }

    // Delete an assignment
    private void handleRemoveAssignment() {
        // Check if there are any
        if (assignmentListModel.getSize() == 0) {
            JOptionPane.showMessageDialog(this, "No assignments to remove");
            return;
        }

        // Show list to pick which one
        JList<String> assignList = new JList<>(assignmentListModel);
        assignList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignList.setSelectedIndex(0);
        
        JScrollPane scrollPane = new JScrollPane(assignList);
        int result = JOptionPane.showConfirmDialog(this, scrollPane, 
            "Select Assignment to Remove", JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION || assignList.getSelectedValue() == null) {
            return;
        }

        // Get the assignment and delete it
        String selected = assignList.getSelectedValue();
        String assignId = selected.split(":")[0].trim();
        Assignment assignment = controller.findAssignment(assignId);
        
        controller.removeAssignment(assignment);
        refreshAll();
    }

    // Save schedule to a file
    private void handleSave() {
        // Ask user for filename
        String filename = JOptionPane.showInputDialog("Enter filename (no extension):");
        
        // If they cancelled or didn't type anything, stop
        if (filename == null || filename.isEmpty()) {
            return;
        }
        
        // Tell controller to save
        controller.save(filename);
        JOptionPane.showMessageDialog(this, "Saved!");
    }

    // Load schedule from a file
    private void handleLoad() {
        // Ask user for filename
        String filename = JOptionPane.showInputDialog("Enter filename (no extension):");
        
        // If they cancelled or didn't type anything, stop
        if (filename == null || filename.isEmpty()) {
            return;
        }
        
        // Tell controller to load
        controller.load(filename);
        refreshAll();
        JOptionPane.showMessageDialog(this, "Loaded!");
    }

    // Update all the lists and dropdowns with current data
    private void refreshAll() {
        // Clear and refill student list
        studentListModel.clear();
        for (Student s : controller.getStudents()) {
            studentListModel.addElement(s.getStudentId() + " - " + s.getFullName());
        }

        // Clear and refill teacher list
        teacherListModel.clear();
        for (Teacher t : controller.getTeachers()) {
            teacherListModel.addElement(t.getTeacherId() + " - " + t.getFullName());
        }

        // Clear and refill course list
        courseListModel.clear();
        for (Course c : controller.getCourses()) {
            courseListModel.addElement(c.getCourseCode() + " - " + c.getTitle());
        }

        // Clear and refill resource list
        resourceListModel.clear();
        for (Resource r : controller.getResources()) {
            resourceListModel.addElement(r.getId() + " - " + r.getName());
        }

        // Clear and refill assignment list
        assignmentListModel.clear();
        for (Assignment a : controller.getAssignments()) {
            // Build string showing all resources in this assignment
            String resourcesStr = "";
            for (int i = 0; i < a.getResources().size(); i++) {
                if (i > 0) {
                    resourcesStr += ", ";
                }
                resourcesStr += a.getResources().get(i).getId();
            }
            
            // Build the display line
            String line = a.getId() + ": " + a.getCourse().getCourseCode();
            line = line + " | " + a.getTeacher().getFullName();
            line = line + " | " + resourcesStr;
            line = line + " | " + a.getTimeBlock().getId();
            line = line + " | Students: " + a.getStudents().size();
            
            assignmentListModel.addElement(line);
        }

        // Refill course dropdown
        courseCombo.removeAllItems();
        for (Course c : controller.getCourses()) {
            courseCombo.addItem(c.getCourseCode() + " - " + c.getTitle());
        }

        // Refill teacher dropdown
        teacherCombo.removeAllItems();
        for (Teacher t : controller.getTeachers()) {
            teacherCombo.addItem(t.getTeacherId() + " - " + t.getFullName());
        }

        // Refill time dropdown
        timeCombo.removeAllItems();
        for (TimeBlock tb : controller.getTimeBlocks()) {
            String itemText = tb.getId() + " (" + tb.getDay() + " " + tb.getStartTime() + "-" + tb.getEndTime() + ")";
            timeCombo.addItem(itemText);
        }

        // Update conflicts display
        ArrayList<String> conflicts = controller.getConflicts();
        
        if (conflicts.isEmpty()) {
            conflictArea.setText("No conflicts!");
        } else {
            // Build text showing all conflicts
            String text = "";
            for (String c : conflicts) {
                text = text + c;
                text = text + "\n\n";
            }
            conflictArea.setText(text);
        }
    }

    public static void main(String[] args) {
        new SchedulerApp();
    }
}