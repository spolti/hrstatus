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

import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.model.Configurations;
import br.com.hrstatus.rest.SetupResource;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Component
public class SetupImpl extends SpringBeanAutowiringSupport implements SetupResource {

	Logger log = Logger.getLogger(SetupImpl.class.getCanonicalName());

	@Autowired(required = true)
	private Configuration configDAO;
	private UserInfo userInfo = new UserInfo();

	@PostConstruct
	public void init() {
		log.info("initializing Autowired Service.");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public Configurations configurations() {
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Returning the database list.");
			return this.configDAO.getConfigs();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}