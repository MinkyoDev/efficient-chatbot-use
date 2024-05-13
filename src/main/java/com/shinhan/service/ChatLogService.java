package com.shinhan.service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import com.shinhan.domain.dao.ChatDAO;
import com.shinhan.domain.dao.ChatLogDAO;
import com.shinhan.domain.dao.HistoryDAO;
import com.shinhan.domain.dto.ChatDTO;
import com.shinhan.domain.dto.ChatLogDTO;
import com.shinhan.utils.OpenAIRequest;

public class ChatLogService {

	OpenAIRequest openAIRequest = new OpenAIRequest();
	HistoryDAO historyDAO = new HistoryDAO();
	ChatDAO chatDAO = new ChatDAO();
	ChatLogDAO chatLogDAO = new ChatLogDAO();
	JSONObject jr;

	public String getChatbotResponse(int chatId, String input, String localPath) {
		ChatDTO chat = new ChatService().getChatById(chatId);

		// cache 조회
//		if (chat.isCeche_enabled()) {
//			LogDTO log = checkSameContents(chatid, input);
//			if (log != null) {
//				map.put("contents", log.getResponse());
//				return map;
//			}
//		}
		
		// 프롬프트 제작
		final String prompt; // final로 선언
		if (chat.isMemory_enabled()) {
			prompt = makePrompt(chatId, input);
		} else {
			prompt = input; // 값을 변경하지 않음
		}

		// 답변 생성
		HashMap<String, String> result = openAIRequest.getChatbotResponse(chat.getModel(), prompt, localPath);
		ChatLogDTO chatLog = chatLogDAO.makeChatLog(chatId, input, result.get("content"),
				Integer.parseInt(result.get("promptTokens")), Integer.parseInt(result.get("completionTokens")));

		// log 저장
		int DBResult = chatLogDAO.insertChatLog(chatLog);
		if (DBResult == 0)
			System.out.println("대화내용이 저장되지 않았습니다. DB error");

		// chat name 생성
		CompletableFuture.runAsync(() -> {
			makeChatName(chatId, prompt, result.get("content"), localPath);
		});

		return result.get("content");
	}

	private void makeChatName(int chatId, String prompt, String answer, String localPath) {
		int count = chatLogDAO.countChatLogByChatId(chatId);

		String input = prompt + answer + "/앞의 대화의 제목을 단답형으로 만들것";

		if (1 < count && count < 4) {
			HashMap<String, String> result = openAIRequest.getChatbotResponse("gpt-3.5-turbo", input, localPath);
			System.out.println(result.get("content"));
			chatDAO.updateName(chatId, result.get("content"));
		} else if (count % 10 == 2) {
			HashMap<String, String> result = openAIRequest.getChatbotResponse("gpt-3.5-turbo", input, localPath);
			System.out.println(result.get("content"));
			chatDAO.updateName(chatId, result.get("content"));
		}
	}

	public String makePrompt(int chatid, String input) {
		// log 조회
		String prompt = "";
		for (ChatLogDTO log : chatLogDAO.selectByChatIDisNull(chatid)) {
			prompt += "/user: " + log.getRequest() + "/assistant: " + log.getResponse();
		}
		prompt += "/user: " + input + "/assistant: ";

		return prompt;
	}

	public List<ChatLogDTO> getChatLogs(int chatid, String userEmail) {
		return chatLogDAO.selectLogByChatIDandUserEmail(chatid, userEmail);
	}
}