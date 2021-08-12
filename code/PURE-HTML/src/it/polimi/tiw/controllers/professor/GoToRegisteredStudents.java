package it.polimi.tiw.controllers.professor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.stream.Collectors;

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
import it.polimi.tiw.beans.Report;
import it.polimi.tiw.beans.Student;
import it.polimi.tiw.dao.ExamDAO;
import it.polimi.tiw.dao.ExamRegisterDAO;
import it.polimi.tiw.dao.ReportDAO;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.ForwardHandler;
import it.polimi.tiw.utils.PathUtils;
import it.polimi.tiw.utils.TemplateHandler;



/**
 * Servlet implementation class ToRegisteredStudentsPage
 */
@WebServlet("/GoToRegisteredStudents")
public class GoToRegisteredStudents extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GoToRegisteredStudents() {
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

		if(examIdString == null) {
			ForwardHandler.forwardToErrorPage(request, response, "Null exam ID, when accessing exam details", templateEngine);
		}
		
		if(courseIdString == null) {
			ForwardHandler.forwardToErrorPage(request, response, "Null course ID, when accessing exam details", templateEngine);
		}
		
		

		int examId;
		int courseId;

		ExamDAO examDAO = new ExamDAO(connection);
		ExamRegisterDAO examRegisterDAO = new ExamRegisterDAO(connection);
		Exam exam = null;
		Optional<Exam> optExam = null;
		List<Student> students = null;

		Map<Student, SimpleImmutableEntry<Integer, String>> registerMap = null;

		try {
			examId = Integer.parseInt(examIdString);
		}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen exam id is not a number, when accessing exam details", templateEngine);
			return;
		}
		
		try {
			courseId = Integer.parseInt(courseIdString);
		}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen exam's course ID is not a number, when accessing exam details", templateEngine);
			return;
		}
		

		HttpSession session = request.getSession(false);
		Professor professor = (Professor)session.getAttribute("professor");
		if(professor == null) {
			ForwardHandler.forwardToErrorPage(request, response, "Your session has expired!", templateEngine);
			return;
		}

		//fetching professor courses to get updated courses list
		try {
			optExam = examDAO.getExamById(examId);
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return;		
		}

		if(optExam.isEmpty()) {
			ForwardHandler.forwardToErrorPage(request, response, "Exam not existing", templateEngine);
			return;
		}

		exam = optExam.get();

		if(professor.getCourseById(exam.getCourseId()).isEmpty()) {
			ForwardHandler.forwardToErrorPage(request, response, "Exam's course not hold by you!", templateEngine);
			return;
		}

		try {
			students = examRegisterDAO.getStudentsByExamId(examId);
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return;		
		}
		
		
		request.setAttribute("examId", examId);
		
		
		if(students.size()==0) {
			request.setAttribute("noSubs", true);  //TODO HANDLE NO SUBS HTML PAGE
			request.setAttribute("courseId", courseId);
			ForwardHandler.forward(request, response, PathUtils.pathToRegisteredStudents, templateEngine);
			return;
		}
		
		System.out.println(courseId);

		registerMap = students.stream()
				.collect(Collectors.toMap(
						student -> student, student -> getExamRegister(examRegisterDAO, student.getId(), examId, request, response)));

		request.setAttribute("noSubs", false);
		request.setAttribute("registerMap", registerMap);
		request.setAttribute("courseId", courseId);
		
		ForwardHandler.forward(request, response, PathUtils.pathToRegisteredStudents, templateEngine);


	}

	private SimpleImmutableEntry<Integer, String> getExamRegister(ExamRegisterDAO examRegisterDAO, int studentId, int examId, HttpServletRequest request, HttpServletResponse response){


		try {
			return examRegisterDAO.getExamRegisterByStudentID(studentId, examId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			} catch (ServletException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return new SimpleImmutableEntry<Integer, String>(-1, "fail");	

		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		String examIdString = request.getParameter("examId");
		String requestType = request.getParameter("requestType");

		if(examIdString == null) {
			ForwardHandler.forwardToErrorPage(request, response, "Null examId, when accessing exam details", templateEngine);
		}

		int examId;

		ExamDAO examDAO = new ExamDAO(connection);
		Exam exam = null;
		Optional<Exam> optExam = null;


		try {
			examId = Integer.parseInt(examIdString);
		}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen exam id is not a number, when accessing exam details", templateEngine);
			return;
		}

		HttpSession session = request.getSession(false);
		Professor professor = (Professor)session.getAttribute("professor");

		//fetching professor courses to get updated courses list
		try {
			optExam = examDAO.getExamById(examId);
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return;		
		}

		if(optExam.isEmpty()) {
			ForwardHandler.forwardToErrorPage(request, response, "Exam not existing", templateEngine);
			return;
		}

		exam = optExam.get();

		if(professor.getCourseById(exam.getCourseId()).isEmpty()) {
			ForwardHandler.forwardToErrorPage(request, response, "Exam's course not hold by you!", templateEngine);
			return;
		}

		if(requestType.equals("publish")) {
			try {
				
				ExamRegisterDAO examRegisterDAO = new ExamRegisterDAO(connection);
				examRegisterDAO.publishGradeByExamID(examId);
				response.sendRedirect(getServletContext().getContextPath() + PathUtils.pathToRegisteredStudents);
				
			}catch (SQLException e) {
				
				ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
				return;
				
			}
		}else if(requestType.equals("record")) {

			try {
				
				ReportDAO reportDAO = new ReportDAO(connection);
				Report report = reportDAO.createReport(examId);
				
				session.setAttribute("report", report);
				response.sendRedirect(getServletContext().getContextPath() + PathUtils.pathToReportPage);
				
			}catch (SQLException e) {
				ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
				return;
			} 
			
		}else{
			ForwardHandler.forwardToErrorPage(request, response, "Invalid POST method", templateEngine);
			return;
		}


		

	}

}