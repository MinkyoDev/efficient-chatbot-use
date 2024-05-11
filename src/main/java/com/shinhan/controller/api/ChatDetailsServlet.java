package com.shinhan.controller.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.shinhan.domain.dto.ChatDTO;
import com.shinhan.domain.dto.ChatLogDTO;
import com.shinhan.domain.dto.UserDTO;
import com.shinhan.service.ChatLogService;
import com.shinhan.service.ChatService;

@WebServlet("/api/v1/chat-details")
public class ChatDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int chatId = Integer.parseInt(request.getParameter("chatId"));
		UserDTO user = (UserDTO) request.getSession().getAttribute("user");

		JSONObject responseData = makeReponse(chatId, user.getEmail());
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(responseData.toJSONString());
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject makeReponse(int chatId, String userEmail) {
		ChatDTO chat = new ChatService().getChatById(chatId);
		List<ChatLogDTO> chatLogList = new ChatLogService().getChatLogs(chatId, userEmail);
		
		JSONObject settings = new JSONObject();
		settings.put("modelName", chat.getModel());
		settings.put("stream", chat.isStream_enabled());
		settings.put("memory", chat.isMemory_enabled());
		settings.put("cache", chat.isCeche_enabled());
		
		JSONArray conversations = new JSONArray();
		for (ChatLogDTO chatLog:chatLogList) {
			JSONObject conversation = new JSONObject();
			conversation.put("request", chatLog.getRequest());
			conversation.put("response", chatLog.getResponse());
			conversations.add(conversation);
		}
		
		JSONObject response = new JSONObject();
		response.put("settings", settings);
		response.put("conversations", conversations);
		
		return response;
	}

}
