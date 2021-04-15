package com.tweetapp.service;

import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.model.UserDetails;
import com.tweetapp.repository.UserRepository;

@Service
public class WelcomeServiceImpl implements WelcomeService{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public boolean userRegistration(UserDetails userData) {
		UserDetails user = userRepository.insert(userData);
		if(null != user.getLoginId())
			return true;
		return false;
	}

	@Override
	public boolean getUserByUserName(String username) {
		Optional<UserDetails> user = userRepository.findById(username);
		if(user.isPresent()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean getUserEmail(String email) {
		try {
			UserDetails user = userRepository.findByEmail(email);
			System.out.println(user.toString());
			return true;
		} catch (Exception e) {
			System.out.println("Exception Occured : "+e);
			return false;
		}
	}

	@Override
	public boolean processForgotPassword(String username, String password) {
		Optional<UserDetails> user = userRepository.findById(username);
		user.get().setPassword(password);
		UserDetails savedUser= userRepository.save(user.get());
		if(savedUser.getLoginId() != null)
			return true;
		return false;
	}

	@Override
	public UserDetails userLogin(String json) {
		JSONParser parse = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parse.parse(json);
			UserDetails loggedInUser = userRepository.getUser((String) obj.get("loginUserId"), (String) obj.get("loginPassword"));
			return loggedInUser;
		} catch (ParseException | NullPointerException e) {
			System.out.println("Exception : "+e);
			return null;
		}
	}

}
