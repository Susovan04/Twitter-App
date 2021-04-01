package com.tweetapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.model.TweetData;

@Repository
public interface TweetRepository extends MongoRepository<TweetData, String>{

}
