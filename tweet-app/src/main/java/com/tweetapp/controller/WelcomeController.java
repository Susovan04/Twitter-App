package com.tweetapp.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

import com.tweetapp.model.Replies;
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
		String cuurentDate = getCurrentDate(); 
	    tweet.setUserName(username);
	    tweet.setAvatar(userRepository.findById(username).get().getImage());
	    tweet.setTime(cuurentDate);
	    tweet.setTweet(tweetData);
	    tweet.setId(UUID.randomUUID());
	    tweetRepository.insert(tweet);	     
	}

	private String timeDifference(String cuurentDate, String storedDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		try {
			Date date1 = formatter.parse(cuurentDate);
			Date date2 = formatter.parse(storedDate);
			
			long time_difference = date1.getTime() - date2.getTime();  
	        
			// Calculate time difference in days  
	        long days_difference = (time_difference / (1000*60*60*24)) % 365;   
	        
	        // Calculate time difference in years  
	        long years_difference = (time_difference / (1000l*60*60*24*365));   
			
	        /*
			 * // Calculate time difference in seconds long seconds_difference =
			 * (time_difference / 1000)% 60;
			 */ 
	        
	        // Calculate time difference in minutes  
	        long minutes_difference = (time_difference / (1000*60)) % 60;   
	          
	        // Calculate time difference in hours  
	        long hours_difference = (time_difference / (1000*60*60)) % 24;
	        
	        System.out.print(   
	                "Difference "  
	                + "between two dates is: ");   
	            System.out.println(   
	                hours_difference   
	                + " hours, "  
	                + minutes_difference   
	                + " minutes, "    
	                + years_difference   
	                + " years, "  
	                + days_difference   
	                + " days"  
	                );
	            
	            if(years_difference == 0) {
	            	if(days_difference == 0) {
	            		if(hours_difference == 0) {
	            			if(minutes_difference == 0) {
	            				return "just now";
	            			} else {
	            				return String.valueOf(minutes_difference) + "minutes ago";
	            			}
	            		} else {
	            			return String.valueOf(hours_difference) + "hours ago";
	            		}
	            	} else {
	            		return String.valueOf(days_difference) + "days ago";
	            	}
	            } else {
	            	return String.valueOf(years_difference) + "year ago";
	            }
	        
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		 return "Error fetching time";
	}
	
	@GetMapping("/tweets/all")
	public List<TweetData> allTweet() {
		List<TweetData> tweetList = new ArrayList<TweetData>();
		for(TweetData tweet : tweetRepository.findAll()) {
			String timeDiff = timeDifference(getCurrentDate(), tweet.getTime());
			tweet.setTime(timeDiff);
			for(Replies reply : tweet.getReplies()) {
				String replyTime = timeDifference(getCurrentDate(), reply.getTime());
				reply.setTime(replyTime);
			}
			tweetList.add(tweet);
		}
		return tweetList;
	}
	
	@PostMapping("/{username}/reply/{id}")
	public void addReply(@PathVariable("username") String userName, @PathVariable("id") UUID tweetId, @RequestBody String replyData) {
		Optional<TweetData> tweet = tweetRepository.findById(tweetId);
		
		Replies reply = new Replies();
		reply.setTime(getCurrentDate());
		reply.setReply(replyData);
		reply.setUserName(userName);
		reply.setAvatar(userRepository.findById(userName).get().getImage());
		tweet.get().getReplies().add(reply);
		System.out.println("------"+tweet.get());
		tweetRepository.save(tweet.get());
	}
	
	@GetMapping("/{username}")
	public List<TweetData> getAllMyTweet(@PathVariable("username") String userName) {
		List<TweetData> tweetList = new ArrayList<TweetData>();
		for(TweetData tweet : tweetRepository.findByUserName(userName)) {
			String timeDiff = timeDifference(getCurrentDate(), tweet.getTime());
			tweet.setTime(timeDiff);
			for(Replies reply : tweet.getReplies()) {
				String replyTime = timeDifference(getCurrentDate(), reply.getTime());
				reply.setTime(replyTime);
			}
			tweetList.add(tweet);
		}
		return tweetList;
	}
	
	public String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	    Date date = new Date();  
	    System.out.println(formatter.format(date)); 
	    return formatter.format(date);
	}
}
