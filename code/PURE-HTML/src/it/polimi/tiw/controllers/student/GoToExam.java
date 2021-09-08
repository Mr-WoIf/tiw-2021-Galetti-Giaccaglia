package it.polimi.tiw.controllers.student;

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
import it.polimi.tiw.beans.Student;
import it.polimi.tiw.dao.CourseDAO;
import it.polimi.tiw.dao.ExamDAO;
import it.polimi.tiw.dao.ExamRegisterDAO;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.DaoUtils;
import it.polimi.tiw.utils.ForwardHandler;
import it.polimi.tiw.utils.MutablePair;
import it.polimi.tiw.utils.PathUtils;
import it.polimi.tiw.utils.TemplateHandler;

/**
 * Servlet implementation class ToHoldCoursePage
 */
@WebServlet("/GoToExam")
public class GoToExam extends HttpServlet {

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String examIdString = request.getParameter("examId");
		String courseIdString = request.getParameter("courseId");

		int examId;
		int courseId;
		boolean hasBeenPublished = false;
		MutablePair<Student, MutablePair<Integer, String>> studentInfo =  new MutablePair <> (null, null);
		
		if(courseIdString == null) {
			ForwardHandler.forwardToErrorPage(request, response, "Null course ID, when accessing course exams details", templateEngine);
			return;
		}
		
		if(examIdString == null) {
			ForwardHandler.forwardToErrorPage(request, response, "Null exam ID, when accessing course exams details", templateEngine);
			return;
		}

		HttpSession session = request.getSession(false);
		Student student = (Student) session.getAttribute("student");
		int studentId = student.getId();
		CourseDAO courseDAO = new CourseDAO(connection);

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

		if(DaoUtils.verifyRequestCommonConstraints(connection , templateEngine , request , response , studentId , examId , courseId , studentInfo , student))
			return;

		//fetching professor courses to get updated courses list
		try {
			student.setCourses(courseDAO.getCoursesByStudentId(studentId));
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return;		
		}

		if(student.getCourseById(courseId).isEmpty()) {
			ForwardHandler.forwardToErrorPage(request, response, "You are not subscribed to this course!", templateEngine);
			return;
		}

		if(!(studentInfo.getRight().getRight().equals("inserted") || studentInfo.getRight().getRight().equals("not inserted"))) {
			hasBeenPublished = true;
			request.setAttribute("studentInfo", studentInfo);
		}
		
		request.setAttribute("examId", examId);
		request.setAttribute("courseId", courseId);
		request.setAttribute("error", "");
		request.setAttribute("hasBeenPublished", hasBeenPublished);
		
		ForwardHandler.forward(request, response, PathUtils.pathToGradePageStudent, templateEngine);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int examId;
		int courseId;
		ExamDAO examDAO = new ExamDAO(connection);
		ExamRegisterDAO examRegisterDAO = new ExamRegisterDAO(connection);
		Exam exam;
		Optional<Exam> optExam;
		MutablePair<Integer, String> register;
		String examIdString = request.getParameter("examId");
		String courseIdString = request.getParameter("courseId");

		if (examIdString == null) {
			ForwardHandler.forwardToErrorPage(request, response, "Null examId, nothing to refuse here!", templateEngine);
			return;
		}
		
		if(courseIdString == null) {
			ForwardHandler.forwardToErrorPage(request, response, "Null course ID, when accessing course exams details", templateEngine);
			return;
		}
		
		try {
			examId = Integer.parseInt(examIdString);
		} catch (NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response,
					"Chosen exam id is not a number, when refusing exam grade", templateEngine);
			return;
		}
		
		try {
			courseId = Integer.parseInt(courseIdString);
		}catch(NumberFormatException e) {
			ForwardHandler.forwardToErrorPage(request, response, "Invalid course Id related to exam Id", templateEngine);
			return;
		}

		HttpSession session = request.getSession(false);
		Student student = (Student) session.getAttribute("student");

		try {
			optExam = examDAO.getExamById(examId);
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return;
		}

		if (optExam.isEmpty()) {
			ForwardHandler.forwardToErrorPage(request, response, "Exam not existing, nothing to refuse here!",
					templateEngine);
			return;
		}

		exam = optExam.get();

		if (student.getCourseById(exam.getCourseId()).isEmpty()) {
			ForwardHandler.forwardToErrorPage(request, response,
					"You are not subscribed to this exam's course, there's nothing to refuse!", templateEngine);
			return;
		}

		try {
			register = examRegisterDAO.getExamRegisterByStudentID(student.getId(), examId);

		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return;
		}

		if (register == null) {
			ForwardHandler.forwardToErrorPage(request, response,
					"You are not subscribed to this exam, no grade to refuse!", templateEngine);
			return;
		}

		if (register.getRight().equals("not inserted")) {
			ForwardHandler.forwardToErrorPage(request, response,
					"Exam grade hasn't been published yet, nothing to refuse!", templateEngine);
			return;
		}

		if (register.getRight().equals("refused")) {
			ForwardHandler.forwardToErrorPage(request, response, "Exam grade has beeen already refused!",
					templateEngine);
			return;
		}

		if (register.getLeft() < 18) {
			ForwardHandler.forwardToErrorPage(request, response,
					"You were absent or failed this exam, no grade to refuse here!", templateEngine);
			return;
		}

		try {
			examRegisterDAO.setGradeStateByExamID(student.getId(), examId, "refused");
		} catch (SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return;
		}

		response.sendRedirect(getServletContext().getContextPath() + "/GoToExam?courseId="+ courseId + "&examId=" + examId);

	}

}
