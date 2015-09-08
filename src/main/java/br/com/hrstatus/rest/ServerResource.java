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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.hrstatus.model.Servidores;

/*
 * @author spolti
 */

@Path("server")
@Produces("application/json; charset=utf8")
public interface ServerResource {

	@Path("listAll")
	@GET
	public List<Servidores> servidores();
	
	@Path("withLogDir")
	@GET
	public List<Servidores> withLogDir();
	
	@Path("ok")
	@GET
	public List<Servidores> serverOk();
	
	@Path("nok")
	@GET
	public List<Servidores> serverNok();
	
	@Path("remove/{id}")
	@GET
	public String removeServer(@PathParam("id")  int id);
	
	@Path("new/{ip}/{hostname}/{user}/{passwd}/{logDir}/{ntpCommand}/{port}/{active}/{so}")
	@GET
	public String newServer(@PathParam("ip")  String ip, @PathParam("hostname")  String hostname,
			@PathParam("user")  String user, @PathParam("passwd")  String passwd, @PathParam("logDir")  String logDir,
			@PathParam("ntpCommand")  String ntpCommand, @PathParam("port")  int port, @PathParam("active")  String active,
			@PathParam("so")  String so);
}