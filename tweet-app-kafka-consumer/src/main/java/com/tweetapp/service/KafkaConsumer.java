package com.tweetapp.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
		
	@KafkaListener(topics = "Tweet", groupId = "group_id")
	public void consumeTweet(String message) {
		System.out.println("Consumed Tweet : "+message);
	}
	
	@KafkaListener(topics = "Reply", groupId = "group_id")
	public void consumeReply(String message) {
		System.out.println("Consumed Reply : "+message);
	}

}
