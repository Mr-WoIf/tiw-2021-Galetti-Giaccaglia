package it.polimi.tiw.utils;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

public final class ForwardHandler {

	private ForwardHandler(){}
	
	public static void forward(HttpServletRequest request, HttpServletResponse response, String path, TemplateEngine templateEngine) throws ServletException, IOException{
		
		ServletContext servletContext = request.getServletContext();
		WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process(path, ctx, response.getWriter());
		
	}

}
