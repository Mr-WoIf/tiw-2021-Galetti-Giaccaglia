package it.polimi.tiw.controllers.login;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.tiw.beans.*;
import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.utils.*;

/**
 * Servlet implementation class Login
 */
@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckLogin() {
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
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String id_param = request.getParameter("id");
		String password_param = request.getParameter("password");
		int id;
		int password;
		
		if(id_param == null){
			request.setAttribute("warning", "Null id");		
			forward(request, response, PathUtils.pathToLoginPage);
			return;
		}
		
		if(password_param == null){
			request.setAttribute("warning", "Null password");		
			forward(request, response, PathUtils.pathToLoginPage);
			return;
		}
		
		if(id_param.isEmpty()) {
			request.setAttribute("warning", "Empty id field!");		
			forward(request, response, PathUtils.pathToLoginPage);
			return;
		}
		
		if(password_param.isEmpty()) {
			request.setAttribute("warning", "Empty email field");		
			forward(request, response, PathUtils.pathToLoginPage);
			return;
		}
		
		try {
			id = Integer.parseInt(id_param);
			password = Integer.parseInt(password_param);
			

			if (password < 10600000 || password > 10800000) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The id is invalid, make sure to use the one provided");
				return;
			}
			
		} catch (NumberFormatException e) {
			request.setAttribute( "warning", "Parameter id with format number is required");
			forward(request, response, PathUtils.pathToLoginPage);
			return;
		}
		
		
			
		UserDAO userDAO = new UserDAO(connection);
		Optional<User> optUser = null;
		User user = null;
		
		try {
			
			optUser = userDAO.findUser(id,  password);
			
		} catch (SQLException e) {
			
			forwardToErrorPage(request, response, e.getMessage());
			return;
		}
		
		if(optUser.isEmpty()) {
			request.setAttribute("warning", "Email or password incorrect!");
			forward(request, response, PathUtils.pathToLoginPage);
			return;
		}
		
		user = optUser.get();
		HttpSession session = request.getSession();
		session.setAttribute("currentUser", user);
		response.sendRedirect(getServletContext().getContextPath() + PathUtils.goToHomeServletPath);		
	}
	
	private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException{
		
		request.setAttribute("error", error);
		forward(request, response, PathUtils.pathToErrorPage);
		return;
	}
	
	private void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException{
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process(path, ctx, response.getWriter());
		
	}
}