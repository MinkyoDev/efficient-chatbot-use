package com.shinhan.controller.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.shinhan.domain.dto.ChatDTO;
import com.shinhan.domain.dto.UserDTO;
import com.shinhan.service.ChatService;

@WebServlet("/api/v1/chat-list")
public class ChatListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserDTO user = (UserDTO) request.getSession().getAttribute("user");

		ChatService chatService = new ChatService();
		List<ChatDTO> chatList = chatService.getAllOrderByModified(user.getEmail());

		JSONArray modelArray = new JSONArray();
		for (ChatDTO chat : chatList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("chatId", chat.getChat_id());
			jsonObject.put("name", chat.getName());

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String formattedDate = dateFormat.format(chat.getLast_modified_at());
			jsonObject.put("last_modified_at", formattedDate);

			modelArray.add(jsonObject);
		}

		JSONObject responseData = new JSONObject();
		responseData.put("chats", modelArray);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(responseData.toJSONString());
	}

}
