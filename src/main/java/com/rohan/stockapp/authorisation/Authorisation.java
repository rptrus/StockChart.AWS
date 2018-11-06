package com.rohan.stockapp.authorisation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rohan.stockapp.entity.User;
import com.rohan.stockapp.repository.UserRepository;

@Component
public class Authorisation {
	
	@Autowired
	UserRepository userRepository;
	
	public boolean auth(String user) {
		User aUser = userRepository.findByUsername(user);
		return aUser != null && aUser.getId() > 0L;
	}

}
