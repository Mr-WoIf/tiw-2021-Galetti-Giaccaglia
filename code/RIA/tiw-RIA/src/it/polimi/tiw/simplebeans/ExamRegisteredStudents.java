package it.polimi.tiw.simplebeans;

import java.util.LinkedHashMap;

import it.polimi.tiw.beans.Student;
import it.polimi.tiw.utils.MutablePair;

public class ExamRegisteredStudents {

	private final int examId;
	private final boolean noSubs;
	private final int courseId;
	private final boolean areAllRecorded;
	private final boolean areAllPublished;
	LinkedHashMap<Student, MutablePair<Integer, String>> registerMap;

	public ExamRegisteredStudents(int examId, boolean noSubs, int courseId, boolean areAllRecorded,
								  boolean areAllPublished, LinkedHashMap<Student, MutablePair<Integer, String>> registerMap) {
		this.examId = examId;
		this.noSubs = noSubs;
		this.courseId = courseId;
		this.areAllRecorded = areAllRecorded;
		this.areAllPublished = areAllPublished;
		this.registerMap = registerMap;
	}

	public int getExamId() {
		return examId;
	}
	
	public boolean isNoSubs() {
		return noSubs;
	}
	
	public int getCourseId() {
		return courseId;
	}
	
	public boolean isAreAllRecorded() {
		return areAllRecorded;
	}

	public boolean isAreAllPublished() {
		return areAllPublished;
	}

	public LinkedHashMap<Student, MutablePair<Integer, String>> getRegisterMap() {
		return registerMap;
	}

}
