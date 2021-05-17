package it.polimi.tiw.controllers.login;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;

import it.polimi.tiw.beans.Course;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.controllers.ForwardHandler;
import it.polimi.tiw.dao.CourseDAO;
import it.polimi.tiw.utils.*;


/**
 * Servlet implementation class ToRegisterPage
 */
@WebServlet("/GoToHomePage")
public class GoToHomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToHomePage() {
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
		
		HttpSession session = request.getSession(false);
		User currentUser = (User)session.getAttribute("currentUser");
		
		List<Course> courses;
		CourseDAO courseDAO = new CourseDAO(connection);
		
		String role = currentUser.getRole();
		
		try {
			if(role.equals("professor"))
				courses =  courseDAO.getCoursesByProfessorId(currentUser.getId());
			else
				courses = courseDAO.getCoursesByStudentId(currentUser.getId());
		}catch(SQLException e) {
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), getServletContext(), templateEngine);
			return;	
		}
		
		request.setAttribute("courses", courses);
		ForwardHandler.forward(request, response, PathUtils.pathToHomePage, getServletContext(), templateEngine);
	}
	
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}