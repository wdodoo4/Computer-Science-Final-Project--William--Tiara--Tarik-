package project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/* 
 * Name: Tiara, William, Tarik
 * Date: 2026.01.12
 * Description: Save and load schedules using CSV format
 */

public class PersistenceManager {

	// save the schedule to a CSV file
	public void save(Schedule schedule, String filename) {
		try {
			FileWriter writer = new FileWriter(filename);
			
			// line 1: schedule name
			writer.write(schedule.getName() + "\n");
			
			// line 2: number of students
			writer.write(schedule.getStudents().size() + "\n");
			
			// next lines: all students (id,name)
			for (Student s : schedule.getStudents()) {
				writer.write(s.getStudentId() + "," + s.getFullName() + "\n");
			}
			
			// number of teachers
			writer.write(schedule.getTeachers().size() + "\n");
			
			// all teachers (id,name)
			for (Teacher t : schedule.getTeachers()) {
				writer.write(t.getTeacherId() + "," + t.getFullName() + "\n");
			}
			
			// number of resources
			writer.write(schedule.getResources().size() + "\n");
			
			// all resources (type,id,info1,info2)
			for (Resource r : schedule.getResources()) {
				if (r instanceof ComputerLab) {
					ComputerLab cl = (ComputerLab) r;
					writer.write("ComputerLab," + cl.getId() + "," + cl.getRoomNumber() + "," + cl.getCapacity() + "\n");
				} else if (r instanceof ScienceLab) {
					ScienceLab sl = (ScienceLab) r;
					writer.write("ScienceLab," + sl.getId() + "," + sl.getRoomNumber() + "," + sl.getCapacity() + "\n");
				} else if (r instanceof Gym) {
					Gym g = (Gym) r;
					writer.write("Gym," + g.getId() + "," + g.getRoomNumber() + "," + g.getCapacity() + "\n");
				} else if (r instanceof Room) {
					Room room = (Room) r;
					writer.write("Room," + room.getId() + "," + room.getRoomNumber() + "," + room.getCapacity() + "\n");
				} else if (r instanceof Equipment) {
					Equipment e = (Equipment) r;
					writer.write("Equipment," + e.getId() + "," + e.getEquipmentType() + "," + e.getQuantity() + "\n");
				}
			}
			
			// number of courses
			writer.write(schedule.getCourses().size() + "\n");
			
			// all courses (code,title)
			for (Course c : schedule.getCourses()) {
				writer.write(c.getCourseCode() + "," + c.getTitle() + "\n");
			}
			
			// number of timeblocks
			writer.write(schedule.getTimeBlocks().size() + "\n");
			
			// all timeblocks (id,day,start,end)
			for (TimeBlock tb : schedule.getTimeBlocks()) {
				writer.write(tb.getId() + "," + tb.getDay() + "," + tb.getStartTime() + "," + tb.getEndTime() + "\n");
			}
			
			// number of assignments
			writer.write(schedule.getAssignments().size() + "\n");
			
			// all assignments (id,courseCode,teacherId,resourceId,timeId,studentId1,studentId2,...)
			for (Assignment a : schedule.getAssignments()) {
				writer.write(a.getId() + "," + 
						   a.getCourse().getCourseCode() + "," + 
						   a.getTeacher().getTeacherId() + "," + 
						   a.getResource().getId() + "," + 
						   a.getTimeBlock().getId());
				
				// add student ids
				for (Student s : a.getStudents()) {
					writer.write("," + s.getStudentId());
				}
				writer.write("\n");
			}
			
			writer.close();
			
		} catch (IOException e) {
			System.out.println("Error saving: " + e.getMessage());
		}
	}

