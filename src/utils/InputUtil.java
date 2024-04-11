package utils;

import java.util.Scanner;

public class InputUtil {

	static Scanner sc = new Scanner(System.in);
	
	static public int inputInt(String message) {
		int intInput = 0;
		if (message != null) {
			System.out.println(message);
		}
		
		System.out.print("입력>> ");
		try {
			intInput = Integer.parseInt(sc.nextLine());
			
		} catch (NumberFormatException e) {
		}
		
		return intInput;
	}
	
	static public String inputString(String message, String str) {
		if (message != null) {
			System.out.println(message);
		}
		
		if (str != null) {
			System.out.printf("%s>> ", str);
		}else {
			System.out.print("입력>> ");
		}
		return sc.nextLine();
	}
}
