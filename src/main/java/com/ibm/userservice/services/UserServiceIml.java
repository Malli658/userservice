package com.ibm.userservice.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.userservice.exception.UserAlreadyExistException;
import com.ibm.userservice.exception.UserNotFoundException;
import com.ibm.userservice.model.User;
import com.ibm.userservice.repository.UserRepository;

@Service
public class UserServiceIml implements UserService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public User save(User user) throws UserAlreadyExistException {
		if(userRepository.getUserByUserEmail(user.getEmail()).isPresent())
			throw new UserAlreadyExistException(user.getEmail()+": this email is already exist please login or try another email id ");
		user.setCreatedDate(LocalDateTime.now());
		user=userRepository.save(user);
		return user;
	}

	@Override
	public Boolean validateUserName(String userName) {
		Optional<User> user=userRepository.getUserByUserName(userName);
		if(user.isPresent())
			return true;
		else
			return false;
	}
	
	@Override
	public User getUserByUserName(String userName) throws UserNotFoundException {
		Optional<User> user=userRepository.getUserByUserName(userName);
		if(user.isPresent())
			return user.get();
		else
			throw new UserNotFoundException(userName);
	}
	
	@Override
	public User getUserByUserId(Long id) throws UserNotFoundException {
		Optional<User> user=userRepository.findById(id);
		if(user.isPresent())
			return user.get();
		else
			throw new UserNotFoundException("id:"+id);
	}

}
