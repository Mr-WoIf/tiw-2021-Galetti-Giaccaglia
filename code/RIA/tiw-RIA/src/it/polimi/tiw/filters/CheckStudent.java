package it.polimi.tiw.filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.beans.Student;
import it.polimi.tiw.utils.FilterHandler;
import it.polimi.tiw.utils.ResponseUtils;

/**
 * Servlet Filter implementation class AdminChecker
 */
public class CheckStudent implements Filter {

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		//
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		System.out.print("Student filter executing ..\n");		
		
		if(!(FilterHandler.doFilterHandler(request, response, Student.class))) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			ResponseUtils.handleResponseCreation(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized to access this page");
			return;	
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

}
