package it.polimi.tiw.beans;

import java.util.List;
import java.util.Optional;

public class Student extends User{
	
	private final String degree;
	private final String school;
	private List<Course> courses;
	
	public Student(int id, String email, String name, String surname, String role, String school, String degree) {

		super(id, email, name, surname, role);
		this.degree = degree;
		this.school = school;
		
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public String getDegree() {
		return degree;
	}
	
	public String getSchool() {
		return school;
	}
	
	public List<Course> getCourses(){
		return courses;
	}
	
	public Optional<Course> getCourseById(int courseId) {
		return courses.stream().filter(course -> course.getId() == courseId).findFirst();
	}

}
