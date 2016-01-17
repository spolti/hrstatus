/*
    Copyright (C) 2012  Filippe Costa Spolti

	This file is part of Hrstatus.

    Hrstatus is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.hrstatus.rest.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.model.Users;
import br.com.hrstatus.rest.UserResource;
import br.com.hrstatus.security.SpringEncoder;
import br.com.hrstatus.utils.PassGenerator;
import br.com.hrstatus.utils.UserInfo;
import br.com.hrstatus.utils.mail.MailSender;

/*
 * @author spolti
 */

@Component
public class UserImpl extends SpringBeanAutowiringSupport implements UserResource{
	
	Logger log = Logger.getLogger(UserImpl.class.getCanonicalName());
	
	@Autowired(required = true)
	private UsersInterface userDAO;
	@Autowired
	private Configuration configurationDAO;
	private UserInfo userInfo = new UserInfo();
	private Users user = new Users();
	
	@PostConstruct
	public void init() {
		log.info("initializing Autowired Service.");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	public List<Users> users() {
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> listing users.");
			return this.userDAO.listUser();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Users> blockedUsers() {
		// TODO Auto-generated method stub
		return this.userDAO.getBlockedUsers();
	}

	public String removeUser(String username) {
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> removing user " + username);
			this.userDAO.deleteUserByID(this.userDAO.getUserByID(username));
			return "SUCESS";
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to remove user " + username + ", see the logs for details.";
		}
	}

	@SuppressWarnings("static-access")
	public String newUser(String username, String password, String role,
			String name, String mail, boolean enabled) {
		
		// Regex to e-mail validation
		Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
		Matcher m = p.matcher(mail);
		SpringEncoder encode = new SpringEncoder();
		
		role = "ROLE_" + role.toUpperCase();
		log.fine(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Parameters received:");
		log.fine(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Username: " + username);
		log.fine(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Password: gotcha!");
		log.fine(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Role: " + role);
		log.fine(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Name: " + name);
		log.fine(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Email: " + mail);
		log.fine(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Enabled? " + enabled);
		
		if (username.isEmpty() || password.isEmpty() || role.isEmpty() || name.isEmpty() || mail.isEmpty()) {
			return "Some parameter is missing";
		}
		
		//password validation
		if (password.equals("auto")) {
			log.fine(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Password = auto -- generating password");
			PassGenerator gemPass = new PassGenerator();
			password = gemPass.gemPass();
			
		} else if (!password.equals("auto")) {
			//Verifying the password complexity
			List<String> passVal = new ArrayList<String>();
			Map<String, String> map = new HashMap<String, String>();
			map = br.com.hrstatus.security.PasswordPolicy.verifyPassComplexity(password);
			Object[] valueMap = map.keySet().toArray();
			for (int i = 0; i < valueMap.length; i++) {
				if (map.get(valueMap[i]).equals("false")) {
					passVal.add(map.get(valueMap[i + 1]));
				}
			}
			
			if (passVal.size() > 0) {
				String passvalidation = "---> ";
				for (int j = 0; j < passVal.size(); j++) {
					passvalidation += passVal.get(j);
				}
				return passvalidation;
			}
		}
		
		//email validation
		if (!m.find()) {
			return "O email " + mail + " não é válido.";
		}
		
		try {
					
			//setting user properties
			user.setFirstLogin(true);
			user.setNome(name);
			user.setUsername(username);
			user.setAuthority(role);
			user.setEnabled(enabled);
			user.setPassword(encode.encodePassUser(password));
			user.setMail(mail);
			
			this.userDAO.saveORupdateUser(user);
			
			log.info("[ " + userInfo.getLoggedUsername() + " ] The user " + user.getUsername() + " was succesfully created.");
			// Sending a e-mail to the user to notify about the user creation.
			MailSender sendMail = new MailSender();
			sendMail.sendCreatUserInfo(this.configurationDAO.getMailSender(),
							user.getMail(), this.configurationDAO.getJndiMail(),
							user.getNome(), user.getUsername(), user.getPassword());
			
			return "The user " + user.getUsername() + " was succesfully created."; 
		} catch (Exception e) {
			e.printStackTrace();
			return "Falha ao criar usuário: " + e;
		}
		
	}

}