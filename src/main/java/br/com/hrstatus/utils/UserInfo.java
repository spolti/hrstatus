package br.com.hrstatus.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.hrstatus.dao.Iteracoes;
import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.model.Users;

public class UserInfo {

	@Autowired
	private UsersInterface userDAO;
	
	@Autowired
	private Iteracoes iteracoesDAO;
	
	
	public String getLoggedUsername(){
		Object  LoggedObjectUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String LoggedUsername = ((UserDetails) LoggedObjectUser).getUsername();
		return LoggedUsername;
	}	
	
	public boolean listLogFiles(Users user, int  id){
		boolean val = false;
	
		for (Servidores u1 : user.getServer()){
			if (id == u1.getId()){
				val = true;
			}
		}
		return val;
	}
}