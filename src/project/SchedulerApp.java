package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.15
 * Description: Core UI and logic
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
    private JComboBox<String> resourceCombo;
    private JComboBox<String> timeCombo;

    // Conflict display area
    
    private JTextArea conflictArea;

    public SchedulerApp() {
        // Create the schedule and helpers
        schedule = new Schedule("Fall 2024");
        persistence = new PersistenceManager();
        validator = new ScheduleValidator();

        // Add some demo data to start
        
        addDemoData();

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
        schedule.addStudent(new Student("S01", "Alice Brown"));
        schedule.addStudent(new Student("S02", "Bob Chen"));

        schedule.addTeacher(new Teacher("T01", "Mr. Smith"));
        schedule.addTeacher(new Teacher("T02", "Ms. Jones"));

        schedule.addCourse(new Course("ENG3U", "Grade 11 English"));
        schedule.addCourse(new Course("MCR3U", "Grade 11 Math"));

        schedule.addResource(new Room("R1", "201", 30));
        schedule.addResource(new ComputerLab("CL1", "301", 25));
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
                if (id.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Enter ID and name");
                    return;
                }
                schedule.addStudent(new Student(id, name));
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
                if (id.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Enter ID and name");
                    return;
                }
                schedule.addTeacher(new Teacher(id, name));
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
                if (code.isEmpty() || title.isEmpty()) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Enter code and title");
                    return;
                }
                schedule.addCourse(new Course(code, title));
                courseCodeField.setText("");
                courseTitleField.setText("");
                refreshAll();
            }
        });

        // Resources section
        
        JPanel resourcePanel = new JPanel(new BorderLayout(5, 5));
        JPanel resourceForm = new JPanel(new GridLayout(4, 2, 5, 5));
        JComboBox<String> resourceTypeCombo = new JComboBox<>(new String[]{"Room", "ComputerLab", "ScienceLab", "Gym", "Equipment"});
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
                if (id.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Enter ID and name");
                    return;
                }

                Resource r;
                if (type.equals("ComputerLab")) {
                    r = new ComputerLab(id, name, 25);
                } else if (type.equals("ScienceLab")) {
                    r = new ScienceLab(id, name, 24);
                } else if (type.equals("Gym")) {
                    r = new Gym(id, name, 200);
                } else if (type.equals("Equipment")) {
                    r = new Equipment(id, name, 5);
                } else {
                    r = new Room(id, name, 30);
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
        
        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        JTextField assignIdField = new JTextField(10);
        courseCombo = new JComboBox<>();
        teacherCombo = new JComboBox<>();
        resourceCombo = new JComboBox<>();
        timeCombo = new JComboBox<>();
        JButton createBtn = new JButton("Create Assignment");

        form.add(new JLabel("Assignment ID:"));
        form.add(assignIdField);
        form.add(new JLabel("Course:"));
        form.add(courseCombo);
        form.add(new JLabel("Teacher:"));
        form.add(teacherCombo);
        form.add(new JLabel("Resource:"));
        form.add(resourceCombo);
        form.add(new JLabel("Time:"));
        form.add(timeCombo);
        form.add(new JLabel(""));
        form.add(createBtn);

        panel.add(form, BorderLayout.NORTH);

        // List of assignments
        
        assignmentListModel = new DefaultListModel<>();
        JList<String> assignmentList = new JList<>(assignmentListModel);
        JScrollPane assignScroll = new JScrollPane(assignmentList);
        panel.add(assignScroll, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout());
        JButton editStudentsBtn = new JButton("Edit Students");
        JButton removeBtn = new JButton("Remove Assignment");
        buttons.add(editStudentsBtn);
        buttons.add(removeBtn);
        panel.add(buttons, BorderLayout.SOUTH);

        // Create assignment button action
        
        createBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = assignIdField.getText().trim();
                String courseStr = (String) courseCombo.getSelectedItem();
                String teacherStr = (String) teacherCombo.getSelectedItem();
                String resourceStr = (String) resourceCombo.getSelectedItem();
                String timeStr = (String) timeCombo.getSelectedItem();

                if (id.isEmpty() || courseStr == null || teacherStr == null || resourceStr == null || timeStr == null) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Fill all fields");
                    return;
                }

                // Find the actual objects
                
                Course course = findCourse(courseStr.split(" - ")[0]);
                Teacher teacher = findTeacher(teacherStr.split(" - ")[0]);
                Resource resource = findResource(resourceStr.split(" - ")[0]);
                TimeBlock time = findTimeBlock(timeStr);

                if (course == null || teacher == null || resource == null || time == null) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Error finding selected items");
                    return;
                }

                Assignment a = new Assignment(id, course, teacher, resource, time);
                schedule.addAssignment(a);
                assignIdField.setText("");
                refreshAll();
            }
        });

        // Edit students button action
        
        editStudentsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idx = assignmentList.getSelectedIndex();
                if (idx < 0) {
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Select an assignment first");
                    return;
                }

                String item = assignmentListModel.getElementAt(idx);
                String assignId = item.split(":")[0].trim();
                Assignment assignment = findAssignment(assignId);

                if (assignment == null) return;

                // Show dialog with student checkboxes
                
                JPanel studentPanel = new JPanel();
                studentPanel.setLayout(new BoxLayout(studentPanel, BoxLayout.Y_AXIS));
                ArrayList<JCheckBox> checkboxes = new ArrayList<>();

                for (Student s : schedule.getStudents()) {
                    JCheckBox cb = new JCheckBox(s.getStudentId() + " - " + s.getFullName());
                    
                    // Check if student is already in this assignment
                    
                    for (Student enrolled : assignment.getStudents()) {
                        if (enrolled.getStudentId().equals(s.getStudentId())) {
                            cb.setSelected(true);
                            break;
                        }
                    }
                    
                    checkboxes.add(cb);
                    studentPanel.add(cb);
                }

                int result = JOptionPane.showConfirmDialog(SchedulerApp.this, new JScrollPane(studentPanel), "Select Students", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                	
                    // Clear and re-add students based on checkboxes
                	
                    assignment.getStudents().clear();
                    for (int i = 0; i < checkboxes.size(); i++) {
                        if (checkboxes.get(i).isSelected()) {
                            assignment.addStudent(schedule.getStudents().get(i));
                        }
                    }
                    refreshAll();
                }
            }
        });

        // Remove assignment button action
        
        removeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idx = assignmentList.getSelectedIndex();
                if (idx >= 0) {
                    String item = assignmentListModel.getElementAt(idx);
                    String assignId = item.split(":")[0].trim();
                    Assignment assignment = findAssignment(assignId);
                    if (assignment != null) {
                        schedule.removeAssignment(assignment);
                        refreshAll();
                    }
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
                String filename = JOptionPane.showInputDialog("Enter filename:");
                if (filename != null && !filename.isEmpty()) {
                    persistence.save(schedule, filename + ".csv");
                    JOptionPane.showMessageDialog(SchedulerApp.this, "Saved!");
                }
            }
        });

        // Load button action
        
        loadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String filename = JOptionPane.showInputDialog("Enter filename:");
                if (filename != null && !filename.isEmpty()) {
                    Schedule loaded = persistence.load(filename + ".csv");
                    if (loaded != null) {
                        schedule = loaded;
                        refreshAll();
                        JOptionPane.showMessageDialog(SchedulerApp.this, "Loaded!");
                    }
                }
            }
        });

        return panel;
    }

    // Refresh all the lists and dropdowns
    
    private void refreshAll() {
        // Refresh lists
        studentListModel.clear();
        for (Student s : schedule.getStudents()) {
            studentListModel.addElement(s.getStudentId() + " - " + s.getFullName());
        }

        teacherListModel.clear();
        for (Teacher t : schedule.getTeachers()) {
            teacherListModel.addElement(t.getTeacherId() + " - " + t.getFullName());
        }

        courseListModel.clear();
        for (Course c : schedule.getCourses()) {
            courseListModel.addElement(c.getCourseCode() + " - " + c.getTitle());
        }

        resourceListModel.clear();
        for (Resource r : schedule.getResources()) {
            resourceListModel.addElement(r.getId() + " - " + r.getName());
        }

        assignmentListModel.clear();
        for (Assignment a : schedule.getAssignments()) {
            String line = a.getId() + ": " + a.getCourse().getCourseCode() + " | " + 
                         a.getTeacher().getFullName() + " | " + a.getResource().getId() + " | " + 
                         a.getTimeBlock().getId() + " | Students: " + a.getStudents().size();
            assignmentListModel.addElement(line);
        }

        // Refresh dropdowns
        
        courseCombo.removeAllItems();
        for (Course c : schedule.getCourses()) {
            courseCombo.addItem(c.getCourseCode() + " - " + c.getTitle());
        }

        teacherCombo.removeAllItems();
        for (Teacher t : schedule.getTeachers()) {
            teacherCombo.addItem(t.getTeacherId() + " - " + t.getFullName());
        }

        resourceCombo.removeAllItems();
        for (Resource r : schedule.getResources()) {
            resourceCombo.addItem(r.getId() + " - " + r.getName());
        }

        timeCombo.removeAllItems();
        for (TimeBlock tb : schedule.getTimeBlocks()) {
            timeCombo.addItem(tb.getId() + " (" + tb.getDay() + " " + tb.getStartTime() + "-" + tb.getEndTime() + ")");
        }

        // Refresh conflicts
        
        ArrayList<String> conflicts = validator.checkConflicts(schedule);
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

    // Helper methods to find objects
    
    private Course findCourse(String code) {
        for (Course c : schedule.getCourses()) {
            if (c.getCourseCode().equals(code)) return c;
        }
        return null;
    }

    private Teacher findTeacher(String id) {
        for (Teacher t : schedule.getTeachers()) {
            if (t.getTeacherId().equals(id)) return t;
        }
        return null;
    }

    private Resource findResource(String id) {
        for (Resource r : schedule.getResources()) {
            if (r.getId().equals(id)) return r;
        }
        return null;
    }

    private TimeBlock findTimeBlock(String display) {
        String id = display.split(" \\(")[0];
        for (TimeBlock tb : schedule.getTimeBlocks()) {
            if (tb.getId().equals(id)) return tb;
        }
        return null;
    }

    private Assignment findAssignment(String id) {
        for (Assignment a : schedule.getAssignments()) {
            if (a.getId().equals(id)) return a;
        }
        return null;
    }

    public static void main(String[] args) {
        new SchedulerApp();
    }
}