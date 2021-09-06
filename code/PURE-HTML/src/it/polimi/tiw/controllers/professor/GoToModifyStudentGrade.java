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

import org.thymeleaf.TemplateEngine;

import it.polimi.tiw.beans.Professor;
import it.polimi.tiw.beans.Student;
import it.polimi.tiw.dao.ExamRegisterDAO;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.DaoUtils;
import it.polimi.tiw.utils.ForwardHandler;
import it.polimi.tiw.utils.MutablePair;
import it.polimi.tiw.utils.PathUtils;
import it.polimi.tiw.utils.TemplateHandler;
/**
 * Servlet implementation class ModifyStudentGrade
 */
	
@WebServlet("/GoToModifyStudentGrade")
public class GoToModifyStudentGrade extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
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


		String examIdString = request.getParameter("examId");
		String courseIdString = request.getParameter("courseId");
		String studentIdString = request.getParameter("studentId");
	
		int examId;
		int courseId;
		int studentId;
		MutablePair<Student, MutablePair<Integer, String>> studentInfo =  new MutablePair <Student, MutablePair<Integer, String>> (null, null);
		
		
		
		session = request.getSession(false);
		Professor professor = (Professor)session.getAttribute("professor");
		
		try {
			examId = Integer.parseInt(examIdString);
		}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen exam id is not a number, when accessing exam details", templateEngine);
			return;
		}
		
		try {
			courseId = Integer.parseInt(courseIdString);
		}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen exam's course id is not a number, when accessing exam details", templateEngine);
			return;
		}
		
		try {
			studentId = Integer.parseInt(studentIdString);
			}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen student id is not a number, when accessing exam details", templateEngine);
			return;
		}

		
		if(!DaoUtils.verifyRequestCommonConstraints(connection, templateEngine, request,response, studentId,examId, courseId, studentInfo, professor))
			return;

		request.setAttribute("studentInfo", studentInfo);
		request.setAttribute("examId", examId);
		request.setAttribute("courseId", courseId);
		request.setAttribute("error", "");
		ForwardHandler.forward(request, response, PathUtils.pathToGradePageProfessor,  templateEngine);
			
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
			ForwardHandler.forwardToErrorPage(request, response, "Invalid course Id related to exam Id", templateEngine);
			return;
		}
		
		try {
			examId = Integer.parseInt(examIdString);
		}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen exam id is not a number, when accessing exam details", templateEngine);
			return;
		}
		
		try {
			studentId = Integer.parseInt(studentIdString);
			}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen student id is not a number, when accessing exam details", templateEngine);
			return;
		}


		try {
				studentGrade = Integer.parseInt(studentGradeString);
		}catch (NumberFormatException e) {
				ForwardHandler.forwardToErrorPage(request, response, "Submitted grade is not valid", templateEngine);
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
		
		if(!DaoUtils.verifyRequestCommonConstraints(connection, templateEngine, request,response, studentId, examId, courseId, studentInfo, professor))
			return;
		
		
		ExamRegisterDAO examRegisterDAO = new ExamRegisterDAO(connection);

		
		try {
			examRegisterDAO.setGradeByStudentId(studentInfo.getLeft().getId(), examId, studentGrade);
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return;	
		}
		
		
		response.sendRedirect(getServletContext().getContextPath() + "/GoToRegisteredStudents?courseId="+ courseId + "&examId=" + examId + "&requestType='load'");
		
	}

  }

