package com.rohan.stockapp.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="USERLOGIN")
public class User {
	
	@Id
	@GeneratedValue
	Long id;
	
	@Column(unique = true)
	String username;
	
	String password;
	
	String bio;
	
	LocalDateTime dateJoined;
	
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
	Set<Holding> holdings;
	
	public Set<Holding> getHoldings() {
		return holdings;
	}

	public void setHoldings(Set<Holding> holdings) {
		this.holdings = holdings;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public LocalDateTime getDateJoined() {
		return dateJoined;
	}

	public void setDateJoined(LocalDateTime dateJoined) {
		this.dateJoined = dateJoined;
	}

}
