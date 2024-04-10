package DTO;

public class LogDTO {

	private int chat_id;
	private String user_id;
	private String request;
	private String response;

	public int getChat_id() {
		return chat_id;
	}

	public void setChat_id(int chat_id) {
		this.chat_id = chat_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	@Override
	public String toString() {
		return "LogDTO [chat_id=" + chat_id + ", user_id=" + user_id + ", request=" + request + ", response=" + response
				+ "]";
	}

}
