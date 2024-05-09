package com.shinhan.controller.api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.shinhan.utils.JSONParsing;
import com.shinhan.utils.OpenAIRequest;

@WebServlet("/api/v1/chatting")
public class ChatAPIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONParsing jsonParsing = new JSONParsing();
		JSONObject json = jsonParsing.JSONParse(request);

		if (json == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("Error parsing JSON data!");
			return;
		}
		System.out.println(json);
		String content = (String) json.get("content");
		
		OpenAIRequest openai = new OpenAIRequest();
		openai.getChatbotResponse("gpt-3.5-turbo", content);
	}

}
