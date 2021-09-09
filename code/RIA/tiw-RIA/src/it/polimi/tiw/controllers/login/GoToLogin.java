package it.polimi.tiw.controllers.login;

import java.io.IOException;
import java.io.Serial;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.utils.*;
/**
 * Servlet implementation class GoToLoginPage
 */
@WebServlet("/login")  //default mapping
@MultipartConfig
public class GoToLogin extends HttpServlet {

 @Serial
 private static final long serialVersionUID = 1L;
 private TemplateEngine templateEngine;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToLogin() {
        super();
    }

    @Override
    public void init() throws ServletException {
     ServletContext servletContext = getServletContext();
     this.templateEngine = TemplateHandler.getEngine(servletContext, ".html");
    }

 /**
  * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
  */
 @Override
 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 ForwardHandler.forward(request, response, PathUtils.pathToLoginPage, templateEngine);
 }

 /**
  * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
  */
 @Override
 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  doGet(request, response);
 }

}