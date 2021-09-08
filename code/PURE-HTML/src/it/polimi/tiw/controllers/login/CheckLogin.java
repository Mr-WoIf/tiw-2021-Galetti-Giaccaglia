package it.polimi.tiw.controllers.login;

import java.io.IOException;
import java.io.Serial;
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

import it.polimi.tiw.beans.*;
import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.utils.*;

/**
 * Servlet implementation class Login
 */
@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
	
	@Serial
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;

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
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String idParam = request.getParameter("id");
		String passwordParam = request.getParameter("password");
		String password;
		String warningAttribute = "warning";
		int id;
		
		if(idParam == null){
			request.setAttribute(warningAttribute, "Null id");
			ForwardHandler.forward(request, response, PathUtils.pathToLoginPage, templateEngine);
			return;
		}
		
		if(passwordParam == null){
			request.setAttribute(warningAttribute, "Null password");
			ForwardHandler.forward(request, response, PathUtils.pathToLoginPage, templateEngine);
			return;
		}
		
		if(idParam.isEmpty()) {
			request.setAttribute(warningAttribute, "Empty id field!");
			ForwardHandler.forward(request, response, PathUtils.pathToLoginPage, templateEngine);
			return;
		}
		
		if(passwordParam.isEmpty()) {
			request.setAttribute(warningAttribute, "Empty password field");
			ForwardHandler.forward(request, response, PathUtils.pathToLoginPage, templateEngine);
			return;
		}
		
		try {
			id = Integer.parseInt(idParam);
			password = passwordParam;

			if (id < 10600000 || id > 10800000) {
				request.setAttribute(warningAttribute, "The id is invalid, make sure to use the one provided");
				ForwardHandler.forward(request, response, PathUtils.pathToLoginPage, templateEngine);
				return;
			}
			
		} catch (NumberFormatException e) {
			request.setAttribute(warningAttribute, "Parameter id with format number is required");
			ForwardHandler.forward(request, response, PathUtils.pathToLoginPage, templateEngine);
			return;
		}

		UserDAO userDAO = new UserDAO(connection);
		Optional<User> optUser;
		User user;
		
		try {
			optUser = userDAO.findUser(id, password);
			
		} catch (SQLException e) {
			
			ForwardHandler.forwardToErrorPage(request, response, e.getMessage(), templateEngine);
			return;
		}
		
		if(optUser.isEmpty()) {
			request.setAttribute(warningAttribute, "id or password incorrect!");
			ForwardHandler.forward(request, response, PathUtils.pathToLoginPage, templateEngine);
			return;
		}
		
		user = optUser.get();
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		response.sendRedirect(getServletContext().getContextPath() + PathUtils.goToHomeServletPath);		
	}

}