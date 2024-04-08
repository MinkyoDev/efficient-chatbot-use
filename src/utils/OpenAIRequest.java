package utils;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import DTO.ChatDTO;

public class OpenAIRequest {
	
	HttpClient client = HttpClient.newHttpClient();
	
    public String chatBot(String prompt) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+ DotEnv.getEnv("OPENAI_KEY"))
                .POST(HttpRequest.BodyPublishers.ofString(String.format("""
                    {
                      "model": "gpt-3.5-turbo-1106",
                      "messages": [{"role": "user", "content": "%s"}],
                      "max_tokens": 200,
                      "temperature": 0.7
                    }
                    """, prompt)))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            ChatDTO chatDTO = makeChat(jsonResponse, prompt);
            
            return chatDTO.getResponse();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ChatDTO makeChat(JSONObject jsonResponse, String prompt) {
    	ChatDTO chatDTO = new ChatDTO();
    	chatDTO.setModel_name("gpt-3.5-turbo-1106");
    	chatDTO.setRequest(prompt);
    	chatDTO.setResponse(jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content"));
    	chatDTO.setPrompt_tokens(jsonResponse.getJSONObject("usage").getInt("prompt_tokens"));
    	chatDTO.setCompletion_tokens(jsonResponse.getJSONObject("usage").getInt("completion_tokens"));
    	
    	return chatDTO;
	}
}
