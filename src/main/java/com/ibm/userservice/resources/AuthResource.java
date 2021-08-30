package com.ibm.userservice.resources;

import java.net.URI;

import io.swagger.annotations.Api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.googlecode.jmapper.JMapper;
import com.ibm.userservice.config.JwtTokenUtil;
import com.ibm.userservice.dto.AuthRequest;
import com.ibm.userservice.dto.SignupDTO;
import com.ibm.userservice.exception.UserAlreadyExistException;
import com.ibm.userservice.exception.UserNotFoundException;
import com.ibm.userservice.model.User;
import com.ibm.userservice.model.UserView;
import com.ibm.userservice.services.UserService;

@Api(tags = "Authentication")
@RestController @RequestMapping(path = "/api/public")
public class AuthResource {
	@Autowired
	private  AuthenticationManager authenticationManager;
	
	@Autowired
    private  JwtTokenUtil jwtTokenUtil;
	
	
	@Autowired
    private  UserService userService;
	
	@PostMapping("/login")
    public ResponseEntity<UserView> login(@RequestBody @Valid AuthRequest request) {
		JMapper<UserView, User> dataMapper=new JMapper<>(UserView.class,User.class);
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            User user = (User) authenticate.getPrincipal();
            return ResponseEntity.ok()
            		.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
            		.header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header")
                    .header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateAccessToken(user))
                    .body(dataMapper.getDestination(user));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
	
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
}
