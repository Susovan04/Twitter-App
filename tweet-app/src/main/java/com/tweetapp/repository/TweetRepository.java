package com.tweetapp.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.model.TweetData;

@Repository
public interface TweetRepository extends MongoRepository<TweetData, UUID>{
	
	List<TweetData> findByUserName(String userName);

}
