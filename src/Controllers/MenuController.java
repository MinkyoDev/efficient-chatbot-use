package controllers;

import java.util.List;

import DTO.ChatDTO;
import DTO.ModelDTO;
import DTO.UserDTO;
import services.ChatService;
import services.ModelService;
import services.UserService;
import utils.InputUtil;
import views.MainView;

public class MenuController {

	static UserService userService = new UserService();
	static ChatService chatService = new ChatService();
	static ModelService modelService = new ModelService();

	public static void menuController(UserDTO user) {
		boolean isStop = false;
		while (!isStop) {
			String[] contents = { "새채팅", "이어하기", "채팅관리", "정보수정", "로그아웃" };
			MainView.printMenus("Menu", contents, '=');

			int job = InputUtil.inputInt(null);

			switch (job) {
			case 1 -> {
				makeNewChat(user);
			}
			case 2 -> {
				continueChat(user);
			}
			case 3 -> {
				ManageController.manageChat(user);
			}
			case 4 -> {
				int result = UserUpdateController.updateUser(user);
				if (result == 1) {
					return;
				}
			}
			case 5 -> {
				isStop = true;
				System.out.println("메인화면으로 돌아갑니다.");
			}
			default -> {
				System.out.println("다시 입력해주세요.");
			}
			}
		}
	}

	private static void makeNewChat(UserDTO user) {
		boolean stream = false, memory = true, cache = true;
		String modelName = "gpt-3.5-turbo-1106";
		MainView.printMenus("New Chat", null, '-');

		List<ModelDTO> modelList = modelService.getAllModels();
		MainView.printModelDTOs(modelList);
		int modelNo = InputUtil.inputInt("사용하실 모델을 골라주세요. [defalut: gpt-3.5-turbo-1106]");
		if (modelNo != 0) {
			modelName = modelList.get(modelNo - 1).getName();
		}

		String useStream = InputUtil.inputString("stream 기능을 사용하시겠습니까? [defalut: false] (y/n)", null);
		if (useStream.toLowerCase().equals("y")) {
			stream = true;
			cache = false;
			System.out.println("stream 기능을 사용하시면 summary와 cache 기능을 사용하실 수 없습니다.");
		} else {
			String useCache = InputUtil.inputString("cache 기능을 사용하시겠습니까? [defalut: true] (y/n)", null);
			if (useCache.toLowerCase().equals("n")) {
				cache = false;
			}
		}
		String useMemory = InputUtil.inputString("memory 기능을 사용하시겠습니까? [defalut: true] (y/n)", null);
		if (useMemory.toLowerCase().equals("n")) {
			memory = false;
		}
		int chatid = chatService.insertChat(user.getId(), modelName, stream, memory, cache);
		if (chatid == 0) {
			System.out.println("새로운 채팅방이 생성되지 않았습니다. DB error");
		}
		ChatController.chatController(chatid);
	}

	private static void continueChat(UserDTO user) {
		MainView.printMenus("Continue", null, '-');
		List<ChatDTO> chatList = chatService.getAllChats(user.getId());
		if (chatList.size() == 0) {
			System.out.println("채팅방이 없습니다.");
			return;
		}
		MainView.printChatDTOs(chatList);

		int chatid = InputUtil.inputInt("채팅방을 선택헤 주세요.");
		if (chatid == 0) {
			System.out.println("숫자를 입력해 주세요.");
			return;
		}
		ChatController.chatController(chatid);
	}

}
