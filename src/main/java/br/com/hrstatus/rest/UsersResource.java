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

import br.com.hrstatus.model.Role;
import br.com.hrstatus.model.User;
import br.com.hrstatus.repository.Repository;
import br.com.hrstatus.security.PasswordUtils;
import br.com.hrstatus.utils.notification.Notification;
import br.com.hrstatus.utils.notification.channel.Email;
import br.com.hrstatus.utils.notification.template.NewUserMessageTemplate;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Path("user")
@Produces("application/json")
@Transactional
public class UsersResource {

    private Logger log = Logger.getLogger(UsersResource.class.getName());

    @Inject
    private User user;
    @Inject
    private Role role;
    @Inject
    private Repository repository;
    @Inject
    private PasswordUtils passwordUtils;
    @Inject
    private Email emailChannel;

    /*
    * Register new user
    * Rest interface
    * \/rest/user/new/{username}/{password}/{role}/{name}/{notification}/{enabled}
    * Example: http://localhost:8080/hs/rest/user/new/username/auto|pasword/admin|user|rest/user%20full%20name/test@test.com/true|false
    */
    @Path("admin/new/{username}/{password}/{role}/{name}/{mail}/{enabled}")
    @GET
    @RolesAllowed({"ROLE_ADMIN"})
    public String newUserRest(String username, String password, String role, String name, String mail, boolean enabled) {
        //TODO
        return null;
    }

    /*
    * Register new user
    * Form request
    */
    @Path("admin/new")
    @POST
    @RolesAllowed({"ROLE_ADMIN"})
    public void newUserForm(@FormParam("username") String username, @FormParam("password") String password,
                            @FormParam("verifyPassword") String verifyPassword, @FormParam("roles") String[] roles,
                            @FormParam("nome") String nome, @FormParam("email") String mail,
                            @FormParam("enabled") boolean enabled, @Context HttpServletRequest request,
                            @Context HttpServletResponse response) throws Exception {

        try {
            user.setNome(nome);
            user.setUsername(username);
            user.setPassword(passwordUtils.encryptUserPassword(password));
            user.setMail(mail);
            user.setEnabled(enabled);
            registerOrUpdate(roles, "new", true);
            user.addRoles(repository.getRoles(user.getUsername()));
            user.dumpUserInformation();

            // Send the notification email in a different thread
            CompletableFuture.runAsync(() -> {
                log.info(new Notification()
                        .send(NewUserMessageTemplate.get(username, password))
                        .subject(NewUserMessageTemplate.SUBJECT)
                        .to(user.getMail())
                        .by(emailChannel));
            }).toCompletableFuture();

            request.setAttribute("info", "success");
            request.setAttribute("user", nome);
            request.setAttribute("userList", getUserAndRoles());
            request.getRequestDispatcher("/admin/user/users.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", true);
            request.setAttribute("message", e.getCause().getMessage());
            request.getRequestDispatcher("/admin/user/user_form.jsp").forward(request, response);
        }
    }

    /*
    * Update User
    * Form request
    */
    @Path("admin/update")
    @POST
    @RolesAllowed({"ROLE_ADMIN"})
    public void update(@FormParam("username") String username, @FormParam("password") String password,
                       @FormParam("verifyPassword") String verifyPassword, @FormParam("roles") String[] roles,
                       @FormParam("nome") String nome, @FormParam("email") String mail, @FormParam("enabled") boolean enabled,
                       @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {

        //load users information from database before update
        user = repository.searchUser(username);
        user.setNome(nome);
        user.setUsername(username);
        user.setPassword(password.length() == 44 && password.endsWith("=") ? password : passwordUtils.encryptUserPassword(password));
        user.setMail(mail);
        user.setEnabled(enabled);
        String result = registerOrUpdate(roles, "update", true);
        user.addRoles(repository.getRoles(user.getUsername()));
        user.dumpUserInformation();

        if ("success".equals(result)) {
            request.setAttribute("update", "success");
            request.setAttribute("user", nome);
        } else {
            request.setAttribute("error", true);
            request.setAttribute("message", result);
            request.setAttribute("userList", getUserAndRoles());
            request.getRequestDispatcher("/admin/user/users.jsp").forward(request, response);
        }
        request.setAttribute("userList", getUserAndRoles());
        request.getRequestDispatcher("/admin/user/users.jsp").forward(request, response);
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
        String result = registerOrUpdate(roles, "update", false);
        user.addRoles(repository.getRoles(user.getUsername()));
        user.dumpUserInformation();

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
        User loggedUser = repository.searchUser(request.getUserPrincipal().getName());
        loggedUser.addRoles(repository.getRoles(loggedUser.getUsername()));

        //double check to make sure user is admin
        if (loggedUser.isAdmin() && !"root".equals(username)) {
            user = repository.searchUser(username);
            log.info("Usuário recebido para edição: " + username);
            user.addRoles(repository.getRoles(user.getUsername()));
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
        User loggedUser = repository.searchUser(request.getUserPrincipal().getName());
        loggedUser.addRoles(repository.getRoles(loggedUser.getUsername()));

        if (loggedUser.getUsername().equals(username)) {
            log.info("Usuário recebido para edição: " + user.getUsername());
            user = repository.searchUser(username);
            user.addRoles(repository.getRoles(user.getUsername()));
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
    public List<User> listUsers(@PathParam("form") String form, @QueryParam("status") String status, @QueryParam("userDeleted") String userDeleted,
                                @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {

        if ("".equals(form)) {
            return getUserAndRoles();
        } else {
            if (!"".equals(status)) {
                request.setAttribute("info", status);
                request.setAttribute("userDeleted", userDeleted);
            }
            request.setAttribute("userList", getUserAndRoles());
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
    public void deleteUser(@PathParam("username") String username, @Context HttpServletRequest request, @Context HttpServletResponse response)
            throws ServletException, IOException {

        if ("root".equals(username)) {
            log.fine("Usuário root não pode ser removido do sistema");
        } else {
            log.fine("Usuário recebido para remoção [" + username + "]");
            repository.delete(repository.searchUser(username));
            repository.delete(username);
        }
    }

    /*
    * Register or update the given user
    * @param String[] roles
    * @param String operation - new/update
    * @returns success or the error message if it fail.
    */
    private String registerOrUpdate(String[] roles, String operation, boolean updateRoles) {
        try {
            if ("new".equals(operation)) {
                repository.registerUser(user);
            } else if ("update".equals(operation)) {
                repository.update(user);
            }
            if (updateRoles) {
                repository.delete(user.getUsername());
                for (String role : roles) {
                    this.role.addRole(role);
                    this.role.setUsername(user.getUsername());
                    log.fine("Mapping the role [" + role + "] to user [" + user.getUsername() + "]");
                    repository.save(this.role);
                    this.role = new Role();
                }
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    * Get the roles for the given user
    * @return the user roles
    */
    private List<User> getUserAndRoles() throws Exception {
        ArrayList<User> userList = new ArrayList<>();
        final List<User> userListfromDb = repository.getUsers();
        for (User user : userListfromDb) {
            user.addRoles(repository.getRoles(user.getUsername()));
            userList.add(user);
        }
        return userList;
    }
}