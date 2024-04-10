package controllers;

import java.util.List;
import java.util.Scanner;

import DTO.ChatDTO;
import DTO.LogDTO;
import DTO.UserDTO;
import services.ChatService;

public class ManageController {
	
	static Scanner sc = new Scanner(System.in);
	static ChatService chatService = new ChatService();

	public static void manageChat(UserDTO user) {
		boolean isStop = false;
		while (!isStop) {
			System.out.println();
			System.out.println("============================");
			System.out.println("Management");
			System.out.println("============================");
			System.out.println("1.채팅방 조회하기");
			System.out.println("2.채팅방 내용보기");
			System.out.println("3.채팅방 삭제하기");
			System.out.println("4.돌아가기");
			System.out.print("입력>> ");
			int job = Integer.parseInt(sc.nextLine());
			
			switch (job) {
			case 1 -> {
				fetchChat(user);
			}
			case 2 -> {
				fetchChatLog(user);
			}
			case 3 -> {
				deleteChat(user);
			}
			case 4 -> {
				isStop = true;
				System.out.println("메인화면으로 돌아갑니다.");
			}
			default -> {
				System.out.println("다시 입력해주세요.");
			}
			}
		}
		
	}

	private static void deleteChat(UserDTO user) {
		System.out.println();
		System.out.println("============================");
		System.out.println("Delete Chat");
		System.out.println("============================");
		System.out.println("삭제할 채팅방을 선택헤 주세요.");
		System.out.print("입력>> ");
		int chatID = Integer.parseInt(sc.nextLine());
		int result = chatService.deleteChat(chatID);
		if (result == 0) {
			System.out.println("채팅방이 삭제되지 않았습니다. DB error");
		}
		System.out.println("채팅방이 성공적으로 삭제되었습니다.");
	}

	private static void fetchChatLog(UserDTO user) {
		System.out.println();
		System.out.println("============================");
		System.out.println("Fetch Chat log");
		System.out.println("============================");
		System.out.println("조회할 채팅방을 선택해 주세요.");
		System.out.print("입력>> ");
		int chatID = Integer.parseInt(sc.nextLine());
		
		List<LogDTO> logList = chatService.getLogByChatid(chatID);
		for (LogDTO log : logList) {
			System.out.println(log);
		}
		
	}

	private static void fetchChat(UserDTO user) {
		System.out.println();
		System.out.println("============================");
		System.out.println("Fetch Chat");
		System.out.println("============================");
		List<ChatDTO> chatList = chatService.getAllChats(user.getId());
		for (ChatDTO chat : chatList) {
			System.out.println(chat);
		}
	}
}
