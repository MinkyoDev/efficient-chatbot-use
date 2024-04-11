package controllers;

import java.util.List;

import DTO.ChatDTO;
import DTO.LogDTO;
import DTO.UserDTO;
import services.ChatService;
import utils.InputUtil;
import views.MainView;

public class ManageController {

	static ChatService chatService = new ChatService();

	public static void manageChat(UserDTO user) {
		boolean isStop = false;
		while (!isStop) {
			String[] contents = { "채팅방 조회하기", "채팅방 내용보기", "채팅방 삭제하기", "돌아가기" };
			MainView.printMenus("Management", contents, '=');
			int job = InputUtil.inputInt(null);

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

	private static void fetchChat(UserDTO user) {
		MainView.printMenus("Fetch Chat", null, '-');
		List<ChatDTO> chatList = chatService.getAllChats(user.getId());
		if (chatList.size() == 0) {
			System.out.println("만들어진 채팅방이 없습니다.");
			return;
		}
		MainView.printChatDTOs(chatList);
	}

	private static void fetchChatLog(UserDTO user) {
		MainView.printMenus("Fetch Chat log", null, '-');
		int chatID = InputUtil.inputInt("조회할 채팅방을 선택해 주세요.");
		List<LogDTO> logList = chatService.getLogByChatid(user.getId(), chatID);
		if (logList.size() == 0) {
			System.out.println("채팅 내역이 없습니다.");
			return;
		}
		MainView.printLogDTOs(logList);

	}

	private static void deleteChat(UserDTO user) {
		MainView.printMenus("Delete Chat", null, '-');
		int chatID = InputUtil.inputInt("삭제할 채팅방을 선택헤 주세요.");
		int result = chatService.deleteChat(user.getId(), chatID);
		if (result == 0) {
			System.out.println("해당 채팅방은 존재하지 않습니다.");
		}else {
			System.out.println("채팅방이 성공적으로 삭제되었습니다.");
		}
	}

}
