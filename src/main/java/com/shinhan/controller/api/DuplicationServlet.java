package com.shinhan.controller.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.shinhan.service.UserService;

@WebServlet("/api/v1/duplication-check")
public class DuplicationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");

		UserService userService = new UserService();
		boolean result = userService.duplicationCheck(email);

		JSONObject responseData = new JSONObject();
		if (result) {
			responseData.put("message", "available");
		} else {
			responseData.put("message", "unavailable");
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(responseData.toJSONString());
	}

}
