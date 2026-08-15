package com.dcl.request;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.dcl.enums.RoleType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

@Data
public class RegisterRequest {
	
	@Email(message = "email cannot be empty")
	@NotBlank(message = "email cannot be empty")
	private String email;
	
	@NotBlank(message = "password cannot be empty")
    @Length(min=6,max=8,message="password must be contain more then 6 characters")
	private String password;
	
	@NotBlank(message = "first name cannot be empty")
	private String firstName;
	
	private String lastName;
	
	@NotNull(message = "dob cannot be empty")
    @Past(message = "dob is not valid")
	private LocalDate dob;
	
	@NotBlank(message = "phone cannot be empty")
    @Length(min=10,max=10,message="phone number must be of 10 digits")
	private String phone;
	
	private RoleType roleName;

}
