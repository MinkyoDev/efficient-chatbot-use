package com.shinhan.auth;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

		UserService userService = new UserService();
		HashMap<String, String> result = userService.signIn(email, password);
		if (!result.get("code").equals("0")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setContentType("text/plain");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write("Unauthorized access.");
	        return;
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("user", userService.getUser(email));

		response.sendRedirect("../chat");
	}

}