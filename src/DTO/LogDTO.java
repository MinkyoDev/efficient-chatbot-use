package DTO;

public class LogDTO {

	private int log_id;
	private int chat_id;
	private int history_id;
	private String request;
	private String response;
	private int prompt_tokens;
	private int completion_tokens;

	public int getLog_id() {
		return log_id;
	}

	public void setLog_id(int log_id) {
		this.log_id = log_id;
	}

	public int getChat_id() {
		return chat_id;
	}

	public void setChat_id(int chat_id) {
		this.chat_id = chat_id;
	}

	public int getHistory_id() {
		return history_id;
	}

	public void setHistory_id(int history_id) {
		this.history_id = history_id;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getPrompt_tokens() {
		return prompt_tokens;
	}

	public void setPrompt_tokens(int prompt_tokens) {
		this.prompt_tokens = prompt_tokens;
	}

	public int getCompletion_tokens() {
		return completion_tokens;
	}

	public void setCompletion_tokens(int completion_tokens) {
		this.completion_tokens = completion_tokens;
	}

	@Override
	public String toString() {
		return "LogDTO [log_id=" + log_id + ", chat_id=" + chat_id + ", history_id=" + history_id + ", request="
				+ request + ", response=" + response + ", prompt_tokens=" + prompt_tokens + ", completion_tokens="
				+ completion_tokens + "]";
	}

}
