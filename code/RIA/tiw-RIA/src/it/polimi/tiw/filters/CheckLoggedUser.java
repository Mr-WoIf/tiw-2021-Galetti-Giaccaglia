package it.polimi.tiw.filters;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.utils.ResponseUtils;

/**
 * Servlet Filter implementation class UserFilter
 */
public class CheckLoggedUser implements Filter {

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
	
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);
		
		if(session != null) {
			Object user = session.getAttribute("user");
			if(user != null && !session.isNew()) {
				chain.doFilter(request, response);
				return;
			}
		} 
		ResponseUtils.handleResponseCreation(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized to access this page");

	}

}