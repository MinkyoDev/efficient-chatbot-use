package com.shinhan.domain.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class ChatLogDTO {

	private int log_id;
	private int chat_id;
	private int history_id;
	private String model_name;
	private String request;
	private String response;
	private int prompt_tokens;
	private int completion_tokens;
	private Date create_at;

}
