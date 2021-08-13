package com.ibm.userservice.dto;

import java.util.Date;

import com.googlecode.jmapper.annotations.JGlobalMap;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JGlobalMap
public class SignupDTO {
	
	private String firstName;
	private String lastName;
	private String password;
	private String userName;
	private Date birthDate;
	private String userProfilePicLink;
	private String email;

}
