package it.polimi.tiw.controllers.login;

	import java.io.IOException;
	import java.io.Serial;

	import javax.servlet.ServletException;
	import javax.servlet.annotation.MultipartConfig;
	import javax.servlet.annotation.WebServlet;
	import javax.servlet.http.HttpServlet;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;

	import it.polimi.tiw.utils.*;

	/**
	 * Servlet implementation class GoToLoginPage
	 */
	@WebServlet("/error")  //default mapping
	@MultipartConfig
	public class GoToErrorPage extends HttpServlet {

		@Serial
		private static final long serialVersionUID = 1L;
	    /**
	     * @see HttpServlet#HttpServlet()
	     */

	    public GoToErrorPage() {
	        super();
	    }

		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			ResponseUtils.handleResponseCreation(response, HttpServletResponse.SC_NOT_FOUND, "The page you are looking for doesn't exists");
		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doGet(request, response);
		}

	}
	

