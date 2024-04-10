package controllers;

import java.util.Scanner;

import DTO.UserDTO;
import services.UserService;

public class HomeController {
	
	static Scanner sc = new Scanner(System.in);
	static UserService userService = new UserService();

	public static void homeConroller() {
		boolean isStop = false;
		while (!isStop) {
			System.out.println();
			System.out.println("============================");
			System.out.println("Home");
			System.out.println("============================");
			System.out.println("1.로그인");
			System.out.println("2.등록");
			System.out.println("3.종료");
			System.out.print("입력>> ");
			int job = Integer.parseInt(sc.nextLine());
			
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
			}
		}
	}
	
	private static UserDTO logInController() {
		System.out.println();
		System.out.println("============================");
		System.out.println("Login");
		System.out.println("============================");
		System.out.print("id>> ");
		String id = sc.nextLine();
		System.out.print("password>> ");
		String password = sc.nextLine();
		UserDTO user = userService.logIn(id, password);
		if (user == null) {
			System.out.println("id 또는 password가 잘못되었습니다.");
			return null;
		}
		System.out.println("로그인 성공");
		return user;
	}

	public static void registController() {
		System.out.println();
		System.out.println("============================");
		System.out.println("Registration");
		System.out.println("============================");
		System.out.print("id>> ");
		String id = sc.nextLine();
		System.out.print("password>> ");
		String password = sc.nextLine();
		String result = userService.registration(id, password);
		System.out.println(result);
	}
}
