package com.tweetapp.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.model.Replies;
import com.tweetapp.model.TweetData;
import com.tweetapp.model.UserDetails;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;

@Service
public class TweetServiceImpl implements TweetService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TweetRepository tweetRepository;

	@Override
	public boolean processResetPassword(String username, String newPassword) {
		Optional<UserDetails> user = userRepository.findById(username);
		user.get().setPassword(newPassword);
		UserDetails savedUser = userRepository.save(user.get());
		if(savedUser.getLoginId() != null)
			return true;
		return false;
	}

	@Override
	public boolean processTweetPost(String username, String tweetData) {
		String cuurentDate = getCurrentDate(); 		
		TweetData tweet = new TweetData();
	    tweet.setUserName(username);
	    tweet.setAvatar(userRepository.findById(username).get().getImage());
	    tweet.setTime(cuurentDate);
	    tweet.setTweet(tweetData);
	    tweet.setId(UUID.randomUUID());	    
	    TweetData postedTweet = tweetRepository.insert(tweet);
	    if(postedTweet.getId() != null)
	    	return true;
	    return false;
	}
	
	@Override
	public List<TweetData> showAllTweets() {
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
	
	@Override
	public boolean processAddReply(String userName, UUID tweetId, String replyData) {
		
		Optional<TweetData> tweet = tweetRepository.findById(tweetId);	
		
		Replies reply = new Replies();
		reply.setTime(getCurrentDate());
		reply.setReply(replyData);
		reply.setUserName(userName);
		reply.setAvatar(userRepository.findById(userName).get().getImage());
		
		tweet.get().getReplies().add(reply);
		TweetData savedTweet = tweetRepository.save(tweet.get());
		
		if(savedTweet.getId() != null)
			return true;
		return false;
	}
	
	@Override
	public List<TweetData> getAllTweetsForSingleUser(String userName) {
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
	
	private String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	    Date date = new Date();
	    return formatter.format(date);
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
			
	        // Calculate time difference in minutes  
	        long minutes_difference = (time_difference / (1000*60)) % 60;   
	          
	        // Calculate time difference in hours  
	        long hours_difference = (time_difference / (1000*60*60)) % 24;
	        	            
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
}
