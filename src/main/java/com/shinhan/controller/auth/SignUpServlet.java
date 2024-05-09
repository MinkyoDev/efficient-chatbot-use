package com.shinhan.controller.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shinhan.service.UserService;

@WebServlet("/auth/sign-up")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("sign_up.html").forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String nicname = email.split("@")[0];
		
		UserService userService = new UserService();
		boolean result = userService.signUp(email, nicname, password);
		if (result) {
			request.setAttribute("nicname", nicname);
			request.getRequestDispatcher("sign_up_success.jsp").forward(request, response);			
		}
	}

	

}
