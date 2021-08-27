package com.ibm.userservice.resources.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ibm.userservice.exception.UserAlreadyExistException;
import com.ibm.userservice.exception.UserNotFoundException;
import com.ibm.userservice.util.ErrorResource;

@ControllerAdvice
public class UserExceptionHandler {
	
	@ExceptionHandler(value = UserAlreadyExistException.class)
	public ResponseEntity<ErrorResource> userAlreadyExist(UserAlreadyExistException userAlreadyExistException){
		ErrorResource er=new ErrorResource(userAlreadyExistException.getMessage());
		er.setCode(4000);
		er.setMoreInfo(userAlreadyExistException.getStackTrace()[0]);
		return new ResponseEntity<ErrorResource>(er,HttpStatus.OK);
	}
	
	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<ErrorResource> userNotFoundException(UserNotFoundException userAlreadyExistException){
		ErrorResource er=new ErrorResource(userAlreadyExistException.getMessage());
		er.setCode(4001);
		er.setMoreInfo(userAlreadyExistException.getStackTrace()[0]);
		return new ResponseEntity<ErrorResource>(er,HttpStatus.OK);
	}

}
