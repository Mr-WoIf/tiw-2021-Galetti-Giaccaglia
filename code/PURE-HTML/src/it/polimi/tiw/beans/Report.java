package it.polimi.tiw.beans;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Report {
	
	private List<Student> students;
	private int reportId;
	private Date reportDate;
	private Map<Integer, Integer> studentsGrades;
	
	public Report(List<Student> students, int reportId, Date reportDate, Map<Integer, Integer> studentsGrades) {
		
		this.students=students;
		this.reportId = reportId;
		this.reportDate = reportDate;
		this.studentsGrades = studentsGrades;

	}

	public List<Student> getStudents() {
		return students;
	}

	public int getReportId() {
		return reportId;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public int getStudentGrade(int studentId) {
		return studentsGrades.get(studentId);
	}
	
	

}
