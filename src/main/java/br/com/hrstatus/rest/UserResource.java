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
import br.com.hrstatus.model.support.response.RequestResponse;
import br.com.hrstatus.repository.Repository;
import br.com.hrstatus.security.PasswordUtils;
import br.com.hrstatus.utils.notification.Notification;
import br.com.hrstatus.utils.notification.channel.Email;
import br.com.hrstatus.utils.notification.template.NewUserMessageTemplate;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * For further information about response codes
 * http://docs.oracle.com/javaee/7/api/javax/ws/rs/core/Response.Status.html
 * https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.5
 *
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Path("user")
@Transactional
public class UserResource {

    private Logger log = Logger.getLogger(UserResource.class.getName());

    @Inject
    private User user;
    @Inject
    private Repository repository;
    @Inject
    private PasswordUtils passwordUtils;
    @Inject
    private Email emailChannel;
    @Inject
    private RequestResponse reqResponse;

    /*
    * Register new user
    * Receives a JSon Object
    */
    @Path("admin/new")
    @POST
    @RolesAllowed({"ROLE_ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newUser(User newUser) {

        String password = newUser.getPassword();
        // ok, now encrypt it and persist the user
        newUser.setPassword(PasswordUtils.encryptUserPassword(password));
        Object result = repository.register(newUser);

        if ("success".equals(result)) {
            // Send the notification email in a different thread
            CompletableFuture.runAsync(() -> {
                log.info(new Notification()
                        .send(NewUserMessageTemplate.get(newUser.getUsername(), password))
                        .subject(NewUserMessageTemplate.SUBJECT)
                        .to(newUser.getMail())
                        .by(emailChannel));
            }).toCompletableFuture();
            reqResponse.setCreatedUser(newUser.getNome());
            reqResponse.setResponseMessage("Usuário " + newUser.getNome() + " foi criado com sucesso.");
            return Response.status(Response.Status.CREATED).entity(reqResponse).build();
        } else if (result.toString().contains("ConstraintViolation")) {
            reqResponse.setFailedUser(newUser.getNome());
            reqResponse.setResponseErrorMessage(String.valueOf(result));
            return Response.status(Response.Status.CONFLICT).entity(reqResponse).build();
        } else {
            reqResponse.setFailedUser(newUser.getNome());
            reqResponse.setResponseErrorMessage(String.valueOf(result));
            return Response.status(Response.Status.BAD_REQUEST).entity(reqResponse).build();
        }
    }

    /*
    * Update User
    * Form request
    */
    @Path("admin/update")
    @POST
    @RolesAllowed({"ROLE_ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(User updatedUser) {
        log.fine("User received to update: " + updatedUser.toString());
        updatedUser.setPassword(updatedUser.getPassword().length() == 44 && updatedUser.getPassword().endsWith("=") ? updatedUser.getPassword() : passwordUtils.encryptUserPassword(updatedUser.getPassword()));
        String result = repository.update(updatedUser);
        if ("success".equals(result)) {
            reqResponse.setResponseMessage("Usuário " + updatedUser.getNome() + " foi atualizado com sucesso.");
            return Response.ok(reqResponse).build();
        } else {
            reqResponse.setFailedUser(updatedUser.getNome());
            reqResponse.setResponseErrorMessage(result);
            return Response.status(Response.Status.BAD_REQUEST).entity(reqResponse).build();
        }
    }

    /*
    * Update User
    * Form request
    */
    @Path("update-nonadmin")
    @POST
    public void updateNonadmin(@FormParam("username") String username, @FormParam("password") String password,
                               @FormParam("verifyPassword") String verifyPassword, @FormParam("roles") String[] roles,
                               @FormParam("nome") String nome, @FormParam("email") String mail, @FormParam("enabled") boolean enabled,
                               @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
        //load users information from database before update
        user = repository.searchUser(username);
        user.setPassword(password.length() == 44 && password.endsWith("=") ? password : passwordUtils.encryptUserPassword(password));
        user.setMail(mail);
        user.addRoles(roles);
        user.toString();

        String result = repository.update(user);

        if ("success".equals(result)) {
            request.setAttribute("update", "success");
            request.setAttribute("user", nome);
        } else {
            request.setAttribute("error", true);
            request.setAttribute("message", result);
            request.getRequestDispatcher("/home/home.jsp").forward(request, response);
        }
        request.getRequestDispatcher("/home/home.jsp").forward(request, response);
    }

    /*
    * Update User - admin rights
    * Form request
    */
    @Path("admin/edit/{username}")
    @GET
    @RolesAllowed({"ROLE_ADMIN"})
    public void edit(@PathParam("username") String username, @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {

        //preload the user attributes before send it to edition
        br.com.hrstatus.model.User loggedUser = repository.searchUser(request.getUserPrincipal().getName());
        loggedUser.addRoles(loggedUser.getRoles().stream().toArray(String[]::new));

        //double check to make sure user is admin
        if (loggedUser.isAdmin() && !"root".equals(username)) {
            user = repository.searchUser(username);
            log.info("Usuário recebido para edição: " + username);
            user.addRoles(user.getRoles().stream().toArray(String[]::new));
            request.setAttribute("user", user);
            request.getRequestDispatcher("/admin/user/edit_user.jsp").forward(request, response);
        } else {
            // Non admin users can't edit other users
            log.fine("Tentativa de alteração de usuário inválida");
            response.sendError(403);
        }
    }

    /*
    * Update myself
    * Form request
    */
    @Path("/edit-nonadmin/{username}")
    @GET
    public void editLimited(@PathParam("username") String username, @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {

        //preload the user attributes before send it to edition
        br.com.hrstatus.model.User loggedUser = repository.searchUser(request.getUserPrincipal().getName());
        loggedUser.addRoles(loggedUser.getRoles().stream().toArray(String[]::new));

        if (loggedUser.getUsername().equals(username)) {
            log.info("Usuário recebido para edição: " + user.getUsername());
            user = repository.searchUser(username);
            user.addRoles(user.getRoles().stream().toArray(String[]::new));
            request.setAttribute("user", user);
            request.getRequestDispatcher("/user/edit.jsp").forward(request, response);

        } else {
            // Non admin users can't edit other users
            log.fine("Tentativa de alteração de usuário inválida");
            response.sendError(403);
        }
    }

    /*
    * @return all users
    */
    @Path("admin/list{form : (/form)?}")
    @GET
    @RolesAllowed({"ROLE_ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listUsers(@PathParam("form") String form, @QueryParam("status") String status, @QueryParam("userDeleted") String userDeleted,
                                @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {

        if ("".equals(form)) {
            return repository.getUsers();
        } else {
            if (!"".equals(status)) {
                request.setAttribute("info", status);
                request.setAttribute("userDeleted", userDeleted);

            }
            request.setAttribute("userList", repository.getUsers());
            request.getRequestDispatcher("/admin/user/users.jsp").forward(request, response);
        }
        //if the request is not coming from the rest api or the form, ignore it
        return null;
    }

    /*
    * Delete the given user
    */
    @Path("admin/delete/{username}")
    @DELETE
    @RolesAllowed({"ROLE_ADMIN"})
    public Response deleteUser(@PathParam("username") String username) throws IOException {

        if ("root".equals(username)) {
            log.fine("Usuário root não pode ser removido do sistema");
            reqResponse.setResponseErrorMessage("Usuário root não pode ser removido do sistema\"");
            return Response.status(Response.Status.FORBIDDEN).entity(reqResponse).build();
        } else {
            log.fine("Usuário recebido para remoção [" + username + "]");
            repository.delete(repository.searchUser(username));
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

}