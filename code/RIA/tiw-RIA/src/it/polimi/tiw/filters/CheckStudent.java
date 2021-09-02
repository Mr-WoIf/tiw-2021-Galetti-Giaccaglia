package it.polimi.tiw.filters;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.beans.Student;
import it.polimi.tiw.utils.FilterHandler;
import it.polimi.tiw.utils.ResponseUtils;

/**
 * Servlet Filter implementation class AdminChecker
 */

public class CheckStudent implements Filter {
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
			
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			ResponseUtils.handleResponseCreation(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized to access this page");
			return;	
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

}
