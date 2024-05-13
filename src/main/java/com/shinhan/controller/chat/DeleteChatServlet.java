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

import com.shinhan.domain.dto.ChatDTO;
import com.shinhan.domain.dto.UserDTO;
import com.shinhan.service.ChatService;
import com.shinhan.utils.Constants;

@WebServlet("/chat/delete-chat")
public class DeleteChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int chatId = Integer.parseInt(request.getParameter("chatId"));

		HttpSession session = request.getSession();
		UserDTO user = (UserDTO) session.getAttribute("user");

		new ChatService().deleteChat(user.getEmail(), chatId);

		chatId = getLastChatId(user);
		Cookie cookie = new Cookie("chatId", String.valueOf(chatId));
		cookie.setMaxAge(24 * 3600);

		response.addCookie(cookie);
		response.sendRedirect("chat");
	}

	private int getLastChatId(UserDTO user) {
		ChatService chatService = new ChatService();
		List<ChatDTO> chatListModified = chatService.getAllOrderByModified(user.getEmail());
		List<ChatDTO> chatList = chatService.getAllChats(user.getEmail());

		int chatId = 0;
		// chatList이 없을 때
		if (chatList.size() == 0) {
			chatId = chatService.insertChat(user.getEmail(), Constants.DEFAULT_MODEL_NAME, false, true, true);
			return chatId;
		}
		
		if (chatListModified.size() == 0) {
			chatId = chatList.get(0).getChat_id();
		} else {
			chatId = chatListModified.get(0).getChat_id();
		}
		return chatId;
	}

}
