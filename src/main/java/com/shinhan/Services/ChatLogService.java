package com.shinhan.Services;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.shinhan.DAO.ChatLogDAO;
import com.shinhan.DAO.HistoryDAO;
import com.shinhan.DTO.ChatDTO;
import com.shinhan.DTO.ChatLogDTO;
import com.shinhan.DTO.HistoryDTO;
import com.shinhan.DTO.LogDTO;
import com.shinhan.utils.Constants;
import com.shinhan.utils.OpenAIRequest;

public class ChatLogService {

	OpenAIRequest openAIRequest = new OpenAIRequest();
	HistoryDAO historyDAO = new HistoryDAO();
	ChatLogDAO chatLogDAO = new ChatLogDAO();
	JSONObject jr;

	public HashMap<String, String> getChatbotResponse(int chatid, ChatDTO chat, String input) {
		HashMap<String, String> map = new HashMap<>();
		if (input.equals("")) {
			map.put("contents", "내용을 입력해 주세요.");
			return map;
		}

		// cache 조회
		if (chat.isCeche_enabled()) {
			LogDTO log = checkSameContents(chatid, input);
			if (log != null) {
				map.put("contents", log.getResponse());
				return map;
			}
		}

		String prompt = input;
		// 프롬프트 제작
		if (chat.isMemory_enabled()) {
			prompt = makePrompt(chatid, input);
		}
		
		// 답변 생성
		ChatLogDTO chatDTO = null;
		if (chat.isStream_enabled()) {
			String contents = openAIRequest.chatBotStream(chat.getModel(), prompt);
			chatDTO = openAIRequest.makeChat(chatid, chat.getModel(), input, contents);
			map.put("prompt", prompt);
			map.put("contents", null);
		} else {
			jr = openAIRequest.chatBot(chat.getModel(), prompt);
			chatDTO = openAIRequest.makeChat(chatid, chat.getModel(), input, jr);
			map.put("prompt", prompt);
			map.put("contents", chatDTO.getResponse());
		}

		// log 저장
		int cResult = chatLogDAO.insertChatLog(chatDTO);
		if (cResult == 0) {
			map.put("contents", "대화내용이 저장되지 않았습니다. DB error");
			return map;
		}

		// total token 계산
		if (chat.isMemory_enabled() && !chat.isStream_enabled()) {
			int totalTokens = chatLogDAO.calculateTotalTokens(chatid);

			if (totalTokens > Constants.RECORD_TOKEN_LIMIT) {
				// summary 생성
				String contents = prompt + chatDTO.getResponse() + "/위의 대화내용 단답형으로 요약";
				jr = openAIRequest.chatBot(Constants.MODEL_NAME_FOR_SUMMARY, contents);
				HistoryDTO historyDTO = openAIRequest.makeHistory(jr, chatid);
				// 대화 내용 저장
				int hResult = historyDAO.insertHistory(historyDTO, chatid);
				if (hResult == 0) {
					map.put("contents", "대화요약이 저장되지 않았습니다. DB error");
					return map;
				}
			}
		}
		return map;
	}

	public LogDTO checkSameContents(int chatid, String input) {
		return chatLogDAO.selectByRequest(chatid, input);
	}

	public String makePrompt(int chatid, String input) {
		// summary 조회
		String summarys = "previous contents: ";
		for (String summery : historyDAO.selectByChatID(chatid)) {
			summarys += summery;
		}

		// log 조회
		String prompt = "";
		for (LogDTO log : chatLogDAO.selectByChatIDisNull(chatid)) {
			prompt += "/user: " + log.getRequest() + "/assistant: " + log.getResponse();
		}
		prompt += "/user: " + input + "/assistant: ";

		String result = summarys + "/" + prompt;
		return result.replace("\n", " ");
	}

	public List<LogDTO> getChatLogs(int chatid) {
		return chatLogDAO.selectTopNByChatID(chatid, Constants.BEFORE_LOGS);
	}

}
