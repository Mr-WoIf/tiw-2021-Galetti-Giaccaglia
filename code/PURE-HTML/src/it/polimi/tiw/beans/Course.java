package it.polimi.tiw.beans;

public class Course {
	
	private final int id;
	private final String name;
	private String professor;
	
	public Course(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public void setProfessor(String professor) {
		this.professor = professor;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getProfessor() {
		return professor;
	}

}
