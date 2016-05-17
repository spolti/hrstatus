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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */

@Path("user")
@Produces("application/json")
public interface UsersResource {

    /*
     * Rest interface
     * \/rest/user/new/{username}/{password}/{role}/{name}/{mail}/{enabled}
     * Example: http://localhost:8080/hs/rest/user/new/username/auto|pasword/admin|user|rest/user%20full%20name/test@test.com/true|false
     */
    @Path("new/{username}/{password}/{role}/{name}/{mail}/{enabled}")
    @GET
    String newUserRest(@PathParam("username") String username, @PathParam("password") String password, @PathParam("role") String role,
                       @PathParam("name") String name, @PathParam("mail") String mail, @PathParam("enabled") boolean enabled);


    /*
    * Form
    * \/rest/user/new/{username}/{password}/{role}/{name}/{mail}/{enabled}
    * Example: http://localhost:8080/hs/rest/user/new/username/auto|pasword/admin|user|rest/user%20full%20name/test@test.com/true|false
     */
    @Path("registerUser")
    @POST
    void newUserForm(@FormParam("username") String username, @FormParam("password") String password, @FormParam("verifyPassword") String verifyPassword, @FormParam("roles") String[] role,
                       @FormParam("nome") String name, @FormParam("mail") String mail, @FormParam("enabled") boolean enabled, @Context HttpServletRequest request, @Context HttpServletResponse response) throws ServletException, IOException;

}