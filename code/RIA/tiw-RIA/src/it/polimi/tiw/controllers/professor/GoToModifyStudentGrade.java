package it.polimi.tiw.controllers.professor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import it.polimi.tiw.beans.Professor;
import it.polimi.tiw.beans.Student;
import it.polimi.tiw.dao.ExamRegisterDAO;
import it.polimi.tiw.simplebeans.StudentExamInfo;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.DaoUtils;
import it.polimi.tiw.utils.MutablePair;
import it.polimi.tiw.utils.ResponseUtils;
/**
 * Servlet implementation class ModifyStudentGrade
 */
	
@WebServlet("/GoToModifyStudentGrade")
public class GoToModifyStudentGrade extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	HttpSession session = null;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GoToModifyStudentGrade() {
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


		String examIdString = request.getParameter("examId");
		String courseIdString = request.getParameter("courseId");
		String studentIdString = request.getParameter("studentId");
	
		int examId;
		int courseId;
		int studentId;
		boolean hasBeenPublished = false;
		MutablePair<Student, MutablePair<Integer, String>> studentInfo =  new MutablePair <Student, MutablePair<Integer, String>> (null, null);
		
		
		
		session = request.getSession(false);
		Professor professor = (Professor)session.getAttribute("professor");
		
		try {
			examId = Integer.parseInt(examIdString);
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Chosen exam id is not a number, when accessing exam details");
			return;	
		}
		
		try {
			courseId = Integer.parseInt(courseIdString);
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Chosen exam's course id is not a number, when accessing exam details");
			return;	
		}
		
		try {
			studentId = Integer.parseInt(studentIdString);
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Chosen student id is not a number, when accessing exam details");
			return;	
		}

		
		DaoUtils.verifyRequestCommonConstraints(connection, request,response, studentId,examId, courseId, studentInfo, professor);

		//request.setAttribute("studentInfo", studentInfo);
		//request.setAttribute("examId", examId);
		//request.setAttribute("courseId", courseId);
		if(!(studentInfo.getRight().getRight().equals("inserted") || studentInfo.getRight().getRight().equals("not inserted")))
			hasBeenPublished = true;
		
		String json = new Gson().toJson(new StudentExamInfo(studentInfo, examId, courseId, hasBeenPublished));
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
			
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String examIdString = request.getParameter("examId");
		String courseIdString = request.getParameter("courseId");
		String studentIdString = request.getParameter("studentId");		
		String studentGradeString = request.getParameter("grade");

		int studentGrade;
		int courseId;
		int examId;
		int studentId;
		
		try {
			courseId = Integer.parseInt(courseIdString);
		}catch(NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid course Id related to exam Id");
			return;	
		}
		
		try {
			examId = Integer.parseInt(examIdString);
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST,  "Chosen exam id is not a number, when accessing exam details");
			return;	
		}
		
		try {
			studentId = Integer.parseInt(studentIdString);
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST,  "Chosen student id is not a number, when accessing exam details");
			return;	
		}


		try {
			studentGrade = Integer.parseInt(studentGradeString);
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Submitted grade is not valid");
			return;	
		}
			
		if(studentGrade > 2 && (studentGrade <18 || studentGrade>31)) {
				String error = studentGrade + " is not a valid grade! Please submit a value between 18 and 31 (laude)";
				request.setAttribute("error", error);
				response.sendRedirect(getServletContext().getContextPath() + "/GoToRegisteredStudents?courseId="+ courseId + "&examId=" + examId);
				return;
		}
		
			
		MutablePair<Student, MutablePair<Integer, String>> studentInfo =  new MutablePair <Student, MutablePair<Integer, String>> (null, null);
		request.getSession(false);
		Professor professor = (Professor)session.getAttribute("professor");
		
		if(!DaoUtils.verifyRequestCommonConstraints(connection, request,response, studentId, examId, courseId, studentInfo, professor))
			return;
		
		
		ExamRegisterDAO examRegisterDAO = new ExamRegisterDAO(connection);

		
		try {
			examRegisterDAO.setGradeByStudentId(studentInfo.getLeft().getId(), examId, studentGrade);
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}
		
		
		response.sendRedirect(getServletContext().getContextPath() + "/GoToRegisteredStudents?courseId="+ courseId + "&examId=" + examId + "&requestType='load'");
		
	}

  }

