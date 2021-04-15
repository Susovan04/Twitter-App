
package com.tweetapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tweetapp.repository.UserRepository;
import com.tweetapp.security.AppUser;

@Service
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<com.tweetapp.model.UserDetails> user = userRepository.findById(username);

		if (user.get().getLoginId() == null) {
			throw new UsernameNotFoundException("Username is not found");
		}

		return new AppUser(user.get());
	}

}
