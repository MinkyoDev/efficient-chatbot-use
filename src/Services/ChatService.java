package services;

import java.util.List;

import DAO.ChatDAO;
import DAO.ChatLogDAO;
import DTO.ChatDTO;
import DTO.LogDTO;

public class ChatService {

	ChatDAO chatDAO = new ChatDAO();
	ChatLogDAO chatLogDAO = new ChatLogDAO();

	public int insertChat(String userid, boolean memory, boolean cache) {
		return chatDAO.insertChat(makeChat(userid, memory, cache));
	}
	
	public List<ChatDTO> getAllChats(String userid) {
		return chatDAO.selectAll(userid);
	}
	
	public List<LogDTO> getLogByChatid(int chatid) {
		return chatLogDAO.selectAllByChatID(chatid);
	}
	
	public int deleteChat(int chatid) {
		return chatDAO.deleteChat(chatid);
	}

	public ChatDTO makeChat(String userid, boolean memory, boolean cache) {
		ChatDTO chat = new ChatDTO();
		chat.setUser_id(userid);
		chat.setMemory_enabled(memory);
		chat.setCeche_enabled(cache);
		return chat;
	}

}
