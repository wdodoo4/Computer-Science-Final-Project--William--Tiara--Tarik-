package project;

import java.util.ArrayList;

/*
 * - Conflict types:
 *   - teacher double-booking
 *   - room double-booking
 *   - student timetable conflicts
 *   - resource double-booking (equipment, carts, etc.)
 */

public class ScheduleValidator {

    // Main method that checks all types of conflicts
    public ArrayList<String> checkConflicts(Schedule schedule) {
        // Make empty list to hold all conflict messages
        ArrayList<String> conflicts = new ArrayList<>();

        // Call each specific check method
        ArrayList<String> teacherConflicts = checkTeacherConflicts(schedule);
        ArrayList<String> roomConflicts = checkRoomConflicts(schedule);
        ArrayList<String> studentConflicts = checkStudentConflicts(schedule);
        ArrayList<String> resourceConflicts = checkResourceDoubleBooking(schedule);

        // Combine all conflict lists into one
        conflicts.addAll(teacherConflicts);
        conflicts.addAll(roomConflicts);
        conflicts.addAll(studentConflicts);
        conflicts.addAll(resourceConflicts);

        // Return the complete list of conflicts
        return conflicts;
    }

    // Check if same teacher is assigned to two classes at same time
    public ArrayList<String> checkTeacherConflicts(Schedule schedule) {
        // List for conflict messages about teachers
        ArrayList<String> conflicts = new ArrayList<>();
        // Get all assignments from schedule
        ArrayList<Assignment> list = schedule.getAssignments();

        // Compare each assignment with every other assignment
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                // Get the two assignments we're comparing
                Assignment a1 = list.get(i);
                Assignment a2 = list.get(j);
                
                // Skip if first assignment has no teacher
                if (a1.getTeacher() == null) {
                    continue;
                }
                
                // Skip if second assignment has no teacher
                if (a2.getTeacher() == null) {
                    continue;
                }
                
                // Skip if first assignment has no time block
                if (a1.getTimeBlock() == null) {
                    continue;
                }
                
                // Skip if second assignment has no time block
                if (a2.getTimeBlock() == null) {
                    continue;
                }

                // Check if same teacher ID
                boolean sameTeacher = a1.getTeacher().getTeacherId().equals(a2.getTeacher().getTeacherId());
                // Check if same time block ID
                boolean sameTime = a1.getTimeBlock().getId().equals(a2.getTimeBlock().getId());
                
                // If both same teacher and same time, we have conflict
                if (sameTeacher && sameTime) {
                    // Get course code for first assignment or use "Unknown"
                    String c1 = "Unknown";
                    if (a1.getCourse() != null) {
                        c1 = a1.getCourse().getCourseCode();
                    }
                    
                    // Get course code for second assignment or use "Unknown"
                    String c2 = "Unknown";
                    if (a2.getCourse() != null) {
                        c2 = a2.getCourse().getCourseCode();
                    }

                    // Build the conflict message string
                    String conflictMessage = "**TEACHER CONFLICT:** ";
                    conflictMessage += a1.getTeacher().getFullName();
                    conflictMessage += " assigned to ";
                    conflictMessage += c1;
                    conflictMessage += " and ";
                    conflictMessage += c2;
                    conflictMessage += " at ";
                    conflictMessage += a1.getTimeBlock().getId();
                    
                    // Add message to conflicts list
                    conflicts.add(conflictMessage);
                }
            }
        }
        
        // Return list of teacher conflicts
        return conflicts;
    }

    // Check if same room is used by two classes at same time
    public ArrayList<String> checkRoomConflicts(Schedule schedule) {
        // List for conflict messages about rooms
        ArrayList<String> conflicts = new ArrayList<>();
        // Get all assignments
        ArrayList<Assignment> list = schedule.getAssignments();
        // Keep track of conflicts we already reported to avoid duplicates
        ArrayList<String> reportedKeys = new ArrayList<>();

        // Compare each assignment with every other assignment
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                // Get two assignments to compare
                Assignment a1 = list.get(i);
                Assignment a2 = list.get(j);
                
                // Skip if first assignment has no time block
                if (a1.getTimeBlock() == null) {
                    continue;
                }
                
                // Skip if second assignment has no time block
                if (a2.getTimeBlock() == null) {
                    continue;
                }
                
                // Check if same time block
                boolean sameTime = a1.getTimeBlock().getId().equals(a2.getTimeBlock().getId());
                // If not same time, skip to next comparison
                if (!sameTime) {
                    continue;
                }

                // Look at all resources in first assignment
                for (Resource res1 : a1.getResources()) {
                    // Only check Room resources, skip others
                    if (!(res1 instanceof Room)) {
                        continue;
                    }
                    
                    // Cast to Room object
                    Room r1 = (Room) res1;
                    
                    // Look at all resources in second assignment
                    for (Resource res2 : a2.getResources()) {
                        // Only check Room resources, skip others
                        if (!(res2 instanceof Room)) {
                            continue;
                        }
                        
                        // Cast to Room object
                        Room r2 = (Room) res2;
                        
                        // Check if same room ID
                        boolean sameRoom = r1.getId().equals(r2.getId());
                        // If same room found at same time
                        if (sameRoom) {
                            // Create unique key to identify this specific conflict
                            String key = r1.getId();
                            key += "|";
                            key += a1.getTimeBlock().getId();
                            key += "|";
                            key += Math.min(i, j);
                            key += "|";
                            key += Math.max(i, j);
                            
                            // Check if we already reported this conflict
                            boolean alreadyReported = false;
                            for (String existingKey : reportedKeys) {
                                if (existingKey.equals(key)) {
                                    alreadyReported = true;
                                    break;
                                }
                            }
                            
                            // If not already reported, add it
                            if (!alreadyReported) {
                                // Get course code for first assignment or "Unknown"
                                String c1 = "Unknown";
                                if (a1.getCourse() != null) {
                                    c1 = a1.getCourse().getCourseCode();
                                }
                                
                                // Get course code for second assignment or "Unknown"
                                String c2 = "Unknown";
                                if (a2.getCourse() != null) {
                                    c2 = a2.getCourse().getCourseCode();
                                }
                                
                                // Build conflict message string
                                String conflictMessage = "**ROOM CONFLICT:** Room ";
                                conflictMessage += r1.getRoomNumber();
                                conflictMessage += " used by ";
                                conflictMessage += c1;
                                conflictMessage += " and ";
                                conflictMessage += c2;
                                conflictMessage += " at ";
                                conflictMessage += a1.getTimeBlock().getId();
                                
                                // Add message to conflicts list
                                conflicts.add(conflictMessage);
                                // Remember we reported this conflict
                                reportedKeys.add(key);
                            }
                        }
                    }
                }
            }
        }
        
        // Return list of room conflicts
        return conflicts;
    }

    // Check if same student is in two classes at same time
    public ArrayList<String> checkStudentConflicts(Schedule schedule) {
        // List for conflict messages about students
        ArrayList<String> conflicts = new ArrayList<>();
        // Get all assignments
        ArrayList<Assignment> list = schedule.getAssignments();
        // Keep track of student conflicts already reported
        ArrayList<String> reportedStudentConflicts = new ArrayList<>();

        // Compare each assignment with every other assignment
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                // Get two assignments to compare
                Assignment a1 = list.get(i);
                Assignment a2 = list.get(j);
                
                // Skip if first assignment has no time block
                if (a1.getTimeBlock() == null) {
                    continue;
                }
                
                // Skip if second assignment has no time block
                if (a2.getTimeBlock() == null) {
                    continue;
                }
                
                // Check if same time block
                boolean sameTime = a1.getTimeBlock().getId().equals(a2.getTimeBlock().getId());
                // If not same time, skip to next comparison
                if (!sameTime) {
                    continue;
                }

                // Compare each student in first assignment with each student in second assignment
                for (Student s : a1.getStudents()) {
                    for (Student t : a2.getStudents()) {
                        // Check if same student ID
                        boolean sameStudent = s.getStudentId().equals(t.getStudentId());
                        // If same student found at same time
                        if (sameStudent) {
                            // Create unique key for this student conflict
                            String conflictKey = s.getStudentId() + "|" + a1.getTimeBlock().getId();
                            
                            // Check if already reported
                            boolean alreadyReported = false;
                            for (String key : reportedStudentConflicts) {
                                if (key.equals(conflictKey)) {
                                    alreadyReported = true;
                                    break;
                                }
                            }
                            
                            // If not already reported, add it
                            if (!alreadyReported) {
                                // Get course code for first assignment or "Unknown"
                                String c1 = "Unknown";
                                if (a1.getCourse() != null) {
                                    c1 = a1.getCourse().getCourseCode();
                                }
                                
                                // Get course code for second assignment or "Unknown"
                                String c2 = "Unknown";
                                if (a2.getCourse() != null) {
                                    c2 = a2.getCourse().getCourseCode();
                                }
                                
                                // Build conflict message string
                                String conflictMessage = "**STUDENT CONFLICT:** ";
                                conflictMessage += s.getFullName();
                                conflictMessage += " in ";
                                conflictMessage += c1;
                                conflictMessage += " and ";
                                conflictMessage += c2;
                                conflictMessage += " at ";
                                conflictMessage += a1.getTimeBlock().getId();
                                
                                // Add message to conflicts list
                                conflicts.add(conflictMessage);
                                // Remember we reported this conflict
                                reportedStudentConflicts.add(conflictKey);
                            }
                        }
                    }
                }
            }
        }
        
        // Return list of student conflicts
        return conflicts;
    }

    // Check if same non-room resource is used by two classes at same time
    public ArrayList<String> checkResourceDoubleBooking(Schedule schedule) {
        // List for conflict messages about non-room resources
        ArrayList<String> conflicts = new ArrayList<>();
        // Get all assignments
        ArrayList<Assignment> list = schedule.getAssignments();
        // Keep track of resource conflicts already reported
        ArrayList<String> reportedResourceConflicts = new ArrayList<>();

        // Compare each assignment with every other assignment
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                // Get two assignments to compare
                Assignment a1 = list.get(i);
                Assignment a2 = list.get(j);
                
                // Skip if first assignment has no time block
                if (a1.getTimeBlock() == null) {
                    continue;
                }
                
                // Skip if second assignment has no time block
                if (a2.getTimeBlock() == null) {
                    continue;
                }
                
                // Check if same time block
                boolean sameTime = a1.getTimeBlock().getId().equals(a2.getTimeBlock().getId());
                // If not same time, skip to next comparison
                if (!sameTime) {
                    continue;
                }

                // Compare all resources in first assignment with all resources in second assignment
                for (Resource r1 : a1.getResources()) {
                    for (Resource r2 : a2.getResources()) {
                        // Check if same resource ID
                        boolean sameResource = r1.getId().equals(r2.getId());
                        // If same resource found at same time
                        if (sameResource) {
                            // Skip if it's a Room (room conflicts handled separately)
                            if (r1 instanceof Room) {
                                continue; // room conflicts are reported above
                            }
                            
                            // Create unique key for this resource conflict
                            String conflictKey = r1.getId() + "|" + a1.getTimeBlock().getId();
                            
                            // Check if already reported
                            boolean alreadyReported = false;
                            for (String key : reportedResourceConflicts) {
                                if (key.equals(conflictKey)) {
                                    alreadyReported = true;
                                    break;
                                }
                            }
                            
                            // If not already reported, add it
                            if (!alreadyReported) {
                                // Get course code for first assignment or "Unknown"
                                String c1 = "Unknown";
                                if (a1.getCourse() != null) {
                                    c1 = a1.getCourse().getCourseCode();
                                }
                                
                                // Get course code for second assignment or "Unknown"
                                String c2 = "Unknown";
                                if (a2.getCourse() != null) {
                                    c2 = a2.getCourse().getCourseCode();
                                }
                                
                                // Build conflict message string
                                String conflictMessage = "**RESOURCE CONFLICT:** ";
                                conflictMessage += r1.getId();
                                conflictMessage += " used by ";
                                conflictMessage += c1;
                                conflictMessage += " and ";
                                conflictMessage += c2;
                                conflictMessage += " at ";
                                conflictMessage += a1.getTimeBlock().getId();
                                
                                // Add message to conflicts list
                                conflicts.add(conflictMessage);
                                // Remember we reported this conflict
                                reportedResourceConflicts.add(conflictKey);
                            }
                        }
                    }
                }
            }
        }
        
        // Return list of non-room resource conflicts
        return conflicts;
    }
}