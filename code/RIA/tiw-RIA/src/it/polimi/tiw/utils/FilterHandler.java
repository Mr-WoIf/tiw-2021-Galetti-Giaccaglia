package it.polimi.tiw.utils;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.beans.Professor;
import it.polimi.tiw.beans.Student;

public class FilterHandler {

	private FilterHandler(){}
	
	public static <T> boolean doFilterHandler(ServletRequest request, ServletResponse response, Class<T> expectedClass)  throws IOException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String loginpath = httpRequest.getServletContext().getContextPath() + "/index.html";
		
		// check if the user is a professor
		HttpSession session = httpRequest.getSession();
		
		if(session!=null) {
			Object user = session.getAttribute("user");

			if( user==null ||
					( !(expectedClass.getClass().getTypeName().equals(Professor.class.getTypeName())) && (user instanceof Professor)) || (!(expectedClass.getClass().getTypeName().equals(Student.class.getTypeName())) && (user instanceof Student))) {
				
					httpResponse.sendRedirect(loginpath);
					return false;
				}
		}	    
		
		return true;
	}


}