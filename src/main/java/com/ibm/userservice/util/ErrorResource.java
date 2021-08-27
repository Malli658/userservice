package com.ibm.userservice.util;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ErrorResource {
	private Integer code;
	private String property;
	private String message;
	private String developerMessage;
	private StackTraceElement moreInfo;
	
	public ErrorResource(String message) {
		this.message=message;
	}
	public ErrorResource(String message, String property) {
		this(message);
		this.property = property;
	}

	public ErrorResource(String message, String property,
			String developerMessage) {
		this(message, property);
		this.developerMessage = developerMessage;
	}
}
