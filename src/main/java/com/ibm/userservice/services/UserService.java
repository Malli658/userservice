package com.ibm.userservice.services;

import com.ibm.userservice.exception.UserAlreadyExistException;
import com.ibm.userservice.exception.UserNotFoundException;
import com.ibm.userservice.model.User;

public interface UserService {
    User save(User user) throws UserAlreadyExistException;
    public Boolean validateUserName(String userName);
    public User getUserByUserName(String userName) throws UserNotFoundException;
    public User getUserByUserId(Long id) throws UserNotFoundException;
    
    public String updateProPic(Long id,String url) throws UserNotFoundException;
}
