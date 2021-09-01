package it.polimi.tiw.simplebeans;

import it.polimi.tiw.beans.Report;

public class ReportInfo {

	private Report report;
	private int courseId;
	private int examId;
	
	public ReportInfo(Report report, int courseId, int examId) {
		this.report = report;
		this.courseId = courseId;
		this.examId = examId;
	}
	
	public Report getReport() {
		return report;
	}
	public int getCourseId() {
		return courseId;
	}
	public int getExamId() {
		return examId;
	}
	
	
}
