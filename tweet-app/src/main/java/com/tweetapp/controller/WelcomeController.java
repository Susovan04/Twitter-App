package com.tweetapp.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

import com.tweetapp.model.TweetData;
import com.tweetapp.model.UserDetails;
import com.tweetapp.repository.TweetRepository;
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
	
	@Autowired
	TweetRepository tweetRepository;
	
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
	
	@PutMapping("/{username}/reset")
	public void resetPassword(@PathVariable("username") String username, @RequestBody String newPassword) {
		System.out.println(username + "---" + newPassword);
		System.out.println(session.getAttribute("userDetails"));

		Optional<UserDetails> user = userRepository.findById(username);
		System.out.println(user.get().toString());
		user.get().setPassword(newPassword);
		userRepository.save(user.get());

	}
	
	@PostMapping("/login")
	public UserDetails userLogin(@RequestBody String json) {
		System.out.println(json);
		JSONParser parse = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parse.parse(json);
			System.out.println(obj.get("loginUserId")+"--"+obj.get("loginPassword"));
			UserDetails loggedInUser = userRepository.getUser((String)obj.get("loginUserId"), (String) obj.get("loginPassword"));
			System.out.println(loggedInUser.toString());
			return loggedInUser;
		} catch (ParseException | NullPointerException e) {
			System.out.println("Exception : "+e);
		}
		return null;
	}
	
	@GetMapping("/users/all")
	public List<UserDetails> allUser() {
		System.out.println(userRepository.findAll().toString());
		return userRepository.findAll();
	}
	
	@PostMapping("/{username}/add")
	public void postTweet(@PathVariable("username") String username, @RequestBody String tweetData) {
		TweetData tweet = new TweetData();
		System.out.println(username+"----"+tweetData);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	    Date date = new Date();  
	    System.out.println(formatter.format(date));  
	    String ex = "04/04/2021 20:08:55"; 
	    tweet.setUserName(username);
	    tweet.setTime(formatter.format(date));
	    tweet.setTweet(tweetData);
	    tweetRepository.insert(tweet);
		try {
			Date date1 = formatter.parse(formatter.format(date));
			Date date2 = formatter.parse(ex);
			
			long time_difference = date2.getTime() - date1.getTime();  
	        // Calucalte time difference in days  
	        long days_difference = (time_difference / (1000*60*60*24)) % 365;   
	        // Calucalte time difference in years  
	        long years_difference = (time_difference / (1000l*60*60*24*365));   
	        // Calucalte time difference in seconds  
	        long seconds_difference = (time_difference / 1000)% 60;   
	        // Calucalte time difference in minutes  
	        long minutes_difference = (time_difference / (1000*60)) % 60;   
	          
	        // Calucalte time difference in hours  
	        long hours_difference = (time_difference / (1000*60*60)) % 24;
	        
	        System.out.print(   
	                "Difference "  
	                + "between two dates is: ");   
	            System.out.println(   
	                hours_difference   
	                + " hours, "  
	                + minutes_difference   
	                + " minutes, "  
	                + seconds_difference   
	                + " seconds, "  
	                + years_difference   
	                + " years, "  
	                + days_difference   
	                + " days"  
	                );
	        
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	     
	}
}
