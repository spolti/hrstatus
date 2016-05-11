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

import br.com.hrstatus.model.Users;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/*
 * @author spolti
 */

@Path("user")
@Produces("application/json; charset=utf8")
public interface UserResource {

    /*
     * \/rest/user/list
     * Example: http://localhost:8080/hs/rest/user/list
     */
    @Path("list")
    @GET
    List<Users> users();

    /*
     * \/rest/user/list/blockedusers
     * Example: http://localhost:8080/hs/rest/user/list/blockedusers
     */
    @Path("list/blockedusers")
    @GET
    List<Users> blockedUsers();

    /*
     * \/rest/user/remove/{username}
     * Example: http://localhost:8080/hs/rest/user/remove/username
     */
    @Path("remove/{username}")
    @GET
    String removeUser(@PathParam("username") String username);

    /*
     * \/rest/user/new/{username}/{password}/{role}/{name}/{mail}/{enabled}
     * Example: http://localhost:8080/hs/rest/user/new/username/auto|pasword/admin|user|rest/user%20full%20name/test@test.com/true|false
     */
    @Path("new/{username}/{password}/{role}/{name}/{mail}/{enabled}")
    @GET
    String newUser(@PathParam("username") String username, @PathParam("password") String password, @PathParam("role") String role,
                   @PathParam("name") String name, @PathParam("mail") String mail, @PathParam("enabled") boolean enabled);

}