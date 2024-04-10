package services;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import DAO.ChatLogDAO;
import DAO.HistoryDAO;
import DTO.ChatLogDTO;
import DTO.HistoryDTO;
import DTO.LogDTO;
import utils.Constants;
import utils.OpenAIRequest;

public class ChatLogService {

	OpenAIRequest openAIRequest = new OpenAIRequest();
	ChatLogDAO chatDAO = new ChatLogDAO();
	HistoryDAO historyDAO = new HistoryDAO();
	JSONObject jr;

	public String getChatbotResponse(int chatid, String input) {
		if (input.equals("")) {
			return "내용을 입력해 주세요.";
		}
		
		// cache 조회
		checkSameContents(chatid, input);

		// 프롬프트 제작
		HashMap<String, String> datas = makePrompt(chatid, input);
		
		// 답변 생성
		jr = openAIRequest.chatBot(input, datas);
		ChatLogDTO chatDTO = openAIRequest.makeChat(chatid, jr, input);
		
		// log 저장
		int cResult = chatDAO.insertChatLog(chatDTO);
		if (cResult == 0) {
			return "대화내용이 저장되지 않았습니다. DB error";
		}

		// total token 계산
		int totalTokens = chatDAO.calculateTotalTokens(chatid);

		if (totalTokens > Constants.RECORD_TOKEN_LIMIT) {
			// summary 생성
			String prompt = datas.get("prompt") + chatDTO.getResponse() + "\\n위의 대화내용 요약";
			jr = openAIRequest.generateSummary(chatid, prompt);
			HistoryDTO historyDTO = openAIRequest.makeHistory(jr, chatid);
			// 대화 내용 저장
			int hResult = historyDAO.insertHistory(historyDTO, chatid);
			if (hResult == 0) {
				return "대화요약이 저장되지 않았습니다. DB error";
			}
		}

		return chatDTO.getResponse();
	}

	// ************************************************************************************************
	public void checkSameContents(int chatid, String input) {
		List<LogDTO> logList = chatDAO.selectAllByChatID(chatid);
		List<LogDTO> tmp = logList.stream().filter(log -> log.getRequest().equals("안녕")).toList();
//		System.out.println(tmp);
	}

	public HashMap<String, String> makePrompt(int chatid, String input) {
		HashMap<String, String> datas = new HashMap<String, String>();
		
		// summary 조회
		String summarys = "previous contents: ";
		for (String summery : historyDAO.selectByChatID(chatid)) {
			summarys += summery;
		}
		datas.put("summarys", summarys);

		// log 조회
		String prompt = "";
		for (LogDTO log : chatDAO.selectByChatIDisNull(chatid)) {
			prompt += "\\nuser: " + log.getRequest() + "\\nassistant: " + log.getResponse();
		}
		prompt += "\\nuser: " + input + "\\nassistant: ";
		datas.put("prompt", prompt);
		
		return datas;
	}

	public List<LogDTO> getChatLogs(int chatid) {
		return chatDAO.selectTopNByChatID(chatid, Constants.BEFORE_LOGS);
	}
}
