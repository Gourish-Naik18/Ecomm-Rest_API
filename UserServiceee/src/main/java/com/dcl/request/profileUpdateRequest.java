package com.dcl.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class profileUpdateRequest {
	
	private String firstName;
	
	private String lastName;
	
	private LocalDate dob;
	
	private String phone;

}
