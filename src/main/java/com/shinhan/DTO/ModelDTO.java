package com.shinhan.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class ModelDTO {

	private String name;
	private String company;
	private double price_per_ptoken;
	private double price_per_ctoken;

}
