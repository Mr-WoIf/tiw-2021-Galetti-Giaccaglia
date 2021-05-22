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

import org.thymeleaf.TemplateEngine;

import it.polimi.tiw.utils.FilterHandler;
import it.polimi.tiw.utils.ForwardHandler;

/**
 * Servlet Filter implementation class UserFilter
 */
public class CheckLoggedUser implements Filter {
	
	private TemplateEngine templateEngine;

    /**
     * Default constructor. 
     */
    public CheckLoggedUser() {
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
		
		if(session != null) {
			Object user = session.getAttribute("user");
			if(user != null && !session.isNew()) {
				chain.doFilter(request, response);
				return;
			}
		} 
		
		String error = ("You are not authorized to access this page");
		ForwardHandler.forwardToErrorPage(httpRequest, httpResponse, error, templateEngine);

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.templateEngine = FilterHandler.initHandler(filterConfig);
		
	}

}