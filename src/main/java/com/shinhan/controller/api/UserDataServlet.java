package com.shinhan.controller.api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.shinhan.domain.dto.UserDTO;

@WebServlet("/api/v1/user-data")
public class UserDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserDTO user = (UserDTO) request.getSession().getAttribute("user");
		
		JSONObject responseData = new JSONObject();
		responseData.put("userEmail", user.getEmail());
		responseData.put("userNicname", user.getNicname());

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(responseData.toJSONString());
	}
}
