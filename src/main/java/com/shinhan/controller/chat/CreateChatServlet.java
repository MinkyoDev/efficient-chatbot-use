package com.shinhan.controller.chat;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shinhan.domain.dto.ModelDTO;
import com.shinhan.domain.dto.UserDTO;
import com.shinhan.service.ChatService;
import com.shinhan.service.ModelService;

@WebServlet("/chat/new-chat")
public class CreateChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String modelName = request.getParameter("modelName");

		HttpSession session = request.getSession();
		UserDTO user = (UserDTO) session.getAttribute("user");

		ChatService chatService = new ChatService();
		int chatId = chatService.insertChat(user.getEmail(), modelName, false, true, true);

		Cookie cookie = new Cookie("chatId", String.valueOf(chatId));
		cookie.setMaxAge(24 * 3600);

		response.addCookie(cookie);
		response.sendRedirect("../chat");
	}

}
