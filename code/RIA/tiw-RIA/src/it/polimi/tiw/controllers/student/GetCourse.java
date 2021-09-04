package it.polimi.tiw.controllers.student;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import it.polimi.tiw.beans.Course;
import it.polimi.tiw.beans.Exam;
import it.polimi.tiw.beans.Student;
import it.polimi.tiw.dao.CourseDAO;
import it.polimi.tiw.dao.ExamDAO;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.MutablePair;
import it.polimi.tiw.utils.ResponseUtils;



/**
 * Servlet implementation class ToHoldCoursePage
 */
@WebServlet("/GetCourse")
@MultipartConfig
public class GetCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetCourse() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
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
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Missing course ID, when accessing course details");
			return;
		}

		int courseId;

		CourseDAO courseDAO = new CourseDAO(connection);
		Course course = null;
		ExamDAO examDAO = new ExamDAO(connection);
		List<Exam> exams = null;

		try {
			courseId = Integer.parseInt(courseIdString);
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Chosen account ID is not a number, when accessing courses details");
			return;
		}

		HttpSession session = request.getSession(false);
		Student currentStudent = (Student)session.getAttribute("student");
		int studentId = currentStudent.getId();

		//fetching professor courses to get updated courses list
		try {
			currentStudent.setCourses(courseDAO.getCoursesByStudentId(studentId));
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}
		
		try {
			if(!courseDAO.isCourseIdValid(courseId)) {
				ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Course ID doesn't match any currently active course");
				return;
			}
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;	
		}

		if(currentStudent.getCourseById(courseId).isEmpty()) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "You are not subscribed to this course!");
			return;
		}
	
		course = currentStudent.getCourseById(courseId).get();
		

		try {
			exams = examDAO.getSubscribedExamsByStudentID(studentId, courseId);
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;	
		}

		try {
			exams = examDAO.getSubscribedExamsByStudentID(studentId, courseId);
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;			
		}
		
		
		

	//	request.setAttribute("course", course);
	//	request.setAttribute("exams", exams);
		
		String json = new Gson().toJson(new MutablePair<Course, List<Exam>>(course, exams));
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
