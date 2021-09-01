package it.polimi.tiw.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class ResponseUtils {

	public static void handleResponseCreation(HttpServletResponse httpResponse, int responseCode, String message) throws IOException {
		httpResponse.setStatus(responseCode);
		httpResponse.getWriter().println(message);
	}
}
