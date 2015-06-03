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

package br.com.hrstatus.rest;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.rest.impl.ServerResource;
import br.com.hrstatus.utils.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/*
 * @author spolti
 */

@Component
public class ServerImpl extends SpringBeanAutowiringSupport implements ServerResource {

	Logger log = Logger.getLogger(ServerImpl.class.getCanonicalName());

	@Autowired(required = true)
	private ServersInterface iteracoesDAO;
	private UserInfo userInfo = new UserInfo();

	@PostConstruct
	public void init() {
		log.info("initializing Autowired Service.");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public List<Servidores> servidores() {
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ] Returning the server list.");
			return this.iteracoesDAO.listServers();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}