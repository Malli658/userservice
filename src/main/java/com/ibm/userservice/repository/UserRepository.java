package com.ibm.userservice.repository;

import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ibm.userservice.model.User;

@Repository @CacheConfig(cacheNames = "users")
public interface UserRepository extends CrudRepository<User, Long>{
	
	@Query("SELECT u From User u where u.userName=?1")
	public Optional<User> getUserByUserName(String userName);
	@Query("SELECT u From User u where u.email=?1")
	public Optional<User> getUserByUserEmail(String email);
	
	@Query("SELECT u From User u where u.userName=?1")
	    @Cacheable
	    Optional<User> findByUsername(String username);
	
	@Caching(evict={
			@CacheEvict(key = "#p0.id"),
			@CacheEvict(key = "#p0.username")
	})
    <S extends User> S save(S entity);
	
}