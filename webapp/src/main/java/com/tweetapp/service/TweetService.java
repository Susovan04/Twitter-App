package com.tweetapp.service;

import java.util.List;
import java.util.UUID;

import com.tweetapp.model.TweetData;

public interface TweetService {

	public boolean processResetPassword(String username, String newPassword);

	public boolean processTweetPost(String username, String tweetData);

	public List<TweetData> showAllTweets();

	public boolean processAddReply(String userName, UUID tweetId, String replyData);

	public List<TweetData> getAllTweetsForSingleUser(String userName);

}
