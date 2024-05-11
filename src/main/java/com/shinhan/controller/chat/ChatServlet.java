package com.shinhan.controller.chat;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shinhan.domain.dto.ChatDTO;
import com.shinhan.domain.dto.UserDTO;
import com.shinhan.service.ChatService;
import com.shinhan.utils.CookieUtils;

@WebServlet("/chat/chat")
public class ChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String> cookies = CookieUtils.cookiesToMap(request.getCookies());
		UserDTO user = (UserDTO) request.getSession().getAttribute("user");

		String chatId_s = cookies.get("chatId");
		System.out.println("chatId_s: " + chatId_s);
		int chatId = 0;
		if (chatId_s == null) {
			chatId = getLastChatId(user);
		} else {
			System.out.println("getLastChatId(user): " + getLastChatId(user));
			chatId = validChatId(chatId, user) != null ? Integer.parseInt(chatId_s) : getLastChatId(user);
		}
		
		System.out.println("chatId: " + chatId);
		Cookie cookie = new Cookie("chatId", String.valueOf(chatId));
		cookie.setMaxAge(24 * 3600);
//		cookie.setPath("/chat");
		response.addCookie(cookie);

		request.getRequestDispatcher("/chat/chat.html").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	private ChatDTO validChatId(int chatId, UserDTO user) {
		return new ChatService().searchValidChatId(user.getEmail(), chatId);

	}

	private int getLastChatId(UserDTO user) {
		ChatService chatService = new ChatService();
		List<ChatDTO> chatListModified = chatService.getAllOrderByModified(user.getEmail());
		List<ChatDTO> chatList = chatService.getAllChats(user.getEmail());

		System.out.println(chatList);
		int chatId = 0;
		if (chatListModified.size() == 0) {
			chatId = chatList.get(0).getChat_id();
		} else {
			chatId = chatListModified.get(0).getChat_id();
		}
		return chatId;
	}

}
