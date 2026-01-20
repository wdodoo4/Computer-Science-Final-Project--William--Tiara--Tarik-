package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.19
 * Description: A lot of UI stuff
 */

public class SchedulerApp extends JFrame {

    private ScheduleController controller;

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
        // Create the controller (handles all logic)
        controller = new ScheduleController();

        // Initialize UI state
        selectedResources = new ArrayList<>();
        editingAssignment = null;

        // Load demo data for testing
        controller.loadDemoData();  // Comment out for final submission

        // Setup the window
        setTitle("Scheduler");
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

    // Top panel - add students, teachers, courses, resources
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Add Resources"));

        panel.add(createStudentPanel());
        panel.add(createTeacherPanel());
        panel.add(createCoursePanel());
        panel.add(createResourcePanel());

        return panel;
    }

    private JPanel createStudentPanel() {
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
                
                String error = controller.addStudent(id, name);
                
                if (error != null) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, error);
                } else {
                    studentIdField.setText("");
                    studentNameField.setText("");
                    refreshAll();
                }
            }
        });

        return studentPanel;
    }

    private JPanel createTeacherPanel() {
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

    private JPanel createCoursePanel() {
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

    private JPanel createResourcePanel() {
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
                handleSelectResources();
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
                handleCreateAssignment(createBtn, cancelEditBtn);
            }
        });

        // Cancel edit button action
        cancelEditBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleCancelEdit(createBtn, cancelEditBtn);
            }
        });

        // Edit assignment button action
        editAssignmentBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleEditAssignment(createBtn, cancelEditBtn);
            }
        });

        // Edit students button action
        editStudentsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleEditStudents();
            }
        });

        // Remove assignment button action
        removeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleRemoveAssignment();
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
                handleSave();
            }
        });

        // Load button action
        loadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLoad();
            }
        });

        return panel;
    }

    // Handler methods 

    private void handleSelectResources() {
        if (controller.getResources().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No resources available");
            return;
        }

        JPanel resourcePanel = new JPanel();
        resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.Y_AXIS));
        ArrayList<JCheckBox> checkboxes = new ArrayList<>();

        for (Resource r : controller.getResources()) {
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
        int result = JOptionPane.showConfirmDialog(this, scrollPane, 
            "Select Resources", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            selectedResources.clear();
            
            for (int i = 0; i < checkboxes.size(); i++) {
                if (checkboxes.get(i).isSelected()) {
                    selectedResources.add(controller.getResources().get(i));
                }
            }
            
            // Update button text to show count
            if (selectedResources.isEmpty()) {
                selectResourcesBtn.setText("Select Resources");
            } else {
                selectResourcesBtn.setText(selectedResources.size() + " Resource(s) Selected");
            }
        }
    }

    private void handleCreateAssignment(JButton createBtn, JButton cancelEditBtn) {
        String id = assignIdField.getText().trim();
        String courseStr = (String) courseCombo.getSelectedItem();
        String teacherStr = (String) teacherCombo.getSelectedItem();
        String timeStr = (String) timeCombo.getSelectedItem();

        // Extract IDs from combo box selections
        String courseCode = null;
        String teacherId = null;
        String timeId = null;

        if (courseStr != null) {
            courseCode = courseStr.split(" - ")[0];
        }
        if (teacherStr != null) {
            teacherId = teacherStr.split(" - ")[0];
        }
        if (timeStr != null) {
            timeId = timeStr.split(" \\(")[0];
        }

        // Call controller to create assignment
        String error = controller.createAssignment(id, courseCode, teacherId, timeId, selectedResources, editingAssignment);

        if (error != null) {
            JOptionPane.showMessageDialog(this, error);
        } else {
            assignIdField.setText("");
            selectedResources.clear();
            selectResourcesBtn.setText("Select Resources");
            editingAssignment = null;
            createBtn.setText("Create Assignment");
            cancelEditBtn.setVisible(false);
            refreshAll();
        }
    }

    private void handleCancelEdit(JButton createBtn, JButton cancelEditBtn) {
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

    private void handleEditAssignment(JButton createBtn, JButton cancelEditBtn) {
        if (assignmentListModel.getSize() == 0) {
            JOptionPane.showMessageDialog(this, "No assignments to edit");
            return;
        }

        JList<String> assignList = new JList<>(assignmentListModel);
        assignList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignList.setSelectedIndex(0);
        
        JScrollPane scrollPane = new JScrollPane(assignList);
        int result = JOptionPane.showConfirmDialog(this, scrollPane, 
            "Select Assignment to Edit", JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION || assignList.getSelectedValue() == null) {
            return;
        }

        String selected = assignList.getSelectedValue();
        String assignId = selected.split(":")[0].trim();
        Assignment assignment = controller.findAssignment(assignId);
        
        if (assignment == null) {
            return;
        }

        // Load assignment data into form
        editingAssignment = assignment;
        assignIdField.setText(assignment.getId());
        
        // Set course combo
        for (int i = 0; i < courseCombo.getItemCount(); i++) {
            if (courseCombo.getItemAt(i).startsWith(assignment.getCourse().getCourseCode() + " - ")) {
                courseCombo.setSelectedIndex(i);
                break;
            }
        }
        
        // Set teacher combo
        for (int i = 0; i < teacherCombo.getItemCount(); i++) {
            if (teacherCombo.getItemAt(i).startsWith(assignment.getTeacher().getTeacherId() + " - ")) {
                teacherCombo.setSelectedIndex(i);
                break;
            }
        }
        
        // Set time combo
        for (int i = 0; i < timeCombo.getItemCount(); i++) {
            if (timeCombo.getItemAt(i).startsWith(assignment.getTimeBlock().getId() + " ")) {
                timeCombo.setSelectedIndex(i);
                break;
            }
        }
        
        // Set resources
        selectedResources.clear();
        for (Resource r : assignment.getResources()) {
            selectedResources.add(r);
        }
        selectResourcesBtn.setText(selectedResources.size() + " Resource(s) Selected");
        
        // Update UI to show we're editing
        createBtn.setText("Update Assignment");
        cancelEditBtn.setVisible(true);
    }

    private void handleEditStudents() {
        if (assignmentListModel.getSize() == 0) {
            JOptionPane.showMessageDialog(this, "No assignments to edit");
            return;
        }

        JList<String> assignList = new JList<>(assignmentListModel);
        assignList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignList.setSelectedIndex(0);
        
        JScrollPane scrollPane = new JScrollPane(assignList);
        int result = JOptionPane.showConfirmDialog(this, scrollPane, 
            "Select Assignment to Edit Students", JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION || assignList.getSelectedValue() == null) {
            return;
        }

        String selected = assignList.getSelectedValue();
        String assignId = selected.split(":")[0].trim();
        Assignment assignment = controller.findAssignment(assignId);
        
        if (assignment == null) {
            return;
        }

        // Show checkboxes for students
        JPanel studentPanel = new JPanel();
        studentPanel.setLayout(new BoxLayout(studentPanel, BoxLayout.Y_AXIS));
        ArrayList<JCheckBox> checkboxes = new ArrayList<>();

        for (Student s : controller.getStudents()) {
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
        result = JOptionPane.showConfirmDialog(this, studentScrollPane, 
            "Select Students for " + assignId, JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            ArrayList<Student> selectedStudents = new ArrayList<>();
            
            for (int i = 0; i < checkboxes.size(); i++) {
                if (checkboxes.get(i).isSelected()) {
                    selectedStudents.add(controller.getStudents().get(i));
                }
            }
            
            controller.updateAssignmentStudents(assignment, selectedStudents);
            refreshAll();
        }
    }

    private void handleRemoveAssignment() {
        if (assignmentListModel.getSize() == 0) {
            JOptionPane.showMessageDialog(this, "No assignments to remove");
            return;
        }

        JList<String> assignList = new JList<>(assignmentListModel);
        assignList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignList.setSelectedIndex(0);
        
        JScrollPane scrollPane = new JScrollPane(assignList);
        int result = JOptionPane.showConfirmDialog(this, scrollPane, 
            "Select Assignment to Remove", JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION || assignList.getSelectedValue() == null) {
            return;
        }

        String selected = assignList.getSelectedValue();
        String assignId = selected.split(":")[0].trim();
        Assignment assignment = controller.findAssignment(assignId);
        
        controller.removeAssignment(assignment);
        refreshAll();
    }

    private void handleSave() {
        String filename = JOptionPane.showInputDialog("Enter filename (no extension):");
        
        if (filename == null || filename.isEmpty()) {
            return;
        }
        
        controller.save(filename);
        JOptionPane.showMessageDialog(this, "Saved!");
    }

    private void handleLoad() {
        String filename = JOptionPane.showInputDialog("Enter filename (no extension):");
        
        if (filename == null || filename.isEmpty()) {
            return;
        }
        
        controller.load(filename);
        refreshAll();
        JOptionPane.showMessageDialog(this, "Loaded!");
    }

    // Refresh all displays
    private void refreshAll() {
        // Refresh resource lists
        studentListModel.clear();
        for (Student s : controller.getStudents()) {
            studentListModel.addElement(s.getStudentId() + " - " + s.getFullName());
        }

        teacherListModel.clear();
        for (Teacher t : controller.getTeachers()) {
            teacherListModel.addElement(t.getTeacherId() + " - " + t.getFullName());
        }

        courseListModel.clear();
        for (Course c : controller.getCourses()) {
            courseListModel.addElement(c.getCourseCode() + " - " + c.getTitle());
        }

        resourceListModel.clear();
        for (Resource r : controller.getResources()) {
            resourceListModel.addElement(r.getId() + " - " + r.getName());
        }

        // Refresh assignment list
        assignmentListModel.clear();
        for (Assignment a : controller.getAssignments()) {
            String resourcesStr = "";
            for (int i = 0; i < a.getResources().size(); i++) {
                if (i > 0) resourcesStr += ", ";
                resourcesStr += a.getResources().get(i).getId();
            }
            
            String line = a.getId() + ": " + a.getCourse().getCourseCode() +
                         " | " + a.getTeacher().getFullName() +
                         " | " + resourcesStr +
                         " | " + a.getTimeBlock().getId() +
                         " | Students: " + a.getStudents().size();
            
            assignmentListModel.addElement(line);
        }

        // Refresh dropdowns
        courseCombo.removeAllItems();
        for (Course c : controller.getCourses()) {
            courseCombo.addItem(c.getCourseCode() + " - " + c.getTitle());
        }

        teacherCombo.removeAllItems();
        for (Teacher t : controller.getTeachers()) {
            teacherCombo.addItem(t.getTeacherId() + " - " + t.getFullName());
        }

        timeCombo.removeAllItems();
        for (TimeBlock tb : controller.getTimeBlocks()) {
            String itemText = tb.getId() + " (" + tb.getDay() + " " + tb.getStartTime() + "-" + tb.getEndTime() + ")";
            timeCombo.addItem(itemText);
        }

        // Refresh conflicts
        ArrayList<String> conflicts = controller.getConflicts();
        
        if (conflicts.isEmpty()) {
            conflictArea.setText("No conflicts!");
        } else {
            String text = "";
            for (String c : conflicts) {
                text += c + "\n\n";
            }
            conflictArea.setText(text);
        }
    }

    public static void main(String[] args) {
        new SchedulerApp();
    }
}