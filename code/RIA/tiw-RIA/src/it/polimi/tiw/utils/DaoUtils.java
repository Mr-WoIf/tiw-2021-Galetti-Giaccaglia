package it.polimi.tiw.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.beans.Exam;
import it.polimi.tiw.beans.Professor;
import it.polimi.tiw.beans.Student;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.CourseDAO;
import it.polimi.tiw.dao.ExamDAO;
import it.polimi.tiw.dao.ExamRegisterDAO;

public class DaoUtils {
	
	public static boolean verifyRequestCommonConstraints(	
			
			Connection connection,
			HttpServletRequest request, 
			HttpServletResponse response, 
			int studentId, 
			int examId, 
			int courseId,
			MutablePair<Student, MutablePair<Integer, String>> studentInfo,
			User user
			
) throws ServletException, IOException {

Optional<Exam> optExam = null;
Student student;
MutablePair<Integer, String> studentExamInfo;
CourseDAO courseDAO = new CourseDAO(connection);
ExamDAO examDAO = new ExamDAO(connection);
ExamRegisterDAO examRegisterDAO = new ExamRegisterDAO(connection);
Exam exam = null;


try {
	if(!courseDAO.isCourseIdValid(courseId)) {
		ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_NOT_FOUND, "Course id doesn't match any currently active course");
		return false;
	}
} catch (SQLException e) {
	ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
	return false;	 
}

//fetching professor courses to get updated courses list
try {
optExam = examDAO.getExamById(examId);
} catch (SQLException e) {
	ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
	return false;		
}

if(optExam.isEmpty()) {
	ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_NOT_FOUND, "Exam not existing");
	return false;
}

exam = optExam.get();

if(user instanceof Professor) {
	
	Professor professor = (Professor)user;
	if(professor.getCourseById(exam.getCourseId()).isEmpty()) {
		ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED,  "Exam's course not hold by you!");
		return false;
	}
	
}

try {
student = examRegisterDAO.getStudentsByExamId(examId).stream().filter(studentBean -> studentBean.getId()== studentId).findFirst().orElseThrow();
} catch (SQLException e) {
	ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
	return false;		
} catch(NoSuchElementException e) {
String customErrorMessage = user instanceof Professor ? "Student is not subscribed to this exam!" : "You are not subscribed to this exam!";
ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, customErrorMessage);
return false;	
}


try {
studentExamInfo = examRegisterDAO.getExamRegisterByStudentID(studentId, examId);
} catch(SQLException e) {
	ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
	return false;		
}

if((user instanceof Professor) && (studentExamInfo.getRight().equals("published") || studentExamInfo.getRight().equals("refused") || studentExamInfo.getRight().equals("reported"))) {
ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "You can't modify this student's exam data, because exam grade state is not 'inserted'");
return false;
}

studentInfo.setLeft(student);
studentInfo.setRight(studentExamInfo);

return true;	
}

}
