package controllers;

import java.util.Scanner;

import DTO.UserDTO;
import services.ChatService;
import services.UserService;

public class MenuController {

	static Scanner sc = new Scanner(System.in);
	static UserService userService = new UserService();
	static ChatService chatService = new ChatService();

	public static void menuController(UserDTO user) {
		boolean isStop = false;
		while (!isStop) {
			System.out.println();
			System.out.println("============================");
			System.out.println("Menu");
			System.out.println("============================");
			System.out.println("1.새채팅");
			System.out.println("2.이어하기");
			System.out.println("3.채팅관리");
			System.out.println("4.정보수정");
			System.out.println("5.로그아웃");
			System.out.print("입력>> ");
			int job = Integer.parseInt(sc.nextLine());

			switch (job) {
			case 1 -> {
				makeNewChat(user);
			}
			case 2 -> {
			}
			case 3 -> {
				ManageController.manageChat(user);
			}
			case 4 -> {
				int result = updateUser(user);
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
		System.out.println();
		System.out.println("============================");
		System.out.println("New Chat");
		System.out.println("============================");

		boolean memory = true;
		boolean cache = true;
		System.out.print("memory 기능을 사용하시겠습니까? defalut: true (y/n)>> ");
		String useMemory = sc.nextLine();
		if (useMemory.toLowerCase().equals("n")) {
			memory = false;
		}
		System.out.print("cache 기능을 사용하시겠습니까? defalut: true (y/n)>> ");
		String useCache = sc.nextLine();
		if (useCache.toLowerCase().equals("n")) {
			cache = false;
		}
		int result = chatService.insertChat(user.getId(), memory, cache);
		if (result == 0) {
			System.out.println("새로운 채팅방이 생성되지 않았습니다. DB error");
		}
		ChatController.chatController(result);
	}

	public static int updateUser(UserDTO user) {
		boolean isStop = false;
		while (!isStop) {
			System.out.println();
			System.out.println("============================");
			System.out.println("Update");
			System.out.println("============================");
			System.out.println("1.password 변경");
			System.out.println("2.탈퇴하기");
			System.out.println("3.돌아가기");
			System.out.print("입력>> ");
			int job = Integer.parseInt(sc.nextLine());

			switch (job) {
			case 1 -> {
				System.out.print("새로운 비밀번호를 입력해 주세요.>> ");
				String newPassword = sc.nextLine();
				String result = userService.updatePassword(user.getId(), newPassword);
				System.out.println(result);
			}
			case 2 -> {
				System.out.print("정말 탈퇴하시겠습니까? (y/n)>> ");
				String select = sc.nextLine();
				if (select.toLowerCase().equals("y")) {
					int result = userService.deleteByID(user.getId());
					if (result == 1) {
						System.out.println("성공적으로 탈퇴되었습니다.");
						return result;
					}
					System.out.println("탈퇴에 실패하였습니다.");
					return 0;
				} else {
					System.out.println("취소되었습니다.");
				}
			}
			case 3 -> {
				isStop = true;
				System.out.println("메뉴로 돌아갑니다.");
			}
			default -> {
				System.out.println("다시 입력해주세요.");
			}
			}
		}
		return 0;
	}

}
