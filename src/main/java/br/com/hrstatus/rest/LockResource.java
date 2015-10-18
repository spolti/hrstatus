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

import br.com.hrstatus.model.Lock;

/*
 * @author spolti
 */

@Path("locks")
@Produces("application/json; charset=utf8")
public interface LockResource {

	
	/*
	 * \/rest/locks/list
	 * Example: http://localhost:8080/hs/rest/locks/list
	 */
	@Path("list")
	@GET
	public List<Lock> listLocks();
	
	/*
	 * \/rest/locks/delete/{id}
	 * Example: http://localhost:8080/hs/rest/locks/delete/1
	 */
	@Path("remove/{id}")
	@GET
	public String deleteLockByID(@PathParam(value = "id") int id);
}