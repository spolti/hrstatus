package br.com.hrstatus.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserInfo {

	public String getLoggedUsername(){
		Object  LoggedObjectUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String LoggedUsername = ((UserDetails) LoggedObjectUser).getUsername();
		return LoggedUsername;
	}
	
	
}
