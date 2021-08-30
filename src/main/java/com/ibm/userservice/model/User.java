package com.ibm.userservice.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table
@Setter
@Getter
@RequiredArgsConstructor
public class User implements UserDetails, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Gender gender;
	private String password;
	private String userName;
	private LocalDateTime createdDate;
	private String email;
	private String userProfilePicLink;
	
	public User(String username, String password) {
        this.userName = username;
        this.password = password;
        this.enabled = true;
    }
	
	private boolean enabled = true;
	
	@ElementCollection
    @CollectionTable(name="Roles", joinColumns= @JoinColumn(name="id"))
    //@Column(name="CONTACT_NO")
	private Set<Role> authorities = new HashSet<>();
	
	/*@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.enabled;
	}*/
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.userName;
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return this.enabled;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return this.enabled;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return this.enabled;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.enabled;
	}
}
