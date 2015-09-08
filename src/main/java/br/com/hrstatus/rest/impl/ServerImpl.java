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

import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.rest.ServerResource;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Component
public class ServerImpl extends SpringBeanAutowiringSupport implements ServerResource {

	Logger log = Logger.getLogger(ServerImpl.class.getCanonicalName());

	@Autowired(required = true)
	private ServersInterface iteracoesDAO;
	private UserInfo userInfo = new UserInfo();

	private Servidores server = new Servidores();
	private Crypto encodePass = new Crypto();

	@PostConstruct
	public void init() {
		log.info("initializing Autowired Service.");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public List<Servidores> servidores() {
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Returning the server list.");
			return this.iteracoesDAO.listServers();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Servidores> withLogDir() {
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Returning the server list with log dir.");
			return this.iteracoesDAO.getHostnamesWithLogDir();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Servidores> serverOk() {
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Returning the servers updated.");
			return this.iteracoesDAO.getServersOK();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Servidores> serverNok() {
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Returning the servers out of date.");
			return this.iteracoesDAO.getServersNOK();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String removeServer(int id) {
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Deleting server id: " + id);
			
			this.iteracoesDAO.deleteServerByID(this.iteracoesDAO.getServerByID(id));
			return "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL, check logs for details";
		}
	}

	@SuppressWarnings("static-access")
	public String newServer(String ip, String hostname, String user,
			String passwd, String logDir, String ntpCommand, int port,
			String active, String so) {
		
		log.fine("Server parameters received: +++++++++++++++++++++");
		log.info("ip "+ ip);
		log.info("hostname "+ hostname);
		log.info("user "+ user);
		log.info("passwd "+ passwd);
		log.info("logDir "+ logDir);
		log.info("ntpCommand "+ ntpCommand);
		log.info("port "+ port);
		log.info("active "+ active);
		log.info("so "+ so);
		log.fine("+++++++++++++++++++++++++++++++++++++++++++++++++");
		
		server.setIp(ip);
		server.setHostname(hostname);
		server.setUser(user);
		try {
			// Encrypting the password
			server.setPass(encodePass.encode(passwd));

		} catch (Exception e) {
			log.severe("Error: " + e);	
		}
		server.setLogDir(logDir);
		server.setSuCommand(ntpCommand);
		server.setPort(port);
		server.setVerify(active);
		server.setSO(so.toUpperCase());
		server.setStatus("Servidor ainda não foi verificado.");
		server.setTrClass("error");
		
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Adding server: " + hostname);
			this.iteracoesDAO.insert_server(server);
			return "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL, check logs for details";
		}
	}
	
}