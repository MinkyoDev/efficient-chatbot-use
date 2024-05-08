package com.shinhan.User;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {

	UserDAO userDAO = new UserDAO();

	public boolean duplicationCheck(String email) {
		if (userDAO.selectAllByEmail(email) == null) {
			return true;
		}
		return false;
	}

	public boolean signUp(String email, String nicname, String password) {
		int result = userDAO.insertUser(userDAO.makeUser(email, nicname, password));
		return result == 1 ? true : false;
	}
	
	public void signIn(String email, String password) {
		HashMap<String, String> response = new HashMap<>();
		UserDTO user = userDAO.selectAllByEmail(email);
		if (user == null) {
			response.put("code", "0");
			response.put("message", "Invalid email");
		}
	}



//	public String registration(String id, String password) {
//		// id 유효성 확인
//		if (!idValidator(id)) {
//			return "id는 영문자와 숫자로만 이루어져 있어야 합니다.";
//		}
//		// password 유효성 확인
//		if (!passwordValidator(password)) {
//			return "비밀번호는 영문과 숫자로만 이루어져야하며 4자리 이상이여야 합니다.";
//		}
//		// 중복확인
//		List<UserDTO> list = userDAO.selectAllByEmail(id);
//
//		// 비활성화된 id가 존재할때
//		if (list.size() != 0 && !list.get(0).is_active()) {
//			int result = userDAO.updateUser(makeUser(id, password));
//			if (result == 0) {
//				return "회원정보가 저장되지 않았습니다. DB error";
//			}
//			return "회원가입을 성공하였습니다.";
//			// 동일한 id가 있을 때
//		} else if (list.size() != 0) {
//			return "동일한 id가 존재합니다.";
//		}
//
//		// user 저장
//		int result = userDAO.insertUser(makeUser(id, password));
//		if (result == 0) {
//			return "회원정보가 저장되지 않았습니다. DB error";
//		}
//		return "회원가입을 성공하였습니다.";
//	}

	public String updatePassword(String id, String newPassword) {
		// password 유효성 확인
		if (!passwordValidator(newPassword)) {
			return "비밀번호는 영문과 숫자로만 이루어져야하며 4자리 이상이여야 합니다.";
		}

		// password 변경
//		int result = userDAO.updateUser(makeUser(id, newPassword));
//		if (result == 0) {
//			return "회원정보가 저장되지 않았습니다. DB error";
//		}
		return "성공적으로 비밀번호가 변경되었습니다.";
	}

	public int deleteByID(String id) {
		return userDAO.deleteByID(id);
	}

	public UserDTO logIn(String id, String password) {
		List<UserDTO> list = userDAO.selectByIDandPassword(id, password);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

//	public UserDTO makeUser(String id, String password) {
//		UserDTO user = new UserDTO();
//		user.setId(id);
//		user.setPassword(password);
//		return user;
//	}

	public static boolean idValidator(String id) {
		String regex = "^[a-zA-Z0-9]+$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(id);

		return matcher.matches() ? true : false;
	}

	public static boolean passwordValidator(String password) {
		String regex = "^[a-zA-Z0-9]{4,}$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);

		return matcher.matches() ? true : false;
	}

}
