package com.shinhan.service;

import java.util.List;

import org.json.JSONObject;

import com.shinhan.domain.dao.ChatDAO;
import com.shinhan.domain.dao.ChatLogDAO;
import com.shinhan.domain.dto.ChatDTO;
import com.shinhan.utils.OpenAIRequest;

public class ChatService {

	OpenAIRequest openAIRequest = new OpenAIRequest();
	ChatDAO chatDAO = new ChatDAO();
	ChatLogDAO chatLogDAO = new ChatLogDAO();
	JSONObject jr;

	public int insertChat(String userid, String modelName, boolean stream, boolean memory, boolean cache) {
		return chatDAO.insertChat(makeChat(userid, modelName, stream, memory, cache));
	}

	public List<ChatDTO> getAllChats(String userEmail) {
		return chatDAO.selectAll(userEmail);
	}
	
	public List<ChatDTO> getAllOrderByModified(String userEmail) {
		return chatDAO.selectAllOrderByModified(userEmail);
	}
	
	public ChatDTO getChatById(int chatId) {
		return chatDAO.selectByChatid(chatId);
	}

	public int deleteChat(String userid, int chatid) {
		return chatDAO.deleteChat(userid, chatid);
	}
	
	public ChatDTO searchValidChatId(String userEmail, int chatId) {
		return chatDAO.selectByEmailandChatId(userEmail, chatId);
	}

	public ChatDTO makeChat(String userEmail, String modelName, boolean stream, boolean memory, boolean cache) {
		ChatDTO chat = new ChatDTO();
		chat.setUser_email(userEmail);
		chat.setModel(modelName);
		chat.setStream_enabled(stream);
		chat.setMemory_enabled(memory);
		chat.setCeche_enabled(cache);
		return chat;
	}

}
