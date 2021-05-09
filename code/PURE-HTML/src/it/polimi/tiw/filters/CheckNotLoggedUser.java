package it.polimi.tiw.filters;

import java.io.IOException;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.utils.PathUtils;

/**
 * Servlet Filter implementation class UserFilter
 */

public class CheckNotLoggedUser implements Filter {
	
	
    /**
     * Default constructor. 
     */
    public CheckNotLoggedUser() {
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
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);
		
		System.out.println("not logged user filter activated");
		
		if(session != null) {
			Object user = session.getAttribute("user");
			if(user != null) {
				httpResponse.sendRedirect(request.getServletContext().getContextPath() + PathUtils.goToHomeServletPath);
				return;
			}
		} 

		System.out.println("user is not logged");
		
		chain.doFilter(request, response);
		
	
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

}