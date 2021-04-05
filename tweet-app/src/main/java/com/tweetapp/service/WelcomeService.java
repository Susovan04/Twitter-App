package com.tweetapp.service;

import com.tweetapp.model.UserDetails;

public interface WelcomeService {
	
	public boolean userRegistration(UserDetails userData);
	
	public boolean getUserByUserName(String username);

	public boolean getUserEmail(String email);

	public boolean processForgotPassword(String username, String password);

	public UserDetails userLogin(String json);

}
