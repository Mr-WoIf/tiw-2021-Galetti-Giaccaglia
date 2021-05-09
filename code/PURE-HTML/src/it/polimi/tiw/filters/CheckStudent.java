package it.polimi.tiw.filters;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import it.polimi.tiw.beans.Student;
import it.polimi.tiw.utils.FilterHandler;


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
		
		FilterHandler.Gen<Student> student = new FilterHandler.Gen<Student>(new Student());
		
		if(!(FilterHandler.doFilterHandler(request, response, chain, student)))
			return;
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
