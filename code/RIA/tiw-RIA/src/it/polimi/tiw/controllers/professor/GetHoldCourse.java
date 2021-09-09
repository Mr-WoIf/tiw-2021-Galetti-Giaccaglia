package it.polimi.tiw.controllers.professor;

import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.beans.Course;
import it.polimi.tiw.beans.Exam;
import it.polimi.tiw.beans.Professor;
import it.polimi.tiw.dao.CourseDAO;
import it.polimi.tiw.dao.ExamDAO;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.MutablePair;
import it.polimi.tiw.utils.ResponseUtils;

/**
 * Servlet implementation class ToHoldCoursePage
 */
@WebServlet("/GetHoldCourse")
@MultipartConfig
public class GetHoldCourse extends HttpServlet {

	@Serial
	private static final long serialVersionUID = 1L;
	private Connection connection;

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
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String courseIdString = request.getParameter("courseId");

		if(courseIdString == null) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Missing course ID, when accessing course details");
			return;
		}

		int courseId;
		CourseDAO courseDAO = new CourseDAO(connection);
		Course course;
		ExamDAO examDAO = new ExamDAO(connection);
		List<Exam> exams;

		try {
			courseId = Integer.parseInt(courseIdString);
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Chosen course ID is not a number, when accessing courses details");
			return;
		}

		HttpSession session = request.getSession(false);
		Professor currentProfessor = (Professor)session.getAttribute("professor");
		
		if(currentProfessor==null) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized to perform this action!");
			return;	
		}

		//fetching professor courses to get updated courses list
		try {
			currentProfessor.setCourses(courseDAO.getCoursesByProfessorId(currentProfessor.getId()));
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "There has been an error finding courses by professor ID");
			return;	
		}
		
		try {
			if(courseDAO.isCourseIdInvalid(courseId)) {
				ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_NOT_FOUND, "Course id doesn't match any currently active course");
				return;	
			}
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;			
		}

		if(currentProfessor.getCourseById(courseId).isEmpty()) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "Course is not held by you");
			return;
		}

		course =  currentProfessor.getCourseById(courseId).get();
		
		try {
			exams = examDAO.getExamsByCourseId(courseId);
			
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;	
		}

		Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
		String json = gson.toJson(new MutablePair<>(course , exams));
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}