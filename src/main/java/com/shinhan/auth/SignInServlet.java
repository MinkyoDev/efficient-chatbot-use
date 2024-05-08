package com.shinhan.auth;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.shinhan.User.UserService;

@WebServlet("/auth/sign-in")
public class SignInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("sign_in.html").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String nicname = email.split("@")[0];
		
		UserService userService = new UserService();
//		boolean result = userService.signIn(email, password);

		JSONObject responseData = new JSONObject();
//		if (result) {
//			responseData.put("message", "available");
//		} else {
//			responseData.put("message", "unavailable");
//		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(responseData.toJSONString());
	}

}