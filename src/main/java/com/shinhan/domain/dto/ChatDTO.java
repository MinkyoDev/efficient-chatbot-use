package com.shinhan.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class ChatDTO {

	private int chat_id;
	private String user_email;
	private String model;
	private String name;
	private boolean stream_enabled;
	private boolean memory_enabled;
	private boolean ceche_enabled;

}
