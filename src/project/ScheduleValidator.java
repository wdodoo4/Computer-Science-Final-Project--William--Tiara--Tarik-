package project;

import java.util.ArrayList;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: Checks for scheduling conflicts
 */

public class ScheduleValidator {
    
    // check for all conflicts and return a list of problems
	
    public ArrayList<String> checkConflicts(Schedule schedule) {
        ArrayList<String> conflicts = new ArrayList<String>();
        
        // look for all 3 types of conflicts
        
        conflicts.addAll(checkTeacherConflicts(schedule));
        conflicts.addAll(checkRoomConflicts(schedule));
        conflicts.addAll(checkStudentConflicts(schedule));
        
        return conflicts;
    }
    
    // Case 1: check if a teacher is teaching 2 classes at once
    
    public ArrayList<String> checkTeacherConflicts(Schedule schedule) {
        ArrayList<String> conflicts = new ArrayList<String>();
        ArrayList<Assignment> assignments = schedule.getAssignments();
        
        // compare every assignment with every other assignment
        
        for (int i = 0; i < assignments.size(); i++) {
            for (int j = i + 1; j < assignments.size(); j++) {
                Assignment first = assignments.get(i);
                Assignment second = assignments.get(j);
                
                // same teacher and same time = problem
                
                String teacherId1 = first.getTeacher().getTeacherId();
                String teacherId2 = second.getTeacher().getTeacherId();
                String timeId1 = first.getTimeBlock().getId();
                String timeId2 = second.getTimeBlock().getId();
                
                if (teacherId1.equals(teacherId2) && timeId1.equals(timeId2)) {
                    String message = "CONFLICT: " + first.getTeacher().getFullName() + 
                                   " is teaching both " + first.getCourse().getTitle() + 
                                   " and " + second.getCourse().getTitle() + 
                                   " at the same time";
                    conflicts.add(message);
                }
            }
        }
        
        return conflicts;
    }
    
    // Case 2: check if a room is used for 2 classes at once
    
    public ArrayList<String> checkRoomConflicts(Schedule schedule) {
        ArrayList<String> conflicts = new ArrayList<String>();
        ArrayList<Assignment> assignments = schedule.getAssignments();
        
        // compare every assignment with every other assignment
        
        for (int i = 0; i < assignments.size(); i++) {
            for (int j = i + 1; j < assignments.size(); j++) {
                Assignment first = assignments.get(i);
                Assignment second = assignments.get(j);
                
                // same room and same time = problem
                
                String room1 = first.getRoom().getRoomNumber();
                String room2 = second.getRoom().getRoomNumber();
                String timeId1 = first.getTimeBlock().getId();
                String timeId2 = second.getTimeBlock().getId();
                
                if (room1.equals(room2) && timeId1.equals(timeId2)) {
                    String message = "CONFLICT: Room " + room1 + 
                                   " is being used for both " + first.getCourse().getTitle() + 
                                   " and " + second.getCourse().getTitle() + 
                                   " at the same time";
                    conflicts.add(message);
                }
            }
        }
        
        return conflicts;
    }
    
    // Case 3: check if a student is in 2 classes at once
    
    public ArrayList<String> checkStudentConflicts(Schedule schedule) {
        ArrayList<String> conflicts = new ArrayList<String>();
        ArrayList<Assignment> assignments = schedule.getAssignments();
        
        // compare every assignment with every other assignment
        
        for (int i = 0; i < assignments.size(); i++) {
            for (int j = i + 1; j < assignments.size(); j++) {
                Assignment first = assignments.get(i);
                Assignment second = assignments.get(j);
                
                // check if they're at the same time first
                
                String timeId1 = first.getTimeBlock().getId();
                String timeId2 = second.getTimeBlock().getId();
                
                if (timeId1.equals(timeId2)) {
                    // now check if any student is in both classes
                    for (Student student : first.getStudents()) {
                        if (second.getStudents().contains(student)) {
                            String message = "CONFLICT: " + student.getFullName() + 
                                           " is enrolled in both " + first.getCourse().getTitle() + 
                                           " and " + second.getCourse().getTitle() + 
                                           " at the same time";
                            conflicts.add(message);
                        }
                    }
                }
            }
        }
        
        return conflicts;
    }
}