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

import br.com.hrstatus.dao.LockIntrface;
import br.com.hrstatus.model.Lock;
import br.com.hrstatus.rest.LockResource;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Component
public class LockImpl extends SpringBeanAutowiringSupport implements LockResource {

	Logger log = Logger.getLogger(LockImpl.class.getCanonicalName());

	@Autowired(required = true)
	private LockIntrface lockDAO;
	private UserInfo userInfo = new UserInfo();

	@PostConstruct
	public void init() {
		log.info("initializing Autowired Service.");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public List<Lock> listLocks() {
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Returning the database list.");
			return this.lockDAO.ListAllLocks();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String deleteLockByID(int id){
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Removing lock id: " + id);
			lockDAO.removeLock(lockDAO.getLockByID(id));
			return "SUCCESS";
		} catch (Exception e) {
			log.severe("Could not delete lock." + e);
			return "FAIL";
		}
	}
}