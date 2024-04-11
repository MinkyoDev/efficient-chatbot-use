package controllers;

import DTO.UserDTO;
import services.UserService;
import utils.InputUtil;
import views.MainView;

public class UserUpdateController {

	static UserService userService = new UserService();

	public static int updateUser(UserDTO user) {
		boolean isStop = false;
		while (!isStop) {
			String[] contents = { "password 변경", "탈퇴하기", "돌아가기" };
			MainView.printMenus("User Management", contents, '=');
			int job = InputUtil.inputInt(null);

			switch (job) {
			case 1 -> {
				updatePassword(user);
			}
			case 2 -> {
				int result = withdrawal(user);
				if (result == 1) {
					return 1;
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

	private static void updatePassword(UserDTO user) {
		MainView.printMenus("Update Password", null, '-');
		String newPassword = InputUtil.inputString("새로운 비밀번호를 입력해 주세요.", null);
		String result = userService.updatePassword(user.getId(), newPassword);
		System.out.println(result);
	}

	private static int withdrawal(UserDTO user) {
		int result = 0;
		MainView.printMenus("Withdrawal", null, '-');
		String select = InputUtil.inputString("정말 탈퇴하시겠습니까? (y/n)", null);
		if (select.toLowerCase().equals("y")) {
			result = userService.deleteByID(user.getId());
			if (result == 1) {
				System.out.println("성공적으로 탈퇴되었습니다.");
			} else {
				System.out.println("탈퇴에 실패하였습니다.");
			}
		} else {
			System.out.println("취소되었습니다.");
		}
		return result;
	}
}
