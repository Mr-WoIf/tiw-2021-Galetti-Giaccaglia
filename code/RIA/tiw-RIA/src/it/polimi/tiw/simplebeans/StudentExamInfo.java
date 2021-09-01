package it.polimi.tiw.simplebeans;

import it.polimi.tiw.beans.Student;
import it.polimi.tiw.utils.MutablePair;

public class StudentExamInfo {
	
	private MutablePair<Student, MutablePair<Integer, String>> studentInfo;
	private int examId;
	private int courseId;
	private boolean hasBeenPublished;
	
	public StudentExamInfo( MutablePair<Student, MutablePair<Integer, String>> studentInfo, int examId, int courseId, boolean hasBeenPublished) {
		this.studentInfo = studentInfo;
		this.examId = examId;
		this.courseId = courseId;
		this.hasBeenPublished = hasBeenPublished;
	}
	
	public MutablePair<Student, MutablePair<Integer, String>> getStudentInfo(){
		return studentInfo;
	}
	
	public int getExamId() {
		return examId;
	}
	
	public int getCourseId() {
		return courseId;
	}
	
	public boolean hasBeenPublished() {
		return hasBeenPublished;
	}

}
