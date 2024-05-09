package com.shinhan.controller.api;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.shinhan.service.UserService;
import com.shinhan.utils.JSONParsing;

@WebServlet("/api/v1/user-valid-check")
public class ValidUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONParsing jsonParsing = new JSONParsing();
		JSONObject json = jsonParsing.JSONParse(request);

		if (json == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("Error parsing JSON data!");
			return;
		}

		String email = (String) json.get("email");
		String password = (String) json.get("password");
		
		HashMap<String, String> result = new UserService().signIn(email, password);
		
		JSONObject responseData = new JSONObject(result);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(responseData.toJSONString());

	}

}
