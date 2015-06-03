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

import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.rest.DataBaseResource;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Component
public class DataBaseImpl extends SpringBeanAutowiringSupport implements DataBaseResource {

	Logger log = Logger.getLogger(DataBaseImpl.class.getCanonicalName());

	@Autowired(required = true)
	private BancoDadosInterface databaseDAO;
	private UserInfo userInfo = new UserInfo();

	@PostConstruct
	public void init() {
		log.info("initializing Autowired Service.");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public List<BancoDados> bancodados() {
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ] Returning the database list.");
			return this.databaseDAO.listDataBases();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}


}