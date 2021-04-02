package com.tweetapp.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "tweet_data")
public class TweetData {

	@Id
	private UUID id;
	private String userName;
	private String time;
	private String tweet;
			
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
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
		return "TweetData [id=" + id + ", userName=" + userName + ", time=" + time + ", tweet=" + tweet + "]";
	}
	
}
