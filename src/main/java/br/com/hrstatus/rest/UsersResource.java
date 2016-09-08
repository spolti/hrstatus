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

import br.com.hrstatus.model.User;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Path("user")
@Produces("application/json")
public interface UsersResource {

    /*
     * Register new user
     * Rest interface
     * \/rest/user/new/{username}/{password}/{role}/{name}/{notification}/{enabled}
     * Example: http://localhost:8080/hs/rest/user/new/username/auto|pasword/admin|user|rest/user%20full%20name/test@test.com/true|false
     */
    @Path("admin/new/{username}/{password}/{role}/{name}/{mail}/{enabled}")
    @GET
    @RolesAllowed({"ROLE_ADMIN"})
    String newUserRest(@PathParam("username") String username, @PathParam("password") String password, @PathParam("role") String role,
                       @PathParam("name") String name, @PathParam("mail") String mail, @PathParam("enabled") boolean enabled);

    /*
    * Register new user
    * Form request
    */
    @Path("admin/new")
    @POST
    @RolesAllowed({"ROLE_ADMIN"})
    void newUserForm(@FormParam("username") String username, @FormParam("password") String password,
                     @FormParam("verifyPassword") String verifyPassword, @FormParam("roles") String[] role,
                     @FormParam("nome") String name, @FormParam("email") String mail, @FormParam("enabled") boolean enabled,
                     @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception;

    /*
    * Update User
    * Form request
    */
    @Path("admin/update")
    @POST
    @RolesAllowed({"ROLE_ADMIN"})
    void update(@FormParam("username") String username, @FormParam("password") String password,
                    @FormParam("verifyPassword") String verifyPassword, @FormParam("roles") String[] roles,
                    @FormParam("nome") String nome, @FormParam("email") String mail, @FormParam("enabled") boolean enabled,
                    @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception;

    /*
    * Update User
    * Form request
    */
    @Path("update-nonadmin")
    @POST
    void updateNonadmin(@FormParam("username") String username, @FormParam("password") String password,
                @FormParam("verifyPassword") String verifyPassword, @FormParam("roles") String[] roles,
                @FormParam("nome") String nome, @FormParam("email") String mail, @FormParam("enabled") boolean enabled,
                @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception;

    /*
    * Update User - admin rights
    * Form request
    */
    @Path("admin/edit/{username}")
    @GET
    @RolesAllowed({"ROLE_ADMIN"})
    void edit(@PathParam("username") String username, @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception;

    /*
    * Update myself
    * Form request
    */
    @Path("/edit-nonadmin/{username}")
    @GET
    void editLimited(@PathParam("username") String username, @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception;

    /*
    * @return all users
    */
    @Path("admin/list{form : (/form)?}")
    @GET
    @RolesAllowed({"ROLE_ADMIN"})
    List<User> listUsers(@PathParam("form") String form, @QueryParam("status") String status, @QueryParam("userDeleted") String userDeleted,
                         @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception;

    /*
    * Delete the given user
    */
    @Path("admin/delete/{username}")
    @DELETE
    @RolesAllowed({"ROLE_ADMIN"})
    void deleteUser(@PathParam("username") String username, @Context HttpServletRequest request, @Context HttpServletResponse response)
            throws ServletException, IOException;
}