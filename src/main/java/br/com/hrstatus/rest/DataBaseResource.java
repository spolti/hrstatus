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

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.hrstatus.model.BancoDados;

/*
 * @author spolti
 */

@Path("database")
@Produces("application/json; charset=utf8")
public interface DataBaseResource {

	@Path("list")
	@GET
	public List<BancoDados> bancodados();
	
	@Path("list/ok")
	@GET
	public List<BancoDados> bancodadosOK();
	
	@Path("list/nok")
	@GET
	public List<BancoDados> bancodadosNOK();
	
	
	@Path("/add/{ip}/")
	@GET
	@RolesAllowed("ADMIN")
	public String addDatabase(@PathParam(value = "ip") String ip);
	
	
}