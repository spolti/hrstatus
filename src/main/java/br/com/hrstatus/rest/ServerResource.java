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

	/*
	 * \/rest/server/list
	 * Example: http://localhost:8080/hs/rest/server/list
	 */
	@Path("list")
	@GET
	public List<Servidores> servidores();
	
	
	/*
	 * \/rest/server/list/withLogDir
	 * Example: http://localhost:8080/hs/rest/server/list/withLogDir
	 */
	@Path("list/withLogDir")
	@GET
	public List<Servidores> withLogDir();
	
	
	/*
	 * \/rest/server/list/ok
	 * Example: http://localhost:8080/hs/rest/server/list/ok
	 */
	@Path("list/ok")
	@GET
	public List<Servidores> serverOk();
	
	
	/*
	 * \/rest/server/list/nok
	 * Example: http://localhost:8080/hs/rest/server/list/nok
	 */
	@Path("list/nok")
	@GET
	public List<Servidores> serverNok();
	
	
	/*
	 * \/rest/server/remove/{id}
	 * Example: http://localhost:8080/hs/rest/server/remove/1
	 */
	@Path("remove/{id}")
	@GET
	public String removeServer(@PathParam("id")  int id);
	
	
	/*
	 * \/rest/server/list
	 * Example: http://localhost:8080/hs/rest/server/new/127.0.0.1/localhost/fspolti/lipe33spol90/%2Fvar%2Flog/ntpdate -u/22/SIM/linux
	 * where %2F is /, example /var/log will be %2Fvar%2Flog
	 */
	@Path("new/{ip}/{hostname}/{user}/{passwd}/{logDir}/{ntpCommand}/{port}/{active}/{so}")
	@GET
	public String newServer(@PathParam("ip")  String ip, @PathParam("hostname")  String hostname,
			@PathParam("user")  String user, @PathParam("passwd")  String passwd, @PathParam("logDir")  String logDir,
			@PathParam("ntpCommand")  String ntpCommand, @PathParam("port")  int port, @PathParam("active")  String active,
			@PathParam("so")  String so);
}