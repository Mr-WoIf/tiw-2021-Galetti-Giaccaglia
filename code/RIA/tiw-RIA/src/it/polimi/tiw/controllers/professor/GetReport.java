package it.polimi.tiw.controllers.professor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.tiw.beans.Report;
import it.polimi.tiw.dao.ReportDAO;
import it.polimi.tiw.simplebeans.ReportInfo;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.ResponseUtils;

/**
 * Servlet implementation class ToHoldCoursePage
 */
@WebServlet("/GetReport")
@MultipartConfig
public class GetReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetReport() {
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
		String reportIdString = request.getParameter("reportID");
		
		int examId;
		int courseId;
		int reportId;
		Report report;
		
		try {
			courseId = Integer.parseInt(courseIdString);
		}catch(NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid course ID related to exam Id");
			return;
		}
		
		try {
			examId = Integer.parseInt(examIdString);
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Exam ID is not a number, when accessing report details");
			return;
		}
		
		try {
			reportId = Integer.parseInt(reportIdString);
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Chosen report ID is not a number, when accesing report details");
			return;
		}
		
		

		ReportDAO reportDAO = new ReportDAO(connection);
		try {
			report = reportDAO.getReport(reportId);
			
		}catch(SQLException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		
		}
		
	//	request.setAttribute("report", report);
	//	request.setAttribute("courseId", courseId);
	//	request.setAttribute("examId", examId);
		
		Gson gson = new GsonBuilder().setDateFormat("yyy/MM/dd").create();
		String json = gson.toJson(new ReportInfo(report, courseId, examId));
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
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
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid course ID related to exam Id");
			return;
		}
		
		try {
			examId =  ((Integer) request.getAttribute("examId"));
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Exam ID is not a number, when accessing report details");
			return;
		}
		
		try {
			reportId = (Integer) request.getAttribute("reportID");
		}catch (NumberFormatException e) {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_BAD_REQUEST, "Chosen report ID is not a number, when accesing report details");
			return;
		}
	
		response.sendRedirect(getServletContext().getContextPath() + "/GoToReport?courseId="+ courseId + "&examId=" + examId + "&reportID=" + reportId);
		
		
		
	}
}
