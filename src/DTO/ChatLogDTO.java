package DTO;

import java.sql.Date;

public class ChatLogDTO {

	private int chat_id;
	private int history_id;
	private String model_name;
	private String request;
	private String response;
	private int prompt_tokens;
	private int completion_tokens;
	private Date create_at;

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

	public String getModel_name() {
		return model_name;
	}

	public void setModel_name(String model_name) {
		this.model_name = model_name;
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

	public Date getCreate_at() {
		return create_at;
	}

	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
	}

	@Override
	public String toString() {
		return "ChatDTO [chat_id=" + chat_id + ", history_id=" + history_id + ", model_name=" + model_name
				+ ", request=" + request + ", response=" + response + ", prompt_tokens=" + prompt_tokens
				+ ", completion_tokens=" + completion_tokens + ", create_at=" + create_at + "]";
	}

}
