package it.polimi.tiw.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;

import it.polimi.tiw.beans.Exam;
import it.polimi.tiw.beans.Professor;
import it.polimi.tiw.beans.Student;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.CourseDAO;
import it.polimi.tiw.dao.ExamDAO;
import it.polimi.tiw.dao.ExamRegisterDAO;

public class DaoUtils {

	private DaoUtils(){}
	
	public static boolean verifyRequestCommonConstraints(	
			
			Connection connection,
			TemplateEngine templateEngine,
			HttpServletRequest request, 
			HttpServletResponse response, 
			int studentId, 
			int examId, 
			int courseId,
			MutablePair<Student, MutablePair<Integer, String>> studentInfo,
			User user
			
) throws ServletException, IOException {

	Optional<Exam> optExam;
	Student student;
	MutablePair<Integer, String> studentExamInfo;
	CourseDAO courseDAO = new CourseDAO(connection);
	ExamDAO examDAO = new ExamDAO(connection);
	ExamRegisterDAO examRegisterDAO = new ExamRegisterDAO(connection);
	Exam exam;


	try {
		if(courseDAO.isCourseIdNotValid(courseId)) {
			ForwardHandler.forwardToErrorPage(request, response, "Course id doesn't match any currently active course", templateEngine);
			return true;
		}
	} catch (SQLException e) {
		ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
		return true;
	}

	//fetching professor courses to get updated courses list
	try {
	optExam = examDAO.getExamById(examId);
	} catch (SQLException e) {
	ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
	return true;
	}

	if(optExam.isEmpty()) {
	ForwardHandler.forwardToErrorPage(request, response, "Exam not existing", templateEngine);
	return true;
	}

	exam = optExam.get();

	if(user instanceof Professor) {

		Professor professor = (Professor)user;
		if(professor.getCourseById(exam.getCourseId()).isEmpty()) {
		ForwardHandler.forwardToErrorPage(request, response, "Exam's course not hold by you!", templateEngine);
		return true;
		}

	}

	try {
		student = examRegisterDAO.getStudentsByExamId(examId).stream().filter(studentBean -> studentBean.getId()== studentId).findFirst().orElseThrow();
	} catch (SQLException e) {
		ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
		return true;
	} catch(NoSuchElementException e) {
		String customErrorMessage = user instanceof Professor ? "Student is not subscribed to this exam!" : "You are not subscribed to this exam!";
		ForwardHandler.forwardToErrorPage(request, response, customErrorMessage , templateEngine);
	return true;
	}

	try {
		studentExamInfo = examRegisterDAO.getExamRegisterByStudentID(studentId, examId);
	} catch(SQLException e) {
		ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
		return true;
	}

	if((user instanceof Professor) && (studentExamInfo.getRight().equals("published") || studentExamInfo.getRight().equals("refused") || studentExamInfo.getRight().equals("recorded"))) {
		ForwardHandler.forwardToErrorPage(request, response, "You can't modify this student's exam data, because exam grade state is not 'inserted'", templateEngine);
		return true;
	}

	studentInfo.setLeft(student);
	studentInfo.setRight(studentExamInfo);

	return false;
	}

}
