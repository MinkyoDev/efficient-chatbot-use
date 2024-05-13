package com.shinhan.service;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shinhan.domain.dao.UserDAO;
import com.shinhan.domain.dto.UserDTO;

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
	
	public HashMap<String, String> signIn(String email, String password) {
		HashMap<String, String> response = new HashMap<>();
		UserDTO user = userDAO.selectAllByEmail(email);
		if (user == null) {
			response.put("code", "1");
			response.put("message", "Invalid email");
		} else if (!user.getPassword().equals(password)) {
			response.put("code", "2");
			response.put("message", "Invalid password");
		} else {
			response.put("code", "0");
			response.put("message", "sign in successfully");			
		}
		return response;
	}
	
	public UserDTO getUser(String email) {
		return userDAO.selectAllByEmail(email);
	}


	public int updatePassword(String userEmail, String userNicname, String newPassword) {
		// password 변경
		UserDTO updateUser =userDAO.makeUser(userEmail, userNicname, newPassword);
		return userDAO.updateUser(updateUser);
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
