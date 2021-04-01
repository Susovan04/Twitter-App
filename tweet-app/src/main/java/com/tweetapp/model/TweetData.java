package com.tweetapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "tweet_data")
public class TweetData {
	
	@Id
	private String userName;
	private String time;
	private String tweet;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTweet() {
		return tweet;
	}
	public void setTweet(String tweet) {
		this.tweet = tweet;
	}
	@Override
	public String toString() {
		return "TweetData [userName=" + userName + ", time=" + time + ", tweet=" + tweet + "]";
	}
}