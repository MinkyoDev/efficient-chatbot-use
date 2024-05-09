package com.shinhan.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class HistoryDTO {

	private int chat_id;
	private int deps;
	private String summary;
	private int prompt_tokens;
	private int completion_tokens;

}
