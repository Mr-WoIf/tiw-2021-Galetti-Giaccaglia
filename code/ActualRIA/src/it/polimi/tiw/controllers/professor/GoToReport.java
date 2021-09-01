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
import org.thymeleaf.TemplateEngine;

import it.polimi.tiw.beans.Report;
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
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GoToReport() {
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
