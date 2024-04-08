package DTO;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class ChatDTO {

	private int chat_id;
	int history_id;
	String model_name;
	String request;
	String response;
	int prompt_tokens;
	int completion_tokens;
	Date create_at;
}
