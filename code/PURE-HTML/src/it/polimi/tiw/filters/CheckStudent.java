package it.polimi.tiw.filters;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;

import it.polimi.tiw.beans.Student;
import it.polimi.tiw.utils.FilterHandler;
import it.polimi.tiw.utils.ForwardHandler;
import it.polimi.tiw.utils.TemplateHandler;

/**
 * Servlet Filter implementation class AdminChecker
 */

public class CheckStudent implements Filter {

	private TemplateEngine templateEngine;
	/**
	 * Default constructor.
	 */
	public CheckStudent() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		System.out.print("Student filter executing ..\n");		
		
		if(!(FilterHandler.doFilterHandler(request, response, chain, Student.class))) {
			
			String error = "You are not authorized to access this page";
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			ForwardHandler.forwardToErrorPage(httpRequest, httpResponse, error, templateEngine);
			
			return;
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		ServletContext servletContext = fConfig.getServletContext();
		this.templateEngine = TemplateHandler.getEngine(servletContext, ".html");
	}

}
