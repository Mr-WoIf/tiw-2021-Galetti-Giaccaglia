package it.polimi.tiw.beans;

public class Student extends User{
	
	private final String degree;
	
	private final String school;
	
	public Student(int id, String email, String name, String surname, String role, String school, String degree) {
		
		super(id, email, name, surname, role);
		this.degree = degree;
		this.school = school;
		
	}

	public String getDegree() {
		return degree;
	}
	
	public String getSchool() {
		return school;
	}

}
