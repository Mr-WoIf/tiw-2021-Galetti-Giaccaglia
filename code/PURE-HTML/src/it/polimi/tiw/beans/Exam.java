package it.polimi.tiw.beans;

import java.util.Date;

public class Exam {
	
	private final Date date;
	private final int id;
	private final int courseId;
	
	public Exam(Date date, int id, int courseId) {
		
		this.date = date;
		this.id = id;
		this.courseId = courseId;
		
	}
	
	public Date geDate() {
		return date;	
	}
	
	public int getId() {
		return id;
	}
	
	public int getCourseId() {
		return courseId;
	}

}
