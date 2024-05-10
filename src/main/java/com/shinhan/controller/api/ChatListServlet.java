package com.shinhan.controller.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shinhan.domain.dto.ChatDTO;
import com.shinhan.domain.dto.UserDTO;
import com.shinhan.service.ChatService;

@WebServlet("/api/v1/chat-list")
public class ChatListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO) session.getAttribute("user");
		
		ChatService chatService = new ChatService();
		List<ChatDTO> chatList = chatService.getAllChats(user.getEmail());
		System.out.println(chatList);
	}

}
