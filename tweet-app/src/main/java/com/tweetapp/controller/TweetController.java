package com.tweetapp.controller;

import java.util.List;
import java.util.UUID;

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
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.TweetService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetController {
		
	@Autowired
	TweetService tweetService;
	
	@Autowired
	UserRepository userRepository;
	
	
	@PutMapping("/{username}/reset")
	public boolean resetPassword(@PathVariable("username") String username, @RequestBody String newPassword) {
		boolean status = tweetService.processResetPassword(username, newPassword);
		return status;
	}
			
	@GetMapping("/users/all")
	public List<UserDetails> allUser() {
		return userRepository.findAll();
	}
	
	@PostMapping("/{username}/add")
	public boolean postTweet(@PathVariable("username") String username, @RequestBody String tweetData) {
		boolean status = tweetService.processTweetPost(username, tweetData); 
		return status;
	}
	
	@GetMapping("/tweets/all")
	public List<TweetData> allTweet() {		
		List<TweetData> tweetList = tweetService.showAllTweets();
		return tweetList;
	}
	
	@PostMapping("/{username}/reply/{id}")
	public boolean addReply(@PathVariable("username") String userName, @PathVariable("id") UUID tweetId, @RequestBody String replyData) {
		boolean status = tweetService.processAddReply(userName, tweetId, replyData);	
		return status;
	}
	
	@GetMapping("/{username}")
	public List<TweetData> getAllMyTweet(@PathVariable("username") String userName) {
		List<TweetData> tweetList = tweetService.getAllTweetsForSingleUser(userName);
		return tweetList;
	}
}
