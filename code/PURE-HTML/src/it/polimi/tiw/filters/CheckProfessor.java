package it.polimi.tiw.filters;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import it.polimi.tiw.beans.Professor;
import it.polimi.tiw.utils.FilterHandler;


/**
 * Servlet Filter implementation class AdminChecker
 */

public class CheckProfessor implements Filter {

	/**
	 * Default constructor.
	 */
	public CheckProfessor() {
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
		
		System.out.print("Professor filter executing ..\n");
		
		FilterHandler.Gen<Professor> professor = new FilterHandler.Gen<Professor>(new Professor());
		
		if(!(FilterHandler.doFilterHandler(request, response, chain, professor)))
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
