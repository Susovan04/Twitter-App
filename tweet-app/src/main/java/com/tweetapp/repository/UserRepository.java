package com.tweetapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tweetapp.model.UserDetails;

@Repository
public interface UserRepository extends MongoRepository<UserDetails, String>{

	UserDetails findByEmail(String email);
	
	@Query(value = "{'_id':?0 , 'password':?1}", fields = "{'firstName':1,'lastName':1}")
	UserDetails getUser(String username, String password);
}
