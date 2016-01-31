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

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import br.com.hrstatus.model.BancoDados;

/*
 * @author spolti
 */

@Path("database")
@Produces("application/json; charset=utf8")
public interface DataBaseResource {

	/*
	 * \/rest/database/list
	 * Example: http://localhost:8080/hs/rest/database/list
	 */
	@Path("list")
	@GET
	public List<BancoDados> bancodados();
	
	/*
	 * \/rest/database/list/ok
	 * Example: http://localhost:8080/hs/rest/database/list/ok
	 */
	@Path("list/ok")
	@GET
	public List<BancoDados> bancodadosOK();
	
	/*	 
	 * * \/rest/database/list/nok
	 * Example: http://localhost:8080/hs/rest/database/list/nok
	 */
	@Path("list/nok")
	@GET
	public List<BancoDados> bancodadosNOK();
	
	/*
	 * \/rest/database/remove/{id}
	 * Example: http://localhost:8080/hs/rest/database/remove/1
	 */
	@Path("remove/{id}")
	@GET
	public String removeDatabase(@PathParam("id")  int id);
	
	/*
	 * \/rest/database/new/{ip}/{hostname}/{dbInstance}/{username}/{password}/{dbVendor}
	 * Example: http://localhost:8080/hs/rest/database/new/127.0.0.1/localhost/hrstatus/hrstatus/P@ssw0rd/mysql
	 */
	@Path("/new/{ip}/{hostname}/{dbInstance}/{username}/{password}/{dbVendor}")
	@GET
	public String addDatabase(@PathParam(value = "ip") String ip, @PathParam("hostname")  String hostname, 
			@PathParam(value = "dbInstance") String dbInstance, @PathParam(value = "username") String username,
			@PathParam(value = "password") String password, @PathParam(value = "dbVendor") String dbVendor);
	
	/*
	 * \/rest/database/new/{ip}/{hostname}/{dbInstance}/{username}/{password}/{dbVendor}
	 * Example: http://localhost:8080/hs/rest/database/new/127.0.0.1/localhost/hrstatus/hrstatus/P@ssw0rd/sqlserver/instanceName
	 */
	@Path("/new/{ip}/{hostname}/{dbInstance}/{username}/{password}/{dbVendor}/{dbName}")
	@GET
	public String addDatabaseSqlServer(@PathParam(value = "ip") String ip, @PathParam("hostname")  String hostname, 
			@PathParam(value = "dbInstance") String dbInstance, @PathParam(value = "username") String username,
			@PathParam(value = "password") String password, @PathParam(value = "dbVendor") String dbVendor,
			@PathParam(value = "dbName") String dbName);
	
	/*
	 * \/rest/database/verification/full
	 * Example: http://localhost:8080/hs/rest/database/verification/full
	 */
	@Path("verification/full")
	@GET
	public List<BancoDados> fullDbVerification(@Context HttpServletResponse response);
	
	/*
	 * \/rest/database/verification/notFull
	 * Example: http://localhost:8080/hs/rest/database/verification/notFull
	 */
	@Path("verification/notFull")
	@GET
	public List<BancoDados> notFullDbVerification(@Context HttpServletResponse response);
}