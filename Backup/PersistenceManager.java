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
					writer.write("ComputerLab," + cl.getId() + "," + cl.getRoomNumber() + ",\n");
				} 
				else if (r instanceof ScienceLab) {
					ScienceLab sl = (ScienceLab) r;
					writer.write("ScienceLab," + sl.getId() + "," + sl.getRoomNumber() + ",\n");
				} 
				else if (r instanceof Gym) {
					Gym g = (Gym) r;
					writer.write("Gym," + g.getId() + "," + g.getRoomNumber() + ",\n");
				} 
				else if (r instanceof Room) {
					Room room = (Room) r;
					writer.write("Room," + room.getId() + "," + room.getRoomNumber() + ",\n");
				} 
				else if (r instanceof Equipment) {
					Equipment e = (Equipment) r;
					writer.write("Equipment," + e.getId() + "," + e.getEquipmentType() + "\n");
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
			
			// all assignments (id,courseCode,teacherId,resourceId1;resourceId2,timeId,studentId1,studentId2,...)
			for (Assignment a : schedule.getAssignments()) {
				
				String resIds = "";
				for (int i = 0; i < a.getResources().size(); i++) {
					if (i > 0) {
						resIds = resIds + ";";
					}
					resIds = resIds + a.getResources().get(i).getId();
				}
				
				writer.write(a.getId() + ",");
				writer.write(a.getCourse().getCourseCode() + ",");
				writer.write(a.getTeacher().getTeacherId() + ",");
				writer.write(resIds + ",");
				writer.write(a.getTimeBlock().getId());
				
				// add student ids
				for (Student s : a.getStudents()) {
					writer.write("," + s.getStudentId());
				}
				writer.write("\n");
			}
			
			writer.close();
			
		} 
		catch (IOException e) {
			System.out.println("Error saving: " + e.getMessage());
		}
	}

	// load a schedule from a CSV file
	public Schedule load(String filename) {
		Schedule schedule = new Schedule("New Schedule");
		
		try {
			File file = new File(filename);
			Scanner scanner = new Scanner(file);
			
			// line 1: schedule name
			String name = scanner.nextLine();
			schedule = new Schedule(name);
			
			// remove default timeblocks so we only use those from the file (prevents duplicates)  

			schedule.getTimeBlocks().clear();
			
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
					Room r = new Room(parts[1], parts[2]);
					schedule.addRoom(r);
				} 
				else if (type.equals("ComputerLab")) {
					ComputerLab cl = new ComputerLab(parts[1], parts[2]);
					schedule.addRoom(cl);
				} 
				else if (type.equals("ScienceLab")) {
					ScienceLab sl = new ScienceLab(parts[1], parts[2]);
					schedule.addRoom(sl);
				} 
				else if (type.equals("Gym")) {
					Gym g = new Gym(parts[1], parts[2]);
					schedule.addRoom(g);
				} 
				else if (type.equals("Equipment")) {
					Equipment e = new Equipment(parts[1], parts[2]);
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
				Course course = findCourse(schedule, parts[1]);
				
				// find teacher
				Teacher teacher = findTeacher(schedule, parts[2]);
				
				// resourceIds field may contain semicolon-separated ids
				String resourceField = parts[3];
				String[] resourceIds = resourceField.split(";");
				Resource primaryResource = findResource(schedule, resourceIds[0]);
				
				// find timeblock
				TimeBlock timeBlock = findTimeBlock(schedule, parts[4]);
				
				// create assignment (pass first resource if present)
				Assignment a = new Assignment(parts[0], course, teacher, primaryResource, timeBlock);
				
				// if more resources present, add them
				if (resourceIds.length > 1) {
					for (int ri = 1; ri < resourceIds.length; ri++) {
						String rid = resourceIds[ri];
						Resource additionalResource = findResource(schedule, rid);
						a.addResource(additionalResource);
					}
				}
				
				// add students (starting at index 5)
				for (int j = 5; j < parts.length; j++) {
					Student student = findStudent(schedule, parts[j]);
					a.addStudent(student);
				}
				
				schedule.addAssignment(a);
			}
			
			scanner.close();
			
		} 
		catch (IOException e) {
			System.out.println("Error loading: " + e.getMessage());
		}
		
		return schedule;
	}
	
	// Helper methods to find objects in schedule
	private Course findCourse(Schedule schedule, String courseCode) {
		for (Course c : schedule.getCourses()) {
			if (c.getCourseCode().equals(courseCode)) {
				return c;
			}
		}
		return new Course("UNKNOWN", "Unknown Course");
	}
	
	private Teacher findTeacher(Schedule schedule, String teacherId) {
		for (Teacher t : schedule.getTeachers()) {
			if (t.getTeacherId().equals(teacherId)) {
				return t;
			}
		}
		return new Teacher("UNKNOWN", "Unknown Teacher");
	}
	
	private Resource findResource(Schedule schedule, String resourceId) {
		for (Resource r : schedule.getResources()) {
			if (r.getId().equals(resourceId)) {
				return r;
			}
		}
		return new Room("UNKNOWN", "Unknown Room");
	}
	
	private TimeBlock findTimeBlock(Schedule schedule, String timeBlockId) {
		for (TimeBlock tb : schedule.getTimeBlocks()) {
			if (tb.getId().equals(timeBlockId)) {
				return tb;
			}
		}
		return new TimeBlock("UNKNOWN", "Monday", "00:00", "00:00");
	}
	
	private Student findStudent(Schedule schedule, String studentId) {
		for (Student s : schedule.getStudents()) {
			if (s.getStudentId().equals(studentId)) {
				return s;
			}
		}
		return new Student("UNKNOWN", "Unknown Student");
	}
}