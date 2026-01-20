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

    public ArrayList<String> checkConflicts(Schedule schedule) {
        ArrayList<String> conflicts = new ArrayList<>();

        ArrayList<String> teacherConflicts = checkTeacherConflicts(schedule);
        ArrayList<String> roomConflicts = checkRoomConflicts(schedule);
        ArrayList<String> studentConflicts = checkStudentConflicts(schedule);
        ArrayList<String> resourceConflicts = checkResourceDoubleBooking(schedule);

        conflicts.addAll(teacherConflicts);
        conflicts.addAll(roomConflicts);
        conflicts.addAll(studentConflicts);
        conflicts.addAll(resourceConflicts);

        return conflicts;
    }

    public ArrayList<String> checkTeacherConflicts(Schedule schedule) {
        ArrayList<String> conflicts = new ArrayList<>();
        ArrayList<Assignment> list = schedule.getAssignments();

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                Assignment a1 = list.get(i);
                Assignment a2 = list.get(j);
                
                if (a1.getTeacher() == null) {
                    continue;
                }
                
                if (a2.getTeacher() == null) {
                    continue;
                }
                
                if (a1.getTimeBlock() == null) {
                    continue;
                }
                
                if (a2.getTimeBlock() == null) {
                    continue;
                }

                boolean sameTeacher = a1.getTeacher().getTeacherId().equals(a2.getTeacher().getTeacherId());
                boolean sameTime = a1.getTimeBlock().getId().equals(a2.getTimeBlock().getId());
                
                if (sameTeacher && sameTime) {
                    String c1 = "Unknown";
                    if (a1.getCourse() != null) {
                        c1 = a1.getCourse().getCourseCode();
                    }
                    
                    String c2 = "Unknown";
                    if (a2.getCourse() != null) {
                        c2 = a2.getCourse().getCourseCode();
                    }

                    String conflictMessage = "**TEACHER CONFLICT:** ";
                    conflictMessage += a1.getTeacher().getFullName();
                    conflictMessage += " assigned to ";
                    conflictMessage += c1;
                    conflictMessage += " and ";
                    conflictMessage += c2;
                    conflictMessage += " at ";
                    conflictMessage += a1.getTimeBlock().getId();
                    
                    conflicts.add(conflictMessage);
                }
            }
        }
        
        return conflicts;
    }

    public ArrayList<String> checkRoomConflicts(Schedule schedule) {
        ArrayList<String> conflicts = new ArrayList<>();
        ArrayList<Assignment> list = schedule.getAssignments();
        ArrayList<String> reportedKeys = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                Assignment a1 = list.get(i);
                Assignment a2 = list.get(j);
                
                if (a1.getTimeBlock() == null) {
                    continue;
                }
                
                if (a2.getTimeBlock() == null) {
                    continue;
                }
                
                boolean sameTime = a1.getTimeBlock().getId().equals(a2.getTimeBlock().getId());
                if (!sameTime) {
                    continue;
                }

                // Check all rooms in both assignments
                for (Resource res1 : a1.getResources()) {
                    if (!(res1 instanceof Room)) {
                        continue;
                    }
                    
                    Room r1 = (Room) res1;
                    
                    for (Resource res2 : a2.getResources()) {
                        if (!(res2 instanceof Room)) {
                            continue;
                        }
                        
                        Room r2 = (Room) res2;
                        
                        boolean sameRoom = r1.getId().equals(r2.getId());
                        if (sameRoom) {
                            // Create unique key to avoid duplicate reports
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
                            
                            if (!alreadyReported) {
                                String c1 = "Unknown";
                                if (a1.getCourse() != null) {
                                    c1 = a1.getCourse().getCourseCode();
                                }
                                
                                String c2 = "Unknown";
                                if (a2.getCourse() != null) {
                                    c2 = a2.getCourse().getCourseCode();
                                }
                                
                                String conflictMessage = "**ROOM CONFLICT:** Room ";
                                conflictMessage += r1.getRoomNumber();
                                conflictMessage += " used by ";
                                conflictMessage += c1;
                                conflictMessage += " and ";
                                conflictMessage += c2;
                                conflictMessage += " at ";
                                conflictMessage += a1.getTimeBlock().getId();
                                
                                conflicts.add(conflictMessage);
                                reportedKeys.add(key);
                            }
                        }
                    }
                }
            }
        }
        
        return conflicts;
    }

    public ArrayList<String> checkStudentConflicts(Schedule schedule) {
        ArrayList<String> conflicts = new ArrayList<>();
        ArrayList<Assignment> list = schedule.getAssignments();
        ArrayList<String> reportedStudentConflicts = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                Assignment a1 = list.get(i);
                Assignment a2 = list.get(j);
                
                if (a1.getTimeBlock() == null) {
                    continue;
                }
                
                if (a2.getTimeBlock() == null) {
                    continue;
                }
                
                boolean sameTime = a1.getTimeBlock().getId().equals(a2.getTimeBlock().getId());
                if (!sameTime) {
                    continue;
                }

                for (Student s : a1.getStudents()) {
                    for (Student t : a2.getStudents()) {
                        boolean sameStudent = s.getStudentId().equals(t.getStudentId());
                        if (sameStudent) {
                            // Create unique key for this conflict
                            String conflictKey = s.getStudentId() + "|" + a1.getTimeBlock().getId();
                            
                            // Check if already reported
                            boolean alreadyReported = false;
                            for (String key : reportedStudentConflicts) {
                                if (key.equals(conflictKey)) {
                                    alreadyReported = true;
                                    break;
                                }
                            }
                            
                            if (!alreadyReported) {
                                String c1 = "Unknown";
                                if (a1.getCourse() != null) {
                                    c1 = a1.getCourse().getCourseCode();
                                }
                                
                                String c2 = "Unknown";
                                if (a2.getCourse() != null) {
                                    c2 = a2.getCourse().getCourseCode();
                                }
                                
                                String conflictMessage = "**STUDENT CONFLICT:** ";
                                conflictMessage += s.getFullName();
                                conflictMessage += " in ";
                                conflictMessage += c1;
                                conflictMessage += " and ";
                                conflictMessage += c2;
                                conflictMessage += " at ";
                                conflictMessage += a1.getTimeBlock().getId();
                                
                                conflicts.add(conflictMessage);
                                reportedStudentConflicts.add(conflictKey);
                            }
                        }
                    }
                }
            }
        }
        
        return conflicts;
    }

    public ArrayList<String> checkResourceDoubleBooking(Schedule schedule) {
        ArrayList<String> conflicts = new ArrayList<>();
        ArrayList<Assignment> list = schedule.getAssignments();
        ArrayList<String> reportedResourceConflicts = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                Assignment a1 = list.get(i);
                Assignment a2 = list.get(j);
                
                if (a1.getTimeBlock() == null) {
                    continue;
                }
                
                if (a2.getTimeBlock() == null) {
                    continue;
                }
                
                boolean sameTime = a1.getTimeBlock().getId().equals(a2.getTimeBlock().getId());
                if (!sameTime) {
                    continue;
                }

                // Check all resources in both assignments
                for (Resource r1 : a1.getResources()) {
                    for (Resource r2 : a2.getResources()) {
                        boolean sameResource = r1.getId().equals(r2.getId());
                        if (sameResource) {
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
                            
                            if (!alreadyReported) {
                                String c1 = "Unknown";
                                if (a1.getCourse() != null) {
                                    c1 = a1.getCourse().getCourseCode();
                                }
                                
                                String c2 = "Unknown";
                                if (a2.getCourse() != null) {
                                    c2 = a2.getCourse().getCourseCode();
                                }
                                
                                String conflictMessage = "**RESOURCE CONFLICT:** ";
                                conflictMessage += r1.getId();
                                conflictMessage += " used by ";
                                conflictMessage += c1;
                                conflictMessage += " and ";
                                conflictMessage += c2;
                                conflictMessage += " at ";
                                conflictMessage += a1.getTimeBlock().getId();
                                
                                conflicts.add(conflictMessage);
                                reportedResourceConflicts.add(conflictKey);
                            }
                        }
                    }
                }
            }
        }
        
        return conflicts;
    }
}