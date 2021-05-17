package it.polimi.tiw.beans;

import java.util.Date;

public class Exam {
	
	private final Date date;
	private final int id;
	
	public Exam(Date date, int id) {
		
		this.date = date;
		this.id = id;
		
	}
	
	public Date geDate() {
		return date;	
	}
	
	public int getId() {
		return id;
	}

}
