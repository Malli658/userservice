package com.ibm.userservice.services;

import static java.lang.String.format;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ibm.userservice.exception.UserAlreadyExistException;
import com.ibm.userservice.exception.UserNotFoundException;
import com.ibm.userservice.model.User;
import com.ibm.userservice.repository.UserRepository;

@Service
public class UserServiceIml implements UserService,UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User save(User user) throws UserAlreadyExistException {
		if(userRepository.getUserByUserEmail(user.getEmail()).isPresent())
			throw new UserAlreadyExistException(user.getEmail()+": this email is already exist please login or try another email id ");
		user.setCreatedDate(LocalDateTime.now());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
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

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		return userRepository.getUserByUserName(username).orElseThrow(()->new UsernameNotFoundException(format("User with username - %s, not found", username)));
	}

	@Override
	public String updateProPic(Long id, String url) throws UserNotFoundException {
		User user=getUserByUserId(id);
		user.setUserProfilePicLink(url);
		userRepository.save(user);
		return "success";
	}

}
