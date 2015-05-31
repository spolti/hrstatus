package br.com.hrstatus.rest.impl.database;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import br.com.hrstatus.model.BancoDados;


@Path("database")
@Produces("application/json; charset=utf8")
public interface DataBaseResource {

	@Path("list")
	@GET
	public List<BancoDados> bancodados();
}