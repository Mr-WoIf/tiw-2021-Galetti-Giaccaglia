package it.polimi.tiw.controllers.professor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.AbstractMap.SimpleImmutableEntry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;

import it.polimi.tiw.beans.Exam;
import it.polimi.tiw.beans.Professor;
import it.polimi.tiw.beans.Student;
import it.polimi.tiw.dao.ExamDAO;
import it.polimi.tiw.dao.ExamRegisterDAO;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.ForwardHandler;
import it.polimi.tiw.utils.PathUtils;
import it.polimi.tiw.utils.TemplateHandler;

public class ModifyStudentGrade {
	

/**
 * Servlet implementation class ModifyStudentGrade
 */
@WebServlet("/GoToModifyStudentGrade")
public class GoToModifyStudentGrade extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;

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
		String studentIdString = request.getParameter("studentId");
		
		Student student = null;
		SimpleImmutableEntry<Integer, String> studentExamInfo = null;
		
		
		verifyRequestCommonConstraints(request,response,studentIdString,examIdString,student,studentExamInfo);

		
		
		SimpleImmutableEntry<Student, SimpleImmutableEntry<Integer, String>> studentInfo =  new SimpleImmutableEntry <Student,SimpleImmutableEntry<Integer, String> > (student, studentExamInfo);
		
		
		request.setAttribute("studentInfo", studentInfo);
		ForwardHandler.forward(request, response, PathUtils.pathToGradePageProfessor,  templateEngine);
			
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getParameter("grade");
		String examIdString = request.getParameter("examId");
		String studentIdString = request.getParameter("studentId");
		
		String studentGradeString = request.getParameter("grade");
		int studentGrade;
		
		if(studentGradeString.equals("absent")) {
			studentGrade = 0;
		}
		
		else if(studentGradeString.equals("postponed")) {
			studentGrade = 1;
		}
		
		else if(studentGradeString.equals("to sit again")) {
			studentGrade = 2;
		}
		
		else {

			try {
				studentGrade = Integer.parseInt(studentGradeString);
			}catch (NumberFormatException e) {
				ForwardHandler.forwardToErrorPage(request, response, "Submitted grade is not valid", templateEngine);
				return;
			}
			
			if(studentGrade <18 || studentGrade>31) {
				String error = studentGrade + " is not a valid grade! Please submit a value between 18 and 31 (laude)";
				request.setAttribute("error", error);
				RequestDispatcher dispatcher = request.getRequestDispatcher(PathUtils.pathToGradePageProfessor);
				dispatcher.forward(request, response);
				return;
			}
		
		}
			
	
		Student student = null;
		SimpleImmutableEntry<Integer, String> studentExamInfo = null;
		
		if( !verifyRequestCommonConstraints(request,response,studentIdString,examIdString,student,studentExamInfo))
			return;
		
		
		ExamRegisterDAO examRegisterDAO = new ExamRegisterDAO(connection);
		int examId;
		try {
			examId = Integer.parseInt(examIdString);
		}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen exam id is not a number, when accessing exam details", templateEngine);
			return;
		}
		
		try {
			examRegisterDAO.setGradeByStudentId(student.getId(), examId, studentGrade);
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return;	
		}
		

		response.sendRedirect(getServletContext().getContextPath() + PathUtils.pathToRegisteredStudents);
		
	}
	
	private boolean verifyRequestCommonConstraints(	
			
								HttpServletRequest request, 
								HttpServletResponse response, 
								String studentIdString, 
								String examIdString, 
								Student student, 
								SimpleImmutableEntry<Integer, String> studentExamInfo
								
) throws ServletException, IOException {
		
		Optional<Exam> optExam = null;
		int examId;
		int studentId;
		ExamDAO examDAO = new ExamDAO(connection);
		ExamRegisterDAO examRegisterDAO = new ExamRegisterDAO(connection);
		Exam exam = null;
	
		
		try {
			examId = Integer.parseInt(examIdString);
		}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen exam id is not a number, when accessing exam details", templateEngine);
			return false;
		}
		
		try {
			studentId = Integer.parseInt(studentIdString);
		}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen student id is not a number, when accessing exam details", templateEngine);
			return false;
		}
		
		
		final int id = studentId;
		HttpSession session = request.getSession(false);
		Professor professor = (Professor)session.getAttribute("professor");

		//fetching professor courses to get updated courses list
		try {
			optExam = examDAO.getExamById(examId);
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return false;		
		}

		if(optExam.isEmpty()) {
			ForwardHandler.forwardToErrorPage(request, response, "Exam not existing", templateEngine);
			return false;
		}
		
		exam = optExam.get();
		
		if(professor.getCourseById(exam.getCourseId()).isEmpty()) {
			ForwardHandler.forwardToErrorPage(request, response, "Exam's course not hold by you!", templateEngine);
			return false;
		}
		
		
		try {
			student = examRegisterDAO.getStudentsByExamId(examId).stream().filter(studentBean -> studentBean.getId()== id).findFirst().orElseThrow();
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return false;		
		} catch(NoSuchElementException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Student is not subscribed to this exam!", templateEngine);
			return false;	
		}
		
		
		try {
			studentExamInfo = examRegisterDAO.getExamRegisterByStudentID(studentId, examId);
		} catch(SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return false;		
		}
		
		if(studentExamInfo.getValue().equals("published") || studentExamInfo.getValue().equals("refused") || studentExamInfo.getValue().equals("reported")) {
			ForwardHandler.forwardToErrorPage(request, response, "You can't modify this student's exam data, because exam grade state is not 'inserted'", templateEngine);
			return false;		
		}
	
		return true;	
	}

}

}
