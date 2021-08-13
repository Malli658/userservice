package com.ibm.userservice.exception;

public class UserAlreadyExistException extends Exception{
   public UserAlreadyExistException(String msg){
	   super(msg);
   }
}
