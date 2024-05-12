package com.shinhan.controller.auth;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shinhan.domain.dto.UserDTO;
import com.shinhan.service.UserService;

@WebServlet("/auth/change-password")
public class ChangePwServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserDTO user = (UserDTO) request.getSession().getAttribute("user");
		
		if (user==null) {
			response.sendRedirect("sign-in");
			return;
		}
		request.getRequestDispatcher("change_password.html").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String password = request.getParameter("password");
		
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO) session.getAttribute("user");
		
		UserService userService = new UserService();
		int result = userService.updatePassword(user.getEmail(), user.getNicname(), password);
		if (result==1) {
			session.invalidate();
			
			request.getRequestDispatcher("chang_pw_success.html").forward(request, response);			
		}
	}

}
