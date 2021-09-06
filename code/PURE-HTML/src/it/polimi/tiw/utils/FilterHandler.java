package it.polimi.tiw.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.beans.Professor;
import it.polimi.tiw.beans.Student;

public class FilterHandler {
	

		
	public static TemplateEngine initHandler(FilterConfig filterConfig) throws ServletException {

		ServletContext servletContext = filterConfig.getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		return TemplateHandler.getEngine(servletContext, ".html");
		
	}
	
	public static <T> boolean doFilterHandler(ServletRequest request, ServletResponse response, FilterChain chain, Class<T> expectedClass)  throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		// check if the user is a professor
		HttpSession session = httpRequest.getSession();
		
		if(session!=null) {
			Object user = session.getAttribute("user");
			
			
			if( user==null ||
					( !(expectedClass.getClass().getTypeName().equals(Professor.class.getTypeName())) && (user instanceof Professor)) || (!(expectedClass.getClass().getTypeName().equals(Student.class.getTypeName())) && (user instanceof Student))) 
					return false;
				
		}	    
		
		return true;
	}


}
