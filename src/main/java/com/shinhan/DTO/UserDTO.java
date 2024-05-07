package com.shinhan.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class UserDTO {

	private String id;
	private String password;
	private boolean is_active = true;

}
