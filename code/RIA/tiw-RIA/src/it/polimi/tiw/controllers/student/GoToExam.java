package it.polimi.tiw.controllers.student;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import it.polimi.tiw.beans.Exam;
import it.polimi.tiw.beans.Student;
import it.polimi.tiw.dao.CourseDAO;
import it.polimi.tiw.dao.ExamDAO;
import it.polimi.tiw.dao.ExamRegisterDAO;
import it.polimi.tiw.simplebeans.StudentExamInfo;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.DaoUtils;
import it.polimi.tiw.utils.MutablePair;
import it.polimi.tiw.utils.ResponseUtils;

/**
 * Servlet implementation class ToHoldCoursePage
 */
@WebServlet("/GoToExam")
@MultipartConfig
public class GoToExam extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private HttpSession session;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GoToExam() {
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String examIdString = request.getParameter("examId");
		String courseIdString = request.getParameter("courseId");
		
		int examId;
		int courseId;
		boolean hasBeenPublished = false;
		MutablePair<Student, MutablePair<Integer, String>> studentInfo =  new MutablePair <Student, MutablePair<Integer, String>> (null, null);
		
		if(courseIdString == null) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Missing course ID, when accessing course exams details");
			return;	
		}
		
		if(examIdString == null) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Missing exam ID, when accessing course exams details");
			return;	
		}
		
		session = request.getSession(false);
		Student student = (Student)session.getAttribute("student");
		int studentId = student.getId();
		
		CourseDAO courseDAO = new CourseDAO(connection);
	

		
		try {
		examId = Integer.parseInt(examIdString);
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Chosen exam ID is not a number, when accessing exam details");
			return;	
		}
		
		try {
			courseId = Integer.parseInt(courseIdString);
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Chosen exam's course ID is not a number, when accessing exam details");
			return;	
		}
	
		
		if(!DaoUtils.verifyRequestCommonConstraints(connection, request,response,studentId, examId, courseId, studentInfo, student))
			return;


		//fetching professor courses to get updated courses list
		try {
			student.setCourses(courseDAO.getCoursesByStudentId(studentId));
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;		
		}
		

		if(student.getCourseById(courseId).isEmpty()) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "You are not subscribed to this course!");
			return;
		}
		
		if(!(studentInfo.getRight().getRight().equals("inserted") || studentInfo.getRight().getRight().equals("not inserted")))
			hasBeenPublished = true;
		//	request.setAttribute("studentInfo", studentInfo);
		
		
		
		//request.setAttribute("examId", examId);
		//request.setAttribute("courseId", courseId);
		//request.setAttribute("hasBeenPublished", hasBeenPublished);
		
		String json = new Gson().toJson(new StudentExamInfo(studentInfo, examId, courseId, hasBeenPublished));
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int examId;
		int courseId;
		ExamDAO examDAO = new ExamDAO(connection);
		ExamRegisterDAO examRegisterDAO = new ExamRegisterDAO(connection);
		Exam exam = null;
		Optional<Exam> optExam = null;
		MutablePair<Integer, String> register = null;
		String examIdString = request.getParameter("examId");
		String courseIdString = request.getParameter("courseId");

		if (examIdString == null) {
			
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Missing exam ID, nothing to refuse here!");
			return;	
		}
		
		if(courseIdString == null) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Missing course ID, when accessing course exams details");
			return;	
		}
		
		
		try {
			examId = Integer.parseInt(examIdString);
		} catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Chosen exam ID is not a number, when refusing exam grade");
			return;	
		}
		
		try {
			courseId = Integer.parseInt(courseIdString);
		}catch(NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid course Id related to exam Id");
			return;	
		}
		

		HttpSession session = request.getSession(false);
		Student student = (Student) session.getAttribute("student");

		try {
			optExam = examDAO.getExamById(examId);
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;	
		}

		if (optExam.isEmpty()) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Exam not existing, nothing to refuse here!");
			return;	
		}

		exam = optExam.get();

		if (student.getCourseById(exam.getCourseId()).isEmpty()) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "You are not subscribed to this exam's course, there's nothing to refuse!");
			return;	
		}

		try {
			register = examRegisterDAO.getExamRegisterByStudentID(student.getId(), examId);

		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;	
		}

		if (register == null) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "You are not subscribed to this exam, no grade to refuse!");
			return;
		}

		if (register.getRight().equals("not inserted")) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "Exam grade hasn't been published yet, nothing to refuse!");
			return;
		}

		if (register.getRight().equals("refused")) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "Exam grade has beeen already refused!");
			return;
		}

		if (register.getLeft() < 18) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "You were absent or failed this exam, no grade to refuse here!");
			return;
		}

		try {
			examRegisterDAO.setGradeStateByExamID(student.getId(), examId, "refused");
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;	
		}

		response.sendRedirect(getServletContext().getContextPath() + "/GoToExam?courseId="+ courseId + "&examId=" + examId);

	}

}
