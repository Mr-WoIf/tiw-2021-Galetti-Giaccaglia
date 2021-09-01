package it.polimi.tiw.controllers.login;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import it.polimi.tiw.beans.*;
import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.utils.*;
import packets.UserPacket;

/**
 * Servlet implementation class Login
 */
@WebServlet("/CheckLogin")
@MultipartConfig

public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

       
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
		String password;
		
		if(id_param == null){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Id can't be null");
			return;
		}
		
		if(password_param == null){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);			
			response.getWriter().println("Password can't be null");
			return;
		}
		
		if(id_param.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Id can't be empty");
			return;
		}
		
		if(password_param.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Password can't be empty");
			return;
		}
		
		try {
			id = Integer.parseInt(id_param);
			password = password_param;
			

			if (id < 10600000 || id > 10800000) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("The id is invalid, make sure to use the one provided");
				return;
			}
			
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter id with format number is required");
			return;
		}
		
		
			
		UserDAO userDAO = new UserDAO(connection);
		Optional<User> optUser = null;
		User user = null;
		
		try {
			
			optUser = userDAO.findUser(id, password);
			
		} catch (SQLException e) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Internal server error, retry later");
			return;
		}
		
		if(optUser.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Incorrect credentials");
			return;
		}
		
			
		user = optUser.get();
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		
		String userPacket = new Gson().toJson(new UserPacket(user.getName(), user.getId(), user.getRole()));
		
		response.setStatus(HttpServletResponse.SC_OK);;
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(userPacket);
	}
	

}