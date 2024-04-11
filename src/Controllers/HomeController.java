package controllers;

import DTO.UserDTO;
import services.UserService;
import utils.InputUtil;
import views.MainView;

public class HomeController {
	
	static UserService userService = new UserService();

	public static void homeConroller() {
		boolean isStop = false;
		while (!isStop) {
			String[] contents = {"로그인", "등록", "종료"};
			MainView.printMenus("Home", contents, '=');
			int job = InputUtil.inputInt(null);
			
			switch (job) {
			case 1 -> {
				UserDTO user = logInController();
				if (user != null) {
					MenuController.menuController(user);
				}
			}
			case 2 -> {
				registController();
			}
			case 3 -> {
				isStop = true;
				System.out.println("프로그램을 종료합니다.");
			}
			default -> {
				System.out.println("다시 입력해주세요.");
			}
			}
		}
	}
	
	private static UserDTO logInController() {
		MainView.printMenus("Login", null, '-');
		String id = InputUtil.inputString(null, "id");
		String password = InputUtil.inputString(null, "password");
		
		UserDTO user = userService.logIn(id, password);
		if (user == null) {
			System.out.println("id 또는 password가 잘못되었습니다.");
			return null;
		}
		System.out.println("로그인 성공");
		return user;
	}

	public static void registController() {
		MainView.printMenus("Registration", null, '-');
		String id = InputUtil.inputString(null, "id");
		String password = InputUtil.inputString(null, "password");
		
		String result = userService.registration(id, password);
		System.out.println(result);
	}
}
