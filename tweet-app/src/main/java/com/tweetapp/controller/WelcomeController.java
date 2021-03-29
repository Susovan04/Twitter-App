package com.tweetapp.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.model.UserDetails;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.WelcomeService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1.0/tweets")
public class WelcomeController {

	@Autowired
	WelcomeService welcomeService;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/register")
	public void userSignup(@RequestBody UserDetails userData) {
		System.out.println("---User Data---"+userData.toString());
		welcomeService.userRegistration(userData);
	}
	
	@GetMapping("/search/user/{username}")
	public boolean userExist(@PathVariable("username") String username) {
		System.out.println("---User Name---"+username);
		Optional<UserDetails> user = userRepository.findById(username);
		System.out.println(user.toString());
		if(user.isPresent()) {
			return true;
		}
		return false;
	}
	
	@GetMapping("/search/email/{emailId}")
	public boolean emailExist(@PathVariable("emailId") String email) {
		System.out.println("---Email---"+email);
		try {
			UserDetails user = userRepository.findByEmail(email);
			System.out.println(user.toString());
			return true;
		} catch (Exception e) {
			System.out.println("Exception Occured : "+e);
			return false;
		}
	}
	
	@PutMapping("/{username}/forgot")
	public void forgotPassword(@PathVariable("username") String username, @RequestBody String password) {
		System.out.println(username+"---"+password);
		Optional<UserDetails> user = userRepository.findById(username);
		user.get().setPassword(password);
		userRepository.save(user.get());
	}
	
	@PostMapping("/login")
	public String userLogin(@RequestBody String json) {
		System.out.println(json);
		JSONParser parse = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parse.parse(json);
			System.out.println(obj.get("loginUserId")+"--"+obj.get("loginPassword"));
			UserDetails loggedInUser = userRepository.getUser((String)obj.get("loginUserId"), (String) obj.get("loginPassword"));
			System.out.println(loggedInUser.toString());
			session.setAttribute("userDetails", loggedInUser);
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.writeValueAsString(loggedInUser.toString());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} catch (ParseException | NullPointerException e) {
			System.out.println("Exception : "+e);
		}
		return null;
	}
}
