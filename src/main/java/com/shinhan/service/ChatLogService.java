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

		// 프롬프트 제작
		final String prompt; // final로 선언
		if (chat.isMemory_enabled()) {
			prompt = makePrompt(chatId, input);
		} else {
			prompt = input; // 값을 변경하지 않음
		}
		System.out.println("prompt_o: " + prompt);

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
		System.out.println("count: "+count);

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

//
//	public HashMap<String, String> getChatbotResponse(int chatid, ChatDTO chat, String input) {
//		HashMap<String, String> map = new HashMap<>();
//		if (input.equals("")) {
//			map.put("contents", "내용을 입력해 주세요.");
//			return map;
//		}
//
//		// cache 조회
//		if (chat.isCeche_enabled()) {
//			LogDTO log = checkSameContents(chatid, input);
//			if (log != null) {
//				map.put("contents", log.getResponse());
//				return map;
//			}
//		}
//
//		String prompt = input;
//		// 프롬프트 제작
//		if (chat.isMemory_enabled()) {
//			prompt = makePrompt(chatid, input);
//		}
//
//		// 답변 생성
//		ChatLogDTO chatDTO = null;
//		if (chat.isStream_enabled()) {
//			String contents = openAIRequest.chatBotStream(chat.getModel(), prompt);
//			chatDTO = openAIRequest.makeChat(chatid, chat.getModel(), input, contents);
//			map.put("prompt", prompt);
//			map.put("contents", null);
//		} else {
//			jr = openAIRequest.chatBot(chat.getModel(), prompt);
//			chatDTO = openAIRequest.makeChat(chatid, chat.getModel(), input, jr);
//			map.put("prompt", prompt);
//			map.put("contents", chatDTO.getResponse());
//		}
//
//		// log 저장
//		int cResult = chatLogDAO.insertChatLog(chatDTO);
//		if (cResult == 0) {
//			map.put("contents", "대화내용이 저장되지 않았습니다. DB error");
//			return map;
//		}
//
//		// total token 계산
//		if (chat.isMemory_enabled() && !chat.isStream_enabled()) {
//			int totalTokens = chatLogDAO.calculateTotalTokens(chatid);
//
//			if (totalTokens > Constants.RECORD_TOKEN_LIMIT) {
//				// summary 생성
//				String contents = prompt + chatDTO.getResponse() + "/위의 대화내용 단답형으로 요약";
//				jr = openAIRequest.chatBot(Constants.MODEL_NAME_FOR_SUMMARY, contents);
//				HistoryDTO historyDTO = openAIRequest.makeHistory(jr, chatid);
//				// 대화 내용 저장
//				int hResult = historyDAO.insertHistory(historyDTO, chatid);
//				if (hResult == 0) {
//					map.put("contents", "대화요약이 저장되지 않았습니다. DB error");
//					return map;
//				}
//			}
//		}
//		return map;
//	}
//
//	public LogDTO checkSameContents(int chatid, String input) {
//		return chatLogDAO.selectByRequest(chatid, input);
//	}

//	public String makePrompt(int chatid, String input) {
//		// summary 조회
//		String summarys = "previous contents: ";
//		for (String summery : historyDAO.selectByChatID(chatid)) {
//			summarys += summery;
//		}
//
//		// log 조회
//		String prompt = "";
//		for (ChatLogDTO log : chatLogDAO.selectByChatIDisNull(chatid)) {
//			prompt += "/user: " + log.getRequest() + "/assistant: " + log.getResponse();
//		}
//		prompt += "/user: " + input + "/assistant: ";
//
//		String result = summarys + "/" + prompt;
//		return result;
//	}

}
