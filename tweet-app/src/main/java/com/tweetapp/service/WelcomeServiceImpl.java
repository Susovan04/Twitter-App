package com.tweetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.model.UserDetails;
import com.tweetapp.repository.UserRepository;

@Service
public class WelcomeServiceImpl implements WelcomeService{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public String userRegistration(UserDetails userData) {
		userRepository.insert(userData);
		return null;
	}

}
