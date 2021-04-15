package com.tweetapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tweetapp.service.AppUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AppUserDetailsService appUserDetailsService;
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(appUserDetailsService).passwordEncoder(passwordEncoder());
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
    	httpSecurity.cors();   	
    	httpSecurity.csrf().disable().httpBasic().and().authorizeRequests()
    	.antMatchers("/api/v1.0/tweets/register").anonymous()
    	.antMatchers("/api/v1.0/tweets/forgot/**").anonymous()
    	.antMatchers("/api/v1.0/tweets/search/**").anonymous()
    	.antMatchers("/api/v1.0/tweets/**").permitAll()
		.anyRequest().authenticated().and().addFilter(new JwtAuthorizationFilter(authenticationManager()));
    }
    
}
