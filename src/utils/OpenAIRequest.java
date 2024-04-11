package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import DTO.ChatLogDTO;
import DTO.HistoryDTO;

public class OpenAIRequest {

	private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    
	HttpClient client = HttpClient.newHttpClient();

    // 공통 HTTP 요청 메소드
    private static String sendHttpRequest(String jsonInputString, String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + DotEnv.getEnv("OPENAI_KEY"));
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        connection.disconnect();
        
        return response.toString();
    }

    public JSONObject chatBot(String modelName, String prompt) {
        String jsonInputString = String.format(
                "{\"model\": \"%s\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"max_tokens\": 500, \"temperature\": 0.7}",
                modelName, prompt);
        try {
            String response = sendHttpRequest(jsonInputString, OPENAI_URL);
            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String chatBotStream(String modelName, String prompt) {
        String jsonInputString = String.format(
                "{" +
                "\"model\": \"%s\"," +
                "\"messages\": [" +
                "  {\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}," +
                "  {\"role\": \"user\", \"content\": \"%s\"}]," +
                "\"stream\": true" +
                "}", modelName, prompt);

        try {
            URL url = new URL(OPENAI_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + DotEnv.getEnv("OPENAI_KEY"));
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0); // 스트리밍 모드 활성화

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            String contents = "";
            // 서버로부터 스트리밍 응답 처리
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                	if (responseLine.startsWith("data: ")) {
						String jsonData = responseLine.substring(6);
						try {
							JSONObject jsonObject = new JSONObject(jsonData);
							String content = jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("delta")
									.getString("content");
							System.out.print(content);
							contents += content;
						} catch (Exception e) {
						}
					}
                }
            }
            System.out.println();
            connection.disconnect();
            return contents;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	public ChatLogDTO makeChat(int chatid, String modelName, String input, JSONObject jr) {
		ChatLogDTO chatDTO = new ChatLogDTO();
		try {
			chatDTO.setChat_id(chatid);
			chatDTO.setModel_name(modelName);
			chatDTO.setRequest(input);
			chatDTO.setResponse(
					jr.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content"));
			chatDTO.setPrompt_tokens(jr.getJSONObject("usage").getInt("prompt_tokens"));
			chatDTO.setCompletion_tokens(jr.getJSONObject("usage").getInt("completion_tokens"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return chatDTO;
	}
	
	public ChatLogDTO makeChat(int chatid, String modelName, String input, String contents) {
		ChatLogDTO chatDTO = new ChatLogDTO();
		chatDTO.setChat_id(chatid);
		chatDTO.setModel_name(modelName);
		chatDTO.setRequest(input);
		chatDTO.setResponse(contents);
		chatDTO.setPrompt_tokens(0);
		chatDTO.setCompletion_tokens(0);
		return chatDTO;
	}

	public HistoryDTO makeHistory(JSONObject jr, int chatid) {
		HistoryDTO history = new HistoryDTO();
		try {
			history.setChat_id(chatid);
			history.setDeps(0);
			history.setSummary(
					jr.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content"));
			history.setPrompt_tokens(jr.getJSONObject("usage").getInt("prompt_tokens"));
			history.setCompletion_tokens(jr.getJSONObject("usage").getInt("completion_tokens"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return history;
	}
}
