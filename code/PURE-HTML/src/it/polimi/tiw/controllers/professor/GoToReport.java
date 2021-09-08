package it.polimi.tiw.controllers.professor;

import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import org.thymeleaf.TemplateEngine;

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
import it.polimi.tiw.dao.CourseDAO;
import it.polimi.tiw.dao.ExamDAO;
import it.polimi.tiw.dao.ReportDAO;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.ForwardHandler;
import it.polimi.tiw.utils.PathUtils;
import it.polimi.tiw.utils.TemplateHandler;

/**
 * Servlet implementation class ToHoldCoursePage
 */
@WebServlet("/GoToReport")
public class GoToReport extends HttpServlet {

	@Serial
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;

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
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String examIdString = request.getParameter("examId");
		String courseIdString = request.getParameter("courseId");
		String reportIdString = request.getParameter("reportID");
		int examId;
		int courseId;
		int reportId;
		Report report;
		
		try {
			courseId = Integer.parseInt(courseIdString);
		}catch(NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Invalid course ID related to exam Id", templateEngine);
			return;
		}
		
		try {
			examId = Integer.parseInt(examIdString);
		}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Exam ID is not a number, when accessing report details", templateEngine);
			return;
		}
		
		try {
			reportId = Integer.parseInt(reportIdString);
			}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen report ID is not a number, when accesing report details", templateEngine);
			return;
		}
		
		HttpSession session = request.getSession(false);
		Professor currentProfessor = (Professor)session.getAttribute("professor");
		
		if(currentProfessor==null) {
			ForwardHandler.forwardToErrorPage(request, response, "You are not authorized to perform this action!", templateEngine);
			return;		
		}
			
		CourseDAO courseDAO = new CourseDAO(connection);

		//fetching professor courses to get updated courses list
		try {
			currentProfessor.setCourses(courseDAO.getCoursesByProfessorId(currentProfessor.getId()));
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, "There has been an error finding courses by professor ID", templateEngine);
			return;		
		}
		
		try {
			if(courseDAO.isCourseIdNotValid(courseId)) {
				ForwardHandler.forwardToErrorPage(request, response,  "Course id doesn't match any currently active course" , templateEngine);
				return;
			}
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return;		
		}

		if(currentProfessor.getCourseById(courseId).isEmpty()) {
			ForwardHandler.forwardToErrorPage(request, response, "Course is not held by you", templateEngine);
			return;
		}

		ExamDAO examDAO = new ExamDAO(connection);
		Optional<Exam> optExam;
		Exam exam;
		
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
				if(currentProfessor.getCourseById(exam.getCourseId()).isEmpty()) {
				ForwardHandler.forwardToErrorPage(request, response, "Exam's course not hold by you!", templateEngine);
				return;
			}

		ReportDAO reportDAO = new ReportDAO(connection);

		try {
			report = reportDAO.getReport(reportId);
			
		}catch(SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return;
		}

		request.setAttribute("report", report);
		request.setAttribute("courseId", courseId);
		request.setAttribute("examId", examId);
		
		ForwardHandler.forward(request, response, PathUtils.pathToReportPage, templateEngine);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int examId;
		int courseId;
		int reportId;
		
		try {
			courseId = (Integer) request.getAttribute("courseId");
		}catch(NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Invalid course ID related to exam Id", templateEngine);
			return;
		}
		
		try {
			examId =  ((Integer) request.getAttribute("examId"));
		}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Exam ID is not a number, when accessing report details", templateEngine);
			return;
		}
		
		try {
			reportId = (Integer) request.getAttribute("reportID");
			}catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Chosen report ID is not a number, when accesing report details", templateEngine);
			return;
		}
	
		response.sendRedirect(getServletContext().getContextPath() + "/GoToReport?courseId="+ courseId + "&examId=" + examId + "&reportID=" + reportId);

	}
}
