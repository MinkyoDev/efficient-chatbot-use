package utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import DTO.ChatDTO;
import DTO.HistoryDTO;

public class OpenAIRequest {

	HttpClient client = HttpClient.newHttpClient();

	public JSONObject chatBot(String input, HashMap<String, String> datas) {
    	String content = datas.get("summarys") + "\n" + datas.get("prompt");

    	// JSON 객체 생성
    	JSONObject jsonBody = new JSONObject();
    	jsonBody.put("model", "gpt-3.5-turbo-1106");
    	jsonBody.put("messages", new JSONArray().put(new JSONObject().put("role", "user").put("content", content)));
    	jsonBody.put("max_tokens", 300);
    	jsonBody.put("temperature", 0.7);

    	// 요청 생성
    	HttpRequest request = HttpRequest.newBuilder()
    	        .uri(URI.create("https://api.openai.com/v1/chat/completions"))
    	        .header("Content-Type", "application/json")
    	        .header("Authorization", "Bearer " + DotEnv.getEnv("OPENAI_KEY"))
    	        .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
    	        .build();
    	
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			JSONObject jr = new JSONObject(response.body());
			return jr;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ChatDTO makeChat(JSONObject jr, String input) {
		ChatDTO chatDTO = new ChatDTO();
		chatDTO.setModel_name("gpt-3.5-turbo-1106");
		chatDTO.setRequest(input);
		chatDTO.setResponse(jr.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content"));
		chatDTO.setPrompt_tokens(jr.getJSONObject("usage").getInt("prompt_tokens"));
		chatDTO.setCompletion_tokens(jr.getJSONObject("usage").getInt("completion_tokens"));

		return chatDTO;
	}

	public JSONObject generateSummary(int chatid, String prompt) {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.openai.com/v1/chat/completions"))
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + DotEnv.getEnv("OPENAI_KEY"))
				.POST(HttpRequest.BodyPublishers.ofString(String.format("""
						{
						  "model": "gpt-3.5-turbo-1106",
						  "messages": [{"role": "user", "content": "%s"}],
						  "max_tokens": 300,
						  "temperature": 0.7
						}
						""", prompt))).build();
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			JSONObject jr = new JSONObject(response.body());
			return jr;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public HistoryDTO makeHistory(JSONObject jr, int chatid) {
		HistoryDTO history = new HistoryDTO();
		history.setChat_id(0);
		history.setDeps(0);
		history.setSummary(jr.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content"));
		history.setPrompt_tokens(jr.getJSONObject("usage").getInt("prompt_tokens"));
		history.setCompletion_tokens(jr.getJSONObject("usage").getInt("completion_tokens"));
		return history;
	}
}
