package it.polimi.tiw.controllers.student;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;

import it.polimi.tiw.beans.Course;
import it.polimi.tiw.beans.Exam;
import it.polimi.tiw.beans.Student;
import it.polimi.tiw.dao.CourseDAO;
import it.polimi.tiw.dao.ExamDAO;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.ForwardHandler;
import it.polimi.tiw.utils.PathUtils;
import it.polimi.tiw.utils.TemplateHandler;



/**
 * Servlet implementation class ToHoldCoursePage
 */
@WebServlet("/GoToCourse")
public class GoToCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GoToCourse() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		this.templateEngine = TemplateHandler.getEngine(servletContext, ".html");
		this.connection = ConnectionHandler.getConnection(servletContext);
	}

	@Override
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		String courseIdString = request.getParameter("courseId");

		if(courseIdString == null) {
			ForwardHandler.forwardToErrorPage(request, response, "Null courseId, when accessing course details", templateEngine);
		}

		int courseId;

		CourseDAO courseDAO = new CourseDAO(connection);
		Course course = null;
		ExamDAO examDAO = new ExamDAO(connection);
		List<Exam> exams = null;

		try {
			courseId = Integer.parseInt(courseIdString);
		}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen account id is not a number, when accessing courses details", templateEngine);
			return;
		}

		HttpSession session = request.getSession(false);
		Student currentStudent = (Student)session.getAttribute("student");
		int studentId = currentStudent.getId();

		//fetching professor courses to get updated courses list
		try {
			currentStudent.setCourses(courseDAO.getCoursesByStudentId(studentId));
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return;		
		}
		
		
		
		try {
			if(!courseDAO.isCourseIdValid(courseId)) {
				ForwardHandler.forwardToErrorPage(request, response, "Course id doesn't match any currently active course", templateEngine);
				return;
			}
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return;		
		}

		if(currentStudent.getCourseById(courseId).isEmpty()) {
			ForwardHandler.forwardToErrorPage(request, response, "You are not subscribed to this course!", templateEngine);
			return;
		}
	
		course = currentStudent.getCourseById(courseId).get();
		

		try {
			exams = examDAO.getSubscribedExamsByStudentID(studentId, courseId);
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return;		
		}


		request.setAttribute("course", course);
		request.setAttribute("exams", exams);
		ForwardHandler.forward(request, response, PathUtils.pathToCoursePage, templateEngine);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
