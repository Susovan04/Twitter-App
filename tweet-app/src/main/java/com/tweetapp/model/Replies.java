package com.tweetapp.model;

public class Replies {

	private String avatar;
	private String userName;
	private String time;
	private String reply;
	
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
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	@Override
	public String toString() {
		return "Replies [avatar=" + avatar + ", userName=" + userName + ", time=" + time + ", reply=" + reply + "]";
	}	
}
