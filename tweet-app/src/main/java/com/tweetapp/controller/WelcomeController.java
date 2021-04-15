package com.tweetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.model.UserDetails;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.WelcomeService;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class WelcomeController {

	@Autowired
	WelcomeService welcomeService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TweetRepository tweetRepository;
	
	@PostMapping("/register")
	public boolean userSignup(@RequestBody UserDetails userData) {
		boolean status = welcomeService.userRegistration(userData);
		return status;
	}
	
	@GetMapping("/search/user/{username}")
	public boolean userExist(@PathVariable("username") String username) {
		boolean status = welcomeService.getUserByUserName(username);
		return status;
	}
	
	@GetMapping("/search/email/{emailId}")
	public boolean emailExist(@PathVariable("emailId") String email) {
		boolean status = welcomeService.getUserEmail(email);
		return status;
	}
	
	@PutMapping("/forgot/{username}")
	public boolean forgotPassword(@PathVariable("username") String username, @RequestBody String password) {
		boolean status = welcomeService.processForgotPassword(username, password);
		return status;
	}
	
	/*
	 * @PostMapping("/login") public UserDetails userLogin(@RequestBody String json)
	 * { UserDetails loggedinUser = welcomeService.userLogin(json); return
	 * loggedinUser; }
	 */
	
}
