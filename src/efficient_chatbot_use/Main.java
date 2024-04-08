package efficient_chatbot_use;

import efficient_chatbot_use.utils.DotEnv;

public class Main {

	public static void main(String[] args) {
		System.out.println(DotEnv.getEnv("ENV1"));
	}
	
	
}
