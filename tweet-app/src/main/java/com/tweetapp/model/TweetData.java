package com.tweetapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "tweet_data")
public class TweetData {

	@Id
	private UUID id;
	private String avatar;
	private String userName;
	private String time;
	private String tweet;
	private List<Replies> replies;	
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
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
	public List<Replies> getReplies() {
		if(replies == null) {
			replies = new ArrayList<Replies>();
		}
		return replies;
	}
	public void setReplies(List<Replies> replies) {
		this.replies = replies;
	}
	@Override
	public String toString() {
		return "TweetData [id=" + id + ", avatar=" + avatar + ", userName=" + userName + ", time=" + time + ", tweet="
				+ tweet + ", replies=" + replies + "]";
	}
}
