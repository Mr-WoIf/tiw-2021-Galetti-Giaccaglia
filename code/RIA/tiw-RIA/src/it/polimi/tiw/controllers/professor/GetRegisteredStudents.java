package it.polimi.tiw.controllers.professor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.tiw.beans.Exam;
import it.polimi.tiw.beans.Professor;
import it.polimi.tiw.beans.Report;
import it.polimi.tiw.beans.Student;
import it.polimi.tiw.dao.ExamDAO;
import it.polimi.tiw.dao.ExamRegisterDAO;
import it.polimi.tiw.dao.ReportDAO;
import it.polimi.tiw.simplebeans.ExamRegisteredStudents;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.MutablePair;
import it.polimi.tiw.utils.ResponseUtils;


/**
 * Servlet implementation class ToRegisteredStudentsPage
 */
@WebServlet("/GetRegisteredStudents")
@MultipartConfig
public class GetRegisteredStudents extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetRegisteredStudents() {
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
		String requestType = request.getParameter("requestType");

		if(examIdString == null) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Missing exam ID, when accessing exam details");
			return;
		}
		
		if(courseIdString == null) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Missing course ID, when accessing exam details");
			return;
		}
		
		if(requestType == null) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Missing request type, when accessing exam details");
			return;
		}
		

		int examId;
		int courseId;

		ExamDAO examDAO = new ExamDAO(connection);
		ExamRegisterDAO examRegisterDAO = new ExamRegisterDAO(connection);
		Exam exam = null;
		Optional<Exam> optExam = null;
		List<Student> students = null;

		LinkedHashMap<Student, MutablePair<Integer, String>> registerMap = null;

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
		

		HttpSession session = request.getSession(false);
		Professor professor;
		
		try {
			professor = (Professor)session.getAttribute("professor");
		}catch(NullPointerException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "Your session has expired!");
			return;
		}
		
		if(professor == null) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "Your session has expired!");
			return;
		}

		//fetching professor courses to get updated courses list
		try {
			optExam = examDAO.getExamById(examId);
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}

		if(optExam.isEmpty()) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_NOT_FOUND, "Exam not existing");
			return;
		}

		exam = optExam.get();

		if(professor.getCourseById(exam.getCourseId()).isEmpty()) {
			
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "Exam's course not hold by you!");
			return;
		}

		try {
			students = examRegisterDAO.getStudentsByExamId(examId);
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}
		
		
	//	request.setAttribute("examId", examId);
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setDateFormat("yyy/MM/dd").create();
		
		if(students.size()==0) {
		//	request.setAttribute("noSubs", true);  //TODO HANDLE NO SUBS HTML PAGE
		//	request.setAttribute("courseId", courseId);
			
			String json = gson.toJson(new ExamRegisteredStudents(examId, true, courseId, false, false, new LinkedHashMap<Student, MutablePair<Integer, String>>()));
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json);
			
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
		
		
		
		
		boolean areAllRecorded;
		
		try {
			areAllRecorded = examRegisterDAO.areAllGradesRecorded(examId);
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}
		
		boolean areAllPublished;
		try {
			areAllPublished = examRegisterDAO.areAllGradesPublished(examId);
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}

	//	request.setAttribute("noSubs", false);
    //	request.setAttribute("registerMap", registerMap);
	//	request.setAttribute("courseId", courseId);
	//	request.setAttribute("areAllRecorded", areAllRecorded);
	//	request.setAttribute("areAllPublished", areAllPublished);
		
	
		String json = gson.toJson(new ExamRegisteredStudents(examId, false, courseId, areAllRecorded, areAllPublished, registerMap));
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);

	}
	
	
	private MutablePair<Integer, String> getExamRegister(ExamRegisterDAO examRegisterDAO, int studentId, int examId, HttpServletRequest request, HttpServletResponse response){


		try {
			return examRegisterDAO.getExamRegisterByStudentID(studentId, examId);
		} catch (SQLException e) {
			try {
				ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return new MutablePair<Integer, String>(-1, "fail");	
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		String examIdString = request.getParameter("examId");
		String requestType = request.getParameter("requestType");
		String courseIdString = request.getParameter("courseId");
		
		
		if(requestType == null) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Missing request type, when modifying exams' grade state");
			return;
		}

		if(examIdString == null) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Missing exam ID, when modifying exams' grade state");
			return;
		}
		
		if(courseIdString == null) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Missing course ID, when modifying exams' grade state");
			return;
		}

		int examId;
		int courseId;

		ExamDAO examDAO = new ExamDAO(connection);
		Exam exam = null;
		Optional<Exam> optExam = null;


		try {
			examId = Integer.parseInt(examIdString);
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Chosen exam ID is not a number, when modifying exams' grade state");
			return;
		}
		
		try {
			courseId = Integer.parseInt(courseIdString);
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Chosen course ID is not a number, when modifying exams' grade state");
			return;
		}

		HttpSession session = request.getSession(false);
		Professor professor = (Professor)session.getAttribute("professor");
		
		if(professor == null) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "Your session has expired!");
			return;
		}

		//fetching professor courses to get updated courses list
		try {
			optExam = examDAO.getExamById(examId);
		} catch (SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;	
		}

		if(optExam.isEmpty()) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_NOT_FOUND, "Exam not existing");
			return;
		}

		exam = optExam.get();

		if(professor.getCourseById(exam.getCourseId()).isEmpty()) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "Exam's course not hold by you!");
			return;
		}
		
		ExamRegisterDAO examRegisterDAO = new ExamRegisterDAO(connection);

		if(requestType.equals("publish")) {
			
			boolean areAllPublished;
			
			try {
				areAllPublished = examRegisterDAO.areAllGradesPublished(examId);
			} catch (SQLException e) {
				ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
				return;	
			}
			
			if(areAllPublished) {
				ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized to publish these grades!");
				return;
			}
			
			try {	
				examRegisterDAO.publishGradeByExamID(examId);
				response.sendRedirect(getServletContext().getContextPath() + "/GetRegisteredStudents?courseId="+ courseId + "&examId=" + examId + "&requestType='load");
				return;
				
			}catch (SQLException e) {
				ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
				return;	
				
			}
		}else if(requestType.equals("record")) {
			
			boolean areAllRecorded;
			
			try {
				areAllRecorded = examRegisterDAO.areAllGradesRecorded(examId);
			} catch (SQLException e) {
				ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
				return;	
			}
			
			if(areAllRecorded ) {
				ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized to record these grades!");
				return;
			}
			
			try {
				
				ReportDAO reportDAO = new ReportDAO(connection);
				Report report = reportDAO.createReport(examId);
				

				RequestDispatcher rd = request.getRequestDispatcher("GetReport");
				request.setAttribute("reportID", report.getReportId());
				request.setAttribute("courseId", courseId);
				request.setAttribute("examId", examId);
				rd.forward(request, response);
				
			}catch (SQLException e) {
				ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
				return;	
			}
			
		}else{
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request type");
			return;	
		}
		
	}

}