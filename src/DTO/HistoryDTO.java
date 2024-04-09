package DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class HistoryDTO {

	int chat_id;
	int deps;
	String summary;
	int prompt_tokens;
	int completion_tokens;
}
