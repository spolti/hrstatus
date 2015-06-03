package br.com.hrstatus.rest.impl.server;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import br.com.hrstatus.model.Servidores;

@Path("server")
@Produces("application/json; charset=utf8")
public interface ServerResource {

	@Path("list")
	@GET
	public List<Servidores> servidores();
}