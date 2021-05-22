package it.polimi.tiw.controllers.student;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.AbstractMap.SimpleImmutableEntry;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;

import it.polimi.tiw.beans.Exam;
import it.polimi.tiw.beans.Student;
import it.polimi.tiw.dao.ExamDAO;
import it.polimi.tiw.dao.ExamRegisterDAO;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.ForwardHandler;
import it.polimi.tiw.utils.PathUtils;
import it.polimi.tiw.utils.TemplateHandler;



	/**
	 * Servlet implementation class ToHoldCoursePage
	 */
	@WebServlet("/GoToExam")
	public class GoToExam extends HttpServlet {
		private static final long serialVersionUID = 1L;
		private TemplateEngine templateEngine;
		private Connection connection;

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

			if(examIdString == null) {
				ForwardHandler.forwardToErrorPage(request, response, "Null examId, when accessing exam details", templateEngine);
			}

			int examId;

			ExamDAO examDAO = new ExamDAO(connection);
			ExamRegisterDAO examRegisterDAO = new ExamRegisterDAO(connection);
			Exam exam = null;
			Optional<Exam> optExam = null;
			
			SimpleImmutableEntry<Integer, String> register = null;

			try {
				examId = Integer.parseInt(examIdString);
			}catch (NumberFormatException e) {
				ForwardHandler.forwardToErrorPage(request, response, "Chosen exam id is not a number, when accessing exam details", templateEngine);
				return;
			}

			HttpSession session = request.getSession(false);
			Student student = (Student)session.getAttribute("student");

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
			
			if(student.getCourseById(exam.getCourseId()).isEmpty()) {
				ForwardHandler.forwardToErrorPage(request, response, "Exam's course not hold by you!", templateEngine);
				return;
			}
			
			
			try {
				register = examRegisterDAO.getExamRegisterByStudentID(student.getId(), examId);
				
			}catch (SQLException e) {
				ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
				return;		
			}
			
			
			request.setAttribute("courseName", student.getCourseById(exam.getCourseId()).get().getName());
			request.setAttribute("exam", exam);
			request.setAttribute("register", register);
			ForwardHandler.forward(request, response, PathUtils.pathToCoursePage, templateEngine);

		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// TODO Auto-generated method stub
			doGet(request, response);
		}

	}

