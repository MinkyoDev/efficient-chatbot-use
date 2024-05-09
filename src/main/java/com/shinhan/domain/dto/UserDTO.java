package com.shinhan.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class UserDTO {

	private String email;
	private String nicname;
	private String password;
	private boolean is_active = true;

}
