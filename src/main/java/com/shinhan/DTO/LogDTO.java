package com.shinhan.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class LogDTO {

	private int log_id;
	private int chat_id;
	private int history_id;
	private String request;
	private String response;
	private int prompt_tokens;
	private int completion_tokens;

}
