package controllers;

import java.util.List;
import java.util.Scanner;

import DTO.LogDTO;
import services.ChatLogService;

public class ChatController {

	static Scanner sc = new Scanner(System.in);

	public static void chatController(int chatid) {
		System.out.println();
		System.out.println("============================");
		System.out.println("Chat Start");
		System.out.println("============================");
		System.out.println("채팅을 종료하시려면 q또는 quit를 입력해 주세요.");

		boolean isStop = false;

		ChatLogService chatService = new ChatLogService();

		List<LogDTO> chatLogs = chatService.getChatLogs(chatid);
		for (LogDTO log : chatLogs) {
			System.out.printf("%s> %s\n", log.getUser_id(), log.getRequest());
			System.out.printf("GPT> %s\n", log.getResponse());
		}

		while (!isStop) {
			System.out.print("입력> ");
			String input = sc.nextLine();
			if (input.equals("q") || input.equals("quit")) {
				System.out.println("채팅을 종료합니다.");
				return;
			}
			String response = chatService.getChatbotResponse(chatid, input);

			System.out.println("GPT> " + response);
		}
	}
}
