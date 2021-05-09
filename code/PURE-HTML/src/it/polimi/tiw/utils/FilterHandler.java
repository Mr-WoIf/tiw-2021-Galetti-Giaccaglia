package it.polimi.tiw.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.beans.Professor;
import it.polimi.tiw.beans.Student;

public class FilterHandler {
	
	
	public static class Gen<T>{   
		  T ob;  
		     
		  public Gen(T o) {   
		    ob = o;   
		  }   
		   
		  // Return ob.   
		  T getob() {   
		    return ob;   
		  }   
		}   
		  
		
	public static TemplateEngine initHandler(FilterConfig filterConfig) throws ServletException {

		ServletContext servletContext = filterConfig.getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		return TemplateHandler.getEngine(servletContext, ".html");
		
	}
	
	public static void forwardHandler(HttpServletRequest request, HttpServletResponse response, String path, TemplateEngine templateEngine) throws ServletException, IOException{
		ServletContext servletContext = request.getServletContext();
		final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process(path, context, response.getWriter());
		
	}
	
	public static <T> boolean doFilterHandler(ServletRequest request, ServletResponse response, FilterChain chain,  FilterHandler.Gen<?> expectedClass)  throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String loginpath = httpRequest.getServletContext().getContextPath() + "/index.html";
		
		// check if the user is a professor
		HttpSession session = httpRequest.getSession();
		Object user = session.getAttribute("user");
		if(( !(expectedClass.getob() instanceof Professor) && (user instanceof Professor)) || !(expectedClass.getob() instanceof Student) && (user instanceof Student)) {
			
		    {
				httpResponse.sendRedirect(loginpath);
				return false;
			}
		    
		}
		
		return true;
	}


}
