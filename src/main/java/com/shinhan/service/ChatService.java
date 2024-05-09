package com.shinhan.service;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.shinhan.domain.dao.ChatDAO;
import com.shinhan.domain.dao.ChatLogDAO;
import com.shinhan.domain.dto.ChatDTO;
import com.shinhan.domain.dto.LogDTO;
import com.shinhan.utils.Constants;
import com.shinhan.utils.OpenAIRequest;

public class ChatService {

	OpenAIRequest openAIRequest = new OpenAIRequest();
	ChatDAO chatDAO = new ChatDAO();
	ChatLogDAO chatLogDAO = new ChatLogDAO();
	JSONObject jr;

	public int insertChat(String userid, String modelName, boolean stream, boolean memory, boolean cache) {
		return chatDAO.insertChat(makeChat(userid, modelName, stream, memory, cache));
	}

	public List<ChatDTO> getAllChats(String userid) {
		return chatDAO.selectAll(userid);
	}

	public List<LogDTO> getLogByChatid(String userid, int chatid) {
		return chatLogDAO.selectAllByChatIDUserID(userid, chatid);
	}

	public int deleteChat(String userid, int chatid) {
		return chatDAO.deleteChat(userid, chatid);
	}

	public ChatDTO makeChat(String userid, String modelName, boolean stream, boolean memory, boolean cache) {
		ChatDTO chat = new ChatDTO();
		chat.setUser_id(userid);
		chat.setModel(modelName);
		chat.setStream_enabled(stream);
		chat.setMemory_enabled(memory);
		chat.setCeche_enabled(cache);
		return chat;
	}

	public void makeName(int chatid, HashMap<String, String> datas) {
		String prompt = datas.get("prompt") + datas.get("contents") + "\\nMake the title of the above conversation in English in one sentence or less";
		jr = openAIRequest.chatBot(Constants.MODEL_NAME_FOR_SUMMARY, prompt);
		String name = null;
		try {
			name = jr.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		chatDAO.updateName(chatid, name);
	}

	public ChatDTO getAllChatsByChatid(int chatid) {
		return chatDAO.selectByChatid(chatid);
	}

}