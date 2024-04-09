package Controllers;

import java.util.List;
import java.util.Scanner;

import DTO.LogDTO;
import Services.ChatService;

public class ChatController {

	public static void chatController() {
		int chatid = 1;
		boolean isStop = false;
		Scanner sc = new Scanner(System.in);

		ChatService chatService = new ChatService();

		List<LogDTO> chatLogs = chatService.getChatLogs(chatid);
		for (LogDTO log : chatLogs) {
			System.out.printf("%s> %s\n", log.getUser_id(), log.getRequest());
			System.out.printf("GPT> %s\n", log.getResponse());
		}

		while (!isStop) {
			System.out.print("입력> ");
			String input = sc.nextLine();
			String response = chatService.getChatbotResponse(chatid, input);

			System.out.println("GPT> " + response);
		}
	}
}
