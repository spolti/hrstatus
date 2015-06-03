package br.com.hrstatus.rest.database;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.rest.impl.database.DataBaseResource;

@Component
public class DataBaseImpl extends SpringBeanAutowiringSupport implements DataBaseResource {

	Logger log = Logger.getLogger(DataBaseImpl.class.getCanonicalName());

	@Autowired(required = true)
	private BancoDadosInterface databaseDAO;

	@PostConstruct
	public void init() {
		log.info("initializinf Autowired Service.");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public List<BancoDados> bancodados() {
		try {
			log.info("Returning the database list.");
			return this.databaseDAO.listDataBases();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}


}