	// load a schedule from a CSV file
	public Schedule load(String filename) {
		try {
			File file = new File(filename);
			Scanner scanner = new Scanner(file);
			
			// line 1: schedule name
			String name = scanner.nextLine();
			Schedule schedule = new Schedule(name);
			
			// line 2: number of students
			int numStudents = Integer.parseInt(scanner.nextLine());
			
			// read all students
			for (int i = 0; i < numStudents; i++) {
				String[] parts = scanner.nextLine().split(",");
				Student s = new Student(parts[0], parts[1]);
				schedule.addStudent(s);
			}
			
			// number of teachers
			int numTeachers = Integer.parseInt(scanner.nextLine());
			
			// read all teachers
			for (int i = 0; i < numTeachers; i++) {
				String[] parts = scanner.nextLine().split(",");
				Teacher t = new Teacher(parts[0], parts[1]);
				schedule.addTeacher(t);
			}
			
			// number of resources
			int numResources = Integer.parseInt(scanner.nextLine());
			
			// read all resources
			for (int i = 0; i < numResources; i++) {
				String[] parts = scanner.nextLine().split(",");
				String type = parts[0];
				
				if (type.equals("Room")) {
					Room r = new Room(parts[1], parts[2], Integer.parseInt(parts[3]));
					schedule.addRoom(r);
				} else if (type.equals("ComputerLab")) {
					ComputerLab cl = new ComputerLab(parts[1], parts[2], Integer.parseInt(parts[3]));
					schedule.addRoom(cl);
				} else if (type.equals("ScienceLab")) {
					ScienceLab sl = new ScienceLab(parts[1], parts[2], Integer.parseInt(parts[3]));
					schedule.addRoom(sl);
				} else if (type.equals("Gym")) {
					Gym g = new Gym(parts[1], parts[2], Integer.parseInt(parts[3]));
					schedule.addRoom(g);
				} else if (type.equals("Equipment")) {
					Equipment e = new Equipment(parts[1], parts[2], Integer.parseInt(parts[3]));
					schedule.addRoom(e);
				}
			}
			
			// number of courses
			int numCourses = Integer.parseInt(scanner.nextLine());
			
			// read all courses
			for (int i = 0; i < numCourses; i++) {
				String[] parts = scanner.nextLine().split(",");
				Course c = new Course(parts[0], parts[1]);
				schedule.addCourse(c);
			}
			
			// number of timeblocks
			int numTimeBlocks = Integer.parseInt(scanner.nextLine());
			
			// read all timeblocks
			for (int i = 0; i < numTimeBlocks; i++) {
				String[] parts = scanner.nextLine().split(",");
				TimeBlock tb = new TimeBlock(parts[0], parts[1], parts[2], parts[3]);
				schedule.addTimeBlock(tb);
			}
			
			// number of assignments
			int numAssignments = Integer.parseInt(scanner.nextLine());
			
			// read all assignments
			for (int i = 0; i < numAssignments; i++) {
				String[] parts = scanner.nextLine().split(",");
				
				// find course
				Course course = null;
				for (Course c : schedule.getCourses()) {
					if (c.getCourseCode().equals(parts[1])) {
						course = c;
						break;
					}
				}
				
				// find teacher
				Teacher teacher = null;
				for (Teacher t : schedule.getTeachers()) {
					if (t.getTeacherId().equals(parts[2])) {
						teacher = t;
						break;
					}
				}
				
				// find resource
				Resource resource = null;
				for (Resource r : schedule.getResources()) {
					if (r.getId().equals(parts[3])) {
						resource = r;
						break;
					}
				}
				
				// find timeblock
				TimeBlock timeBlock = null;
				for (TimeBlock tb : schedule.getTimeBlocks()) {
					if (tb.getId().equals(parts[4])) {
						timeBlock = tb;
						break;
					}
				}
				
				// create assignment
				Assignment a = new Assignment(parts[0], course, teacher, (Room)resource, timeBlock);
				
				// add students (starting at index 5)
				for (int j = 5; j < parts.length; j++) {
					for (Student s : schedule.getStudents()) {
						if (s.getStudentId().equals(parts[j])) {
							a.addStudent(s);
							break;
						}
					}
				}
				
				schedule.addAssignment(a);
			}
			
			scanner.close();
			return schedule;
			
		} catch (IOException e) {
			System.out.println("Error loading: " + e.getMessage());
			return null;
		}
	}
}