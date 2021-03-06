package it.polimi.tiw.controllers.professor;

import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.thymeleaf.TemplateEngine;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.beans.Exam;
import it.polimi.tiw.beans.Professor;
import it.polimi.tiw.beans.Report;
import it.polimi.tiw.beans.Student;
import it.polimi.tiw.dao.ExamDAO;
import it.polimi.tiw.dao.ExamRegisterDAO;
import it.polimi.tiw.dao.ReportDAO;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.ForwardHandler;
import it.polimi.tiw.utils.MutablePair;
import it.polimi.tiw.utils.PathUtils;
import it.polimi.tiw.utils.SORTING_TYPE;
import it.polimi.tiw.utils.SortingHistory;
import it.polimi.tiw.utils.TemplateHandler;

/**
 * Servlet implementation class ToRegisteredStudentsPage
 */
@WebServlet("/GoToRegisteredStudents")
public class GoToRegisteredStudents extends HttpServlet {

	@Serial
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;
	private SortingHistory sortingHistory;

	@Override
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		this.templateEngine = TemplateHandler.getEngine(servletContext, ".html");
		this.connection = ConnectionHandler.getConnection(servletContext);
		this.sortingHistory = new SortingHistory();
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

		String examIdString = request.getParameter("examId");
		String courseIdString = request.getParameter("courseId");
		String requestType = request.getParameter("requestType");

		if(examIdString == null) {
			ForwardHandler.forwardToErrorPage(request, response, "Null exam ID, when accessing exam details", templateEngine);
			return;
		}
		
		if(courseIdString == null) {
			ForwardHandler.forwardToErrorPage(request, response, "Null course ID, when accessing exam details", templateEngine);
			return;
		}
		
		if(requestType == null) {
			ForwardHandler.forwardToErrorPage(request, response, "Null request type, when accessing exam details", templateEngine);
			return;
		}

		int examId;
		int courseId;

		ExamDAO examDAO = new ExamDAO(connection);
		ExamRegisterDAO examRegisterDAO = new ExamRegisterDAO(connection);
		Exam exam;
		Optional<Exam> optExam;
		List<Student> students;
		LinkedHashMap<Student, MutablePair<Integer, String>> registerMap;
		HttpSession session = request.getSession(false);
		Professor professor;

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

		try {
			professor = (Professor)session.getAttribute("professor");
		}catch(NullPointerException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Your session has expired!", templateEngine);
			return;
		}
		
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
			request.setAttribute("noSubs", true);
			request.setAttribute("courseId", courseId);
			ForwardHandler.forward(request, response, PathUtils.pathToRegisteredStudents, templateEngine);
			return;
		}

		registerMap = students.stream().collect(Collectors.toMap(
                Function.identity(),
                student -> getExamRegister(examRegisterDAO, student.getId(), examId, request, response),
                (student, register) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", student));
                },
                LinkedHashMap::new
        ));
		
       if(requestType.equals("sort")) {
			
			String sortingTypeString = request.getParameter("sortingType");
			SORTING_TYPE sortingType;
			
			if(sortingTypeString == null) {
				ForwardHandler.forwardToErrorPage(request, response, "Null sorting type, when ordering registered students' table", templateEngine);
				return;
			}
			
			try {
				sortingType = SORTING_TYPE.valueOf(sortingTypeString);
			}catch (IllegalArgumentException e) {
				ForwardHandler.forwardToErrorPage(request, response, "Chosen sorting type doesn't exists, when ordering registered students' table", templateEngine);
				return;
			}
			
			sortingHistory.sort(registerMap, sortingType);
			
			
		}
		
		boolean areAllRecorded;
		
		try {
			areAllRecorded = examRegisterDAO.areAllGradesRecorded(examId);
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(),  templateEngine);
			return;
		}
		
		boolean areAllPublished;
		try {
			areAllPublished = examRegisterDAO.areAllGradesPublished(examId);
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response,e.getMessage(), templateEngine);
			return;
		}

		request.setAttribute("noSubs", false);
		request.setAttribute("registerMap", registerMap);
		request.setAttribute("courseId", courseId);
		request.setAttribute("areAllRecorded", areAllRecorded);
		request.setAttribute("areAllPublished", areAllPublished);
		ForwardHandler.forward(request, response, PathUtils.pathToRegisteredStudents, templateEngine);

	}
	
	
	private MutablePair<Integer, String> getExamRegister(ExamRegisterDAO examRegisterDAO, int studentId, int examId, HttpServletRequest request, HttpServletResponse response){

		try {
			return examRegisterDAO.getExamRegisterByStudentID(studentId, examId);
		} catch (SQLException e) {
			try {
				ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return new MutablePair<>(-1 , "fail");

		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String examIdString = request.getParameter("examId");
		String requestType = request.getParameter("requestType");
		String courseIdString = request.getParameter("courseId");
		
		if(requestType == null) {
			ForwardHandler.forwardToErrorPage(request, response, "Null request type, when modifying exams' grade state", templateEngine);
			return;
		}

		if(examIdString == null) {
			ForwardHandler.forwardToErrorPage(request, response, "Null exam ID, when modifying exams' grade state", templateEngine);
			return;
		}
		
		if(courseIdString == null) {
			ForwardHandler.forwardToErrorPage(request, response, "Null course ID, when modifying exams' grade state", templateEngine);
			return;
		}

		int examId;
		int courseId;
		ExamDAO examDAO = new ExamDAO(connection);
		Exam exam;
		Optional<Exam> optExam;


		try {
			examId = Integer.parseInt(examIdString);
		}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen exam ID is not a number, when modifying exams' grade state", templateEngine);
			return;
		}
		
		try {
			courseId = Integer.parseInt(courseIdString);
		}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen course ID is not a number, when modifying exams' grade state", templateEngine);
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
		
		ExamRegisterDAO examRegisterDAO = new ExamRegisterDAO(connection);

		if(requestType.equals("publish")) {
			
			boolean areAllPublished;
			
			try {
				areAllPublished = examRegisterDAO.areAllGradesPublished(examId);
			} catch (SQLException e) {
				ForwardHandler.forwardToErrorPage(request, response, e.getMessage(),  templateEngine);
				return;
			}
			
			if(areAllPublished) {
				ForwardHandler.forwardToErrorPage(request, response, "You are not authorized to publish these grades!",  templateEngine);
				return;
			}
			
			try {	
				examRegisterDAO.publishGradeByExamID(examId);
				response.sendRedirect(getServletContext().getContextPath() + "/GoToRegisteredStudents?courseId="+ courseId + "&examId=" + examId + "&requestType='load");

			}catch (SQLException e) {
				
				ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			}

		}else if(requestType.equals("record")) {
			
			boolean areAllRecorded;
			
			try {
				areAllRecorded = examRegisterDAO.areAllGradesRecorded(examId);
			} catch (SQLException e) {
				ForwardHandler.forwardToErrorPage(request, response, e.getMessage(),  templateEngine);
				return;
			}
			
			if(areAllRecorded ) {
				ForwardHandler.forwardToErrorPage(request, response, "You are not authorized to record these grades!",  templateEngine);
				return;
			}
			
			try {
				
				ReportDAO reportDAO = new ReportDAO(connection);
				Report report = reportDAO.createReport(examId);
				RequestDispatcher rd = request.getRequestDispatcher("GoToReport");
				request.setAttribute("reportID", report.getReportId());
				request.setAttribute("courseId", courseId);
				request.setAttribute("examId", examId);
				rd.forward(request, response);
				
			}catch (SQLException e) {
				ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			}
			
		}else{
			ForwardHandler.forwardToErrorPage(request, response, "Invalid request type", templateEngine);
		}
		
	}

}