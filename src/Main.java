import utils.OpenAIRequest;

public class Main {

	public static void main(String[] args) {
		String message = "안녕?";
		
		OpenAIRequest openAIRequest = new OpenAIRequest();
		String response = openAIRequest.chatBot(message);
		System.out.println(response);
	}
	
	
}
