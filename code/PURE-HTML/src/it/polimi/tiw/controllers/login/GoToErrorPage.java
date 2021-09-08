package it.polimi.tiw.controllers.login;

	import java.io.IOException;
	import java.io.Serial;
	import org.thymeleaf.TemplateEngine;

	import javax.servlet.ServletContext;
	import javax.servlet.ServletException;
	import javax.servlet.annotation.WebServlet;
	import javax.servlet.http.HttpServlet;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;

	import it.polimi.tiw.utils.*;

	/**
	 * Servlet implementation class GoToLoginPage
	 */
	@WebServlet("/error")
	public class GoToErrorPage extends HttpServlet {

		@Serial
		private static final long serialVersionUID = 1L;
		private TemplateEngine templateEngine;

	    @Override
	    public void init() throws ServletException {
	    	ServletContext servletContext = getServletContext();
			this.templateEngine = TemplateHandler.getEngine(servletContext, ".html");
	    }

		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

			String path = request.getRequestURI();
			System.out.print(path);
			String error = " the page you are looking for doesn't exists";
			ForwardHandler.forwardToErrorPage(request, response, error, templateEngine);
		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doGet(request, response);
		}
		

	}
	