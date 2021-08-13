package com.ibm.userservice.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.googlecode.jmapper.JMapper;
import com.ibm.userservice.dto.SignupDTO;
import com.ibm.userservice.exception.UserAlreadyExistException;
import com.ibm.userservice.exception.UserNotFoundException;
import com.ibm.userservice.model.User;
import com.ibm.userservice.services.UserService;

@RestController
@RequestMapping("/user")
public class UserResources {
	
	@Autowired
	private UserService userService;
	
	@PostMapping(value="/save")
	public ResponseEntity<Object> save(@RequestBody SignupDTO signUp) throws UserAlreadyExistException{
		JMapper<User, SignupDTO> newsfeedMapper=new JMapper<>(User.class, SignupDTO.class);
		User user=newsfeedMapper.getDestination(signUp);
		user=userService.save(user);
		URI uri=ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping(value="/get/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Long userId) throws UserNotFoundException{
		User user=userService.getUserByUserId(userId);
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
	
	@GetMapping(value="/get/name")
	public ResponseEntity<User> getUserByName(@RequestParam String name) throws UserNotFoundException{
		User user=userService.getUserByUserName(name);
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
	
	@GetMapping(value="/user-name/validate")
	public ResponseEntity<Boolean> checkUserName(String name){
		Boolean result=userService.validateUserName(name);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
