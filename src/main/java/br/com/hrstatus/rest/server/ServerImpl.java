package br.com.hrstatus.rest.server;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.rest.impl.server.ServerResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Component
public class ServerImpl extends SpringBeanAutowiringSupport implements ServerResource {

	Logger log = Logger.getLogger(ServerImpl.class.getCanonicalName());

	@Autowired(required = true)
	private ServersInterface iteracoesDAO;

	@PostConstruct
	public void init() {
		log.info("initializinf Autowired Service.");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public List<Servidores> servidores() {
		try {
			log.info("Returning the server list.");
			return this.iteracoesDAO.listServers();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}