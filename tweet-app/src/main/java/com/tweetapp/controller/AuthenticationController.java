package com.tweetapp.controller;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class AuthenticationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);
	
	@GetMapping("/authenticate/login")
	public Map<String, String> authenticate(@RequestHeader("Authorization") String authHeader) {
		System.out.println("header-------"+authHeader);
		Map<String, String> jwt = new HashMap<String, String>();
		String user = getUser(authHeader);
		String token = generateJwt(user);
		jwt.put("token", token);
		jwt.put("user", user);
		return jwt;
	}
	
	private String getUser(String authHeader) {
		String encodedCredentials = authHeader.split(" ")[1];
		byte[] credentials = Base64.getDecoder().decode(encodedCredentials);
		String user = new String(credentials).split(":")[0];
		LOGGER.info("User -> " + user);
		return user;
	}
	
	private String generateJwt(String user) {
		JwtBuilder builder = Jwts.builder();
		builder.setSubject(user);

		// Set the token issue time as current time
		builder.setIssuedAt(new Date());

		// Set the token expiry as 20 minutes from now
		builder.setExpiration(new Date((new Date()).getTime() + 1200000));
		builder.signWith(SignatureAlgorithm.HS256, "secretkey");

		String token = builder.compact();

		return token;
	}
	
}
