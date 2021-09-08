package it.polimi.tiw.controllers.login;


import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.beans.Course;
import it.polimi.tiw.beans.Professor;
import it.polimi.tiw.beans.Student;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.CourseDAO;
import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.utils.*;

/**
 * Servlet implementation class ToRegisterPage
 */
@WebServlet("/GoToHomePage")
public class GoToHomePage extends HttpServlet {

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
		
		HttpSession session = request.getSession(false);
		User currentUser = (User)session.getAttribute("user");
		List<Course> courses;
		CourseDAO courseDAO = new CourseDAO(connection);
		UserDAO userDAO = new UserDAO(connection);
		int id = currentUser.getId();
		String role = currentUser.getRole();
		
		try {
			if(role.equals("professor")) {
				courses =  courseDAO.getCoursesByProfessorId(id);
				Professor professor = userDAO.findProfessorById(id);
				session.setAttribute("professor", professor);
			}
				
			else {
				courses = courseDAO.getCoursesByStudentId(id);
				Student student = userDAO.findStudentById(id);
				session.setAttribute("student", student);
			}
			
		}catch(SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, "An error occured!", templateEngine);
			return;	
		}

		request.setAttribute("courses", courses);
		ForwardHandler.forward(request, response, PathUtils.pathToHomePage, templateEngine);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}