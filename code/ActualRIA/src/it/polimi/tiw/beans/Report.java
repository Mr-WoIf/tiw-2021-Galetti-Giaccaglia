package it.polimi.tiw.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
	
	public String getPrettyPrintedDate() {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String format = formatter.format(reportDate);
		return format;
	}

	public int getStudentGrade(int studentId) {
		return Objects.nonNull(studentsGrades.get(studentId)) ? studentsGrades.get(studentId) : -1;
	}
	
	

}
