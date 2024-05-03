package com.adas.app.leap.service;

import java.util.ArrayList;


import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.adas.app.leap.common.CommonFunc;

@Repository
public class JwtUserDetailsService implements UserDetailsService {
	

	@Autowired
	private CommonFunc utils;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		String userJsonDetails=utils.getUserDetails(username);
		
		JSONObject jsonObject = new JSONObject(userJsonDetails);
		
		String uid = "a";
		String pwd =null;

		
		try {
			uid = jsonObject.getJSONObject("data").getString("uid");
			pwd = jsonObject.getJSONObject("data").getString("pwd");
			
			
		}catch(Exception e) {
			System.out.println("Error in Auth:"+e.getMessage());
		}
		
		if (uid.equals(username)) {
			return new User(uid, pwd,
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		
		
	}

}