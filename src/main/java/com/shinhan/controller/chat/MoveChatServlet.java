package com.shinhan.controller.chat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/chat/move-chat")
public class MoveChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String chatId = request.getParameter("chatId");

		Cookie cookie = new Cookie("chatId", chatId);
		cookie.setMaxAge(24 * 3600);

		response.addCookie(cookie);
		response.sendRedirect("../chat");
	}

}
