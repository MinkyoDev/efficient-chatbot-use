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
import com.shinhan.utils.Constants;
import com.shinhan.utils.CookieUtils;

@WebServlet("/chat/chat")
public class ChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String> cookies = CookieUtils.cookiesToMap(request.getCookies());
		UserDTO user = (UserDTO) request.getSession().getAttribute("user");

		String chatId_s = cookies.get("chatId");
		int chatId = 0;
		if (chatId_s == null) {
			chatId = getLastChatId(user);
		} else {
			chatId = Integer.parseInt(chatId_s);
			chatId = validChatId(chatId, user) != null ? chatId : getLastChatId(user);
		}
		Cookie cookie = new Cookie("chatId", String.valueOf(chatId));
		cookie.setMaxAge(24 * 3600);
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

		int chatId = 0;

		// chat 하나도 없으면 디폴트로 하나 생성
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
