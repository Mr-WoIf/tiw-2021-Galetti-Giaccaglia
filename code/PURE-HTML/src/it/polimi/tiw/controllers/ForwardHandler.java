package it.polimi.tiw.controllers;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.tiw.utils.PathUtils;

public final class ForwardHandler {
	
	public static void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response, String error, ServletContext servletContext, TemplateEngine templateEngine) throws ServletException, IOException{
		
		request.setAttribute("error", error);
		forward(request, response, PathUtils.pathToErrorPage, servletContext, templateEngine);
		return;
	}
	
	public static void forward(HttpServletRequest request, HttpServletResponse response, String path, ServletContext servletContext, TemplateEngine templateEngine) throws ServletException, IOException{
		
		WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process(path, ctx, response.getWriter());
		
	}

}
