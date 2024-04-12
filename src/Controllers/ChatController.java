package controllers;

import java.util.HashMap;
import java.util.List;

import DTO.ChatDTO;
import DTO.LogDTO;
import services.ChatLogService;
import services.ChatService;
import utils.InputUtil;
import views.MainView;

public class ChatController {

	static ChatService chatService = new ChatService();
	static ChatLogService chatLogService = new ChatLogService();

	public static void chatController(int chatid) {
		boolean isStop = false;
		
		MainView.printMenus("Chat Start", null, '=');

		// chat 설정 조회
		ChatDTO chat = chatService.getAllChatsByChatid(chatid);
		MainView.printChatDTOs(chat);
		
		System.out.println("채팅을 종료하시려면 q또는 quit를 입력해 주세요.");

		// 이전 대화내용 조회
		List<LogDTO> chatLogs = chatLogService.getChatLogs(chatid);
		for (LogDTO log : chatLogs) {
			System.out.printf("user> %s\n", log.getRequest());
			System.out.printf("GPT> %s\n", log.getResponse());
		}

		// 채팅 시작
		HashMap<String, String> datas = new HashMap<>();
		while (!isStop) {
			String input = InputUtil.inputString(null, "user");
			if (input.equals("q") || input.equals("quit")) {
				System.out.println("채팅을 종료중입니다. 잠시만 기댜려 주세요.");
				if (datas.get("prompt") != null) {
					chatService.makeName(chatid, datas);
				}
				return;
			}
			System.out.print("GPT> ");
			datas = chatLogService.getChatbotResponse(chatid, chat, input);
			if (datas.get("contents") != null) {
				System.out.println(datas.get("contents"));
			}
		}
	}
}
