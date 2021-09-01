package it.polimi.tiw.beans;

public class ExamRegister {
	
	private int studentId;
	private int examId;
	private int grade;
	private int reportId;
	private String state;
	
	public ExamRegister(int studentId, int examId, int grade, int reportId, String state) {
		
		this.studentId = studentId;
		this.examId = examId;
		this.grade = grade;
		this.reportId = reportId;
		this.state = state;
	}

	public int getStudentId() {
		return studentId;
	}

	public int getExamId() {
		return examId;
	}

	public int getGrade() {
		return grade;
	}

	public int getReportId() {
		return reportId;
	}

	public String getState() {
		return state;
	}
	
	
}
