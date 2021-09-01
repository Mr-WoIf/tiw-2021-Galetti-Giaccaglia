package it.polimi.tiw.controllers.login;

	import java.io.IOException;

	import javax.servlet.ServletContext;
	import javax.servlet.ServletException;
	import javax.servlet.annotation.WebServlet;
	import javax.servlet.http.HttpServlet;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;

	import org.thymeleaf.TemplateEngine;

	import it.polimi.tiw.utils.*;
	/**
	 * Servlet implementation class GoToLoginPage
	 */
	@WebServlet("/error")  //default mapping
	public class GoToErrorPage extends HttpServlet {
		private static final long serialVersionUID = 1L;
		private TemplateEngine templateEngine;
	    /**
	     * @see HttpServlet#HttpServlet()
	     */
	    public GoToErrorPage() {
	        super();
	    }

	    
	    @Override
	    public void init() throws ServletException {
	    	ServletContext servletContext = getServletContext();
			this.templateEngine = TemplateHandler.getEngine(servletContext, ".html");
	    }

	    
		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			String error = "ERROR 404 : the page you are looking for doesn't exists";
			
			ForwardHandler.forwardToErrorPage(request, response, error, templateEngine);
			return;
		}

		
		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// TODO Auto-generated method stub
			doGet(request, response);
		}
		

	}
	

