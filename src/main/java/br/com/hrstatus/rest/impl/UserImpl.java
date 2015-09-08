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

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.model.Users;
import br.com.hrstatus.rest.UserResource;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Component
public class UserImpl extends SpringBeanAutowiringSupport implements UserResource{
	
	Logger log = Logger.getLogger(UserImpl.class.getCanonicalName());
	
	@Autowired(required = true)
	private UsersInterface userDAO;
	private UserInfo userInfo = new UserInfo();
	
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

}
