package DTO;

public class ChatDTO {

	private int chat_id;
	private String user_id;
	private String name;
	private boolean memory_enabled;
	private boolean ceche_enabled;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMemory_enabled() {
		return memory_enabled;
	}

	public void setMemory_enabled(boolean memory_enabled) {
		this.memory_enabled = memory_enabled;
	}

	public boolean isCeche_enabled() {
		return ceche_enabled;
	}

	public void setCeche_enabled(boolean ceche_enabled) {
		this.ceche_enabled = ceche_enabled;
	}

	@Override
	public String toString() {
		return "ChatDTO [chat_id=" + chat_id + ", user_id=" + user_id + ", name=" + name + ", memory_enabled="
				+ memory_enabled + ", ceche_enabled=" + ceche_enabled + "]";
	}

}
