package com.tweetapp.service;

import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tweetapp.model.UserDetails;
import com.tweetapp.repository.UserRepository;

@Service
public class WelcomeServiceImpl implements WelcomeService{
	
	@Autowired
	UserRepository userRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WelcomeServiceImpl.class);

	@Override
	public boolean userRegistration(UserDetails userData) {
		userData.setPassword(passwordEncoder().encode(userData.getPassword()));
		UserDetails user = userRepository.insert(userData);
		if(null != user.getLoginId()) {
			LOGGER.info("User Registered Successfully");
			return true;
		}
		LOGGER.error("User Registered UnSuccessful");
		return false;
	}

	@Override
	public boolean getUserByUserName(String username) {
		Optional<UserDetails> user = userRepository.findById(username);
		if(user.isPresent()) {
			LOGGER.info("An User is Present with the username : "+ username);
			return true;
		}
		LOGGER.info("No User is Present with this username : "+username);
		return false;
	}

	@Override
	public boolean getUserEmail(String email) {
		try {
			UserDetails user = userRepository.findByEmail(email);
			LOGGER.info("User present with Email Id : " + user.toString());
			return true;
		} catch (Exception e) {
			LOGGER.error("Exception Occured while finding user with EmailId : "+e);
			return false;
		}
	}

	@Override
	public boolean processForgotPassword(String username, String password) {
		Optional<UserDetails> user = userRepository.findById(username);
		user.get().setPassword(passwordEncoder().encode(password));
		UserDetails savedUser= userRepository.save(user.get());
		if(savedUser.getLoginId() != null) {
			LOGGER.info("Password Updated Successfully");
			return true;
		}
		LOGGER.error("Error Occured in Forgot Password");
		return false;
	}

	@Override
	public UserDetails userLogin(String json) {
		JSONParser parse = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parse.parse(json);
			UserDetails loggedInUser = userRepository.getUser((String) obj.get("loginUserId"), (String) obj.get("loginPassword"));
			LOGGER.info("Login Successful");
			return loggedInUser;
		} catch (ParseException | NullPointerException e) {
			LOGGER.error("Exception in Login : " +e);
			return null;
		}
	}

	private PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
