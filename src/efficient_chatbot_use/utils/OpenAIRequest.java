package efficient_chatbot_use.utils;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class OpenAIRequest {
    public static void main(String[] args) {
        // HttpClient 인스턴스 생성
        HttpClient client = HttpClient.newHttpClient();
        
        // HttpRequest 구성
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                // 이 부분에서 실제 API 키로 $OPENAI_API_KEY를 대체해야 합니다.
                .header("Authorization", "Bearer "+ DotEnv.getEnv("OPENAI_KEY"))
                .POST(HttpRequest.BodyPublishers.ofString("""
                    {
                      "model": "gpt-4-1106-preview",
                      "messages": [{"role": "user", "content": "대한민국의 수도는?"}],
                      "max_tokens": 200,
                      "temperature": 0.7
                    }
                    """))
                .build();

        try {
            // 요청 보내기 및 응답 받기
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 응답 출력
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response: " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
