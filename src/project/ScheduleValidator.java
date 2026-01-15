package project;

import java.util.ArrayList;

/*
 * ScheduleValidator (MVP)
 * - Keeps the conflict checks simple and easy to read
 * - Conflict types:
 *   - teacher double-booking
 *   - room double-booking
 *   - student timetable conflicts
 *   - room capacity exceeded
 *   - resource double-booking (equipment, carts, etc.)
 */
public class ScheduleValidator {

    public ArrayList<String> checkConflicts(Schedule schedule) {
        ArrayList<String> conflicts = new ArrayList<>();

        conflicts.addAll(checkTeacherConflicts(schedule));
        conflicts.addAll(checkRoomConflicts(schedule));
        conflicts.addAll(checkStudentConflicts(schedule));
        conflicts.addAll(checkCapacityConflicts(schedule));
        conflicts.addAll(checkResourceDoubleBooking(schedule));

        return conflicts;
    }

    public ArrayList<String> checkTeacherConflicts(Schedule schedule) {
        ArrayList<String> conflicts = new ArrayList<>();
        ArrayList<Assignment> list = schedule.getAssignments();

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                Assignment a1 = list.get(i);
                Assignment a2 = list.get(j);
                if (a1.getTeacher() == null || a2.getTeacher() == null) continue;
                if (a1.getTimeBlock() == null || a2.getTimeBlock() == null) continue;

                if (a1.getTeacher().getTeacherId().equals(a2.getTeacher().getTeacherId())
                        && a1.getTimeBlock().getId().equals(a2.getTimeBlock().getId())) {

                    String c1 = a1.getCourse() != null ? a1.getCourse().getCourseCode() : "Unknown";
                    String c2 = a2.getCourse() != null ? a2.getCourse().getCourseCode() : "Unknown";

                    conflicts.add("TEACHER CONFLICT: " + a1.getTeacher().getFullName()
                            + " assigned to " + c1 + " and " + c2 + " at " + a1.getTimeBlock().getId());
                }
            }
        }
        return conflicts;
    }

    public ArrayList<String> checkRoomConflicts(Schedule schedule) {
        ArrayList<String> conflicts = new ArrayList<>();
        ArrayList<Assignment> list = schedule.getAssignments();

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                Assignment a1 = list.get(i);
                Assignment a2 = list.get(j);
                if (a1.getTimeBlock() == null || a2.getTimeBlock() == null) continue;

                Room r1 = a1.getRoom();
                Room r2 = a2.getRoom();
                if (r1 == null || r2 == null) continue;

                if (r1.getId().equals(r2.getId()) && a1.getTimeBlock().getId().equals(a2.getTimeBlock().getId())) {
                    String c1 = a1.getCourse() != null ? a1.getCourse().getCourseCode() : "Unknown";
                    String c2 = a2.getCourse() != null ? a2.getCourse().getCourseCode() : "Unknown";
                    conflicts.add("ROOM CONFLICT: Room " + r1.getRoomNumber()
                            + " used by " + c1 + " and " + c2 + " at " + a1.getTimeBlock().getId());
                }
            }
        }
        return conflicts;
    }

    public ArrayList<String> checkStudentConflicts(Schedule schedule) {
        ArrayList<String> conflicts = new ArrayList<>();
        ArrayList<Assignment> list = schedule.getAssignments();

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                Assignment a1 = list.get(i);
                Assignment a2 = list.get(j);
                if (a1.getTimeBlock() == null || a2.getTimeBlock() == null) continue;
                if (!a1.getTimeBlock().getId().equals(a2.getTimeBlock().getId())) continue;

                for (Student s : a1.getStudents()) {
                    for (Student t : a2.getStudents()) {
                        if (s.getStudentId().equals(t.getStudentId())) {
                            String c1 = a1.getCourse() != null ? a1.getCourse().getCourseCode() : "Unknown";
                            String c2 = a2.getCourse() != null ? a2.getCourse().getCourseCode() : "Unknown";
                            conflicts.add("STUDENT CONFLICT: " + s.getFullName() + " in " + c1 + " and " + c2
                                    + " at " + a1.getTimeBlock().getId());
                        }
                    }
                }
            }
        }
        return conflicts;
    }

    public ArrayList<String> checkCapacityConflicts(Schedule schedule) {
        ArrayList<String> conflicts = new ArrayList<>();
        for (Assignment a : schedule.getAssignments()) {
            Room r = a.getRoom();
            if (r != null) {
                int cap = r.getCapacity();
                int enrolled = a.getStudents().size();
                if (enrolled > cap) {
                    conflicts.add("CAPACITY CONFLICT: Assignment " + a.getId() + " in room " + r.getRoomNumber()
                            + " has " + enrolled + " students (cap " + cap + ")");
                }
            }
        }
        return conflicts;
    }

    public ArrayList<String> checkResourceDoubleBooking(Schedule schedule) {
        ArrayList<String> conflicts = new ArrayList<>();
        ArrayList<Assignment> list = schedule.getAssignments();

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                Assignment a1 = list.get(i);
                Assignment a2 = list.get(j);
                if (a1.getTimeBlock() == null || a2.getTimeBlock() == null) continue;
                if (!a1.getTimeBlock().getId().equals(a2.getTimeBlock().getId())) continue;

                for (Resource r1 : a1.getResources()) {
                    for (Resource r2 : a2.getResources()) {
                        if (r1.getId().equals(r2.getId())) {
                            if (r1 instanceof Room) continue; // room conflicts are reported above
                            conflicts.add("RESOURCE CONFLICT: Resource " + r1.getId() + " (" + r1.getName() + ") "
                                    + "used by " + a1.getId() + " and " + a2.getId() + " at " + a1.getTimeBlock().getId());
                        }
                    }
                }
            }
        }
        return conflicts;
    }
}