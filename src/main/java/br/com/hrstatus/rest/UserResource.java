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
import br.com.hrstatus.repository.impl.DataBaseRepository;
import br.com.hrstatus.security.PasswordUtils;
import br.com.hrstatus.utils.notification.Notification;
import br.com.hrstatus.utils.notification.channel.Email;
import br.com.hrstatus.utils.notification.template.NewUserMessageTemplate;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
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
    private DataBaseRepository repository;
    @Inject
    private Email emailChannel;
    @Inject
    private RequestResponse reqResponse;

    /*
    * Register new user
    * Receives a JSon Object
    */
    @POST
    @Path("admin/new")
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
            reqResponse.setFailedSubject(newUser.getNome());
            reqResponse.setResponseErrorMessage(String.valueOf(result));
            return Response.status(Response.Status.CONFLICT).entity(reqResponse).build();
        } else {
            reqResponse.setFailedSubject(newUser.getNome());
            reqResponse.setResponseErrorMessage(String.valueOf(result));
            return Response.status(Response.Status.BAD_REQUEST).entity(reqResponse).build();
        }
    }

    /*
    * Update User
    */
    @POST
    @Path("admin/update")
    @RolesAllowed({"ROLE_ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(User updatedUser) {
        log.fine("User received to update: " + updatedUser.toString());
        updatedUser.setPassword(updatedUser.getPassword().length() == 44 && updatedUser.getPassword().endsWith("=") ? updatedUser.getPassword() : PasswordUtils.encryptUserPassword(updatedUser.getPassword()));
        return response(String.valueOf(repository.update(updatedUser)), updatedUser);
    }

    /*
    * Update User
    * Form request
    */
    @POST
    @Path("update-nonadmin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateNonAdmin(User updatedUser) {
        log.fine("User received to update: " + updatedUser.toString());
        //make sure someone does not changed the user roles
        //this method should not allow update the roles
        User tempUser = repository.search(User.class, "username", updatedUser.getUsername());
        updatedUser.addRoles(tempUser.getRoles().stream().toArray(String[]::new));
        updatedUser.setPassword(updatedUser.getPassword().length() == 44 && updatedUser.getPassword().endsWith("=") ? updatedUser.getPassword() : PasswordUtils.encryptUserPassword(updatedUser.getPassword()));
        return response(String.valueOf(repository.update(updatedUser)), updatedUser);
    }

    /*
    * Update User - admin rights
    * Form request
    */
    @GET
    @Path("admin/edit/{username}")
    @RolesAllowed({"ROLE_ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response edit(@PathParam("username") String username, @Context HttpServletRequest request) {

        //preload the user attributes before send it to edition
        br.com.hrstatus.model.User loggedUser = repository.search(User.class, "username", request.getUserPrincipal().getName());
        loggedUser.addRoles(loggedUser.getRoles().stream().toArray(String[]::new));

        //double check to make sure user is admin
        if (loggedUser.isAdmin() && !"root".equals(username)) {
            user = repository.search(User.class, "username", username);
            log.info("Usuário recebido para edição: " + username);
            user.addRoles(user.getRoles().stream().toArray(String[]::new));
            return Response.ok(user).build();
        } else {
            // Non admin users can't edit other users
            // fazer plugin para envio de email contendo detalhes da tentativa inválida de alteração
            log.fine("Tentativa de alteração de usuário inválida");
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    /*
    * Update myself, its not allowed user A modify user B, A can only modify itself
    */
    @GET
    @Path("/edit-nonadmin/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editLimited(@PathParam("username") String username, @Context HttpServletRequest request) throws Exception {
        if (request.getUserPrincipal().getName().equals(username) && !request.getUserPrincipal().getName().equals("root")) {
            log.info("Usuário recebido para edição: " + user.getUsername());
            user = repository.search(User.class, "username", username);
            user.addRoles(user.getRoles().stream().toArray(String[]::new));
            return Response.ok(user).build();
        } else {
            // Non admin users can't edit other users
            // fazer plugin para envio de email contendo detalhes da tentativa inválida de alteração
            log.fine("Tentativa de alteração de usuário inválida");
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    /*
    * @return all users
    */
    @GET
    @Path("list")
    @RolesAllowed({"ROLE_ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response listUsers(){
        return Response.ok(repository.list(User.class)).build();
    }

    /*
    * Delete the given user
    */
    @DELETE
    @Path("admin/delete/{username}")
    @RolesAllowed({"ROLE_ADMIN"})
    public Response deleteUser(@PathParam("username") String username) {
        if ("root".equals(username)) {
            log.fine("Usuário root não pode ser removido do sistema");
            reqResponse.setResponseErrorMessage("Usuário root não pode ser removido do sistema\"");
            return Response.status(Response.Status.FORBIDDEN).entity(reqResponse).build();
        } else {
            log.fine("Usuário recebido para remoção [" + username + "]");
            repository.delete(repository.search(User.class, "username", username));
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }


    private Response response (String result, User updatedUser) {
        if ("success".equals(result)) {
            reqResponse.setResponseMessage("Usuário " + updatedUser.getNome() + " foi atualizado com sucesso.");
            return Response.ok(reqResponse).build();
        } else {
            reqResponse.setFailedSubject(updatedUser.getNome());
            reqResponse.setResponseErrorMessage(result);
            return Response.status(Response.Status.BAD_REQUEST).entity(reqResponse).build();
        }
    }
}