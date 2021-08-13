package com.ibm.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ibm.userservice.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	/*@Query("SELECT n From NewsFeed n where n.userId=?1")
     Page<NewsFeed> getNewsFeed(Long userID,Pageable pagable);*/
	@Query("SELECT u From User u where u.userName=?1")
	public Optional<User> getUserByUserName(String userName);
	@Query("SELECT u From User u where u.email=?1")
	public Optional<User> getUserByUserEmail(String email);
	
}