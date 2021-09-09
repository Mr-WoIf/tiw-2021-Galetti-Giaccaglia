package it.polimi.tiw.beans;

import java.util.List;
import java.util.Optional;

public class Professor extends User{
	
	private final String department;
	private List<Course> courses;

	public Professor(int id, String email, String name, String surname, String role, String department) {
		super(id, email, name, surname, role);
		this.department = department;
	}
	
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public List<Course> getCourses(){
		return courses;
	}
	
	public Optional<Course> getCourseById(int courseId) {
		return courses.stream().filter(course -> course.getId() == courseId).findFirst();
	}
}
