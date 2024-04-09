package DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class LogDTO {

	int chat_id;
	String user_id;
	String request;
	String response;
}
