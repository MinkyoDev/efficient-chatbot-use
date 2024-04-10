package DTO;

public class HistoryDTO {

	private int chat_id;
	private int deps;
	private String summary;
	private int prompt_tokens;
	private int completion_tokens;

	public int getChat_id() {
		return chat_id;
	}

	public void setChat_id(int chat_id) {
		this.chat_id = chat_id;
	}

	public int getDeps() {
		return deps;
	}

	public void setDeps(int deps) {
		this.deps = deps;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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
		return "HistoryDTO [chat_id=" + chat_id + ", deps=" + deps + ", summary=" + summary + ", prompt_tokens="
				+ prompt_tokens + ", completion_tokens=" + completion_tokens + "]";
	}

}
