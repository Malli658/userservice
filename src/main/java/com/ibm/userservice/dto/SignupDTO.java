package com.ibm.userservice.dto;

import java.util.Date;

import com.googlecode.jmapper.annotations.JGlobalMap;
import com.ibm.userservice.model.Gender;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JGlobalMap
public class SignupDTO {
	
	private Gender gender;
	private String password;
	private String userName;
	private String email;
	private String userProfilePicLink;

}
