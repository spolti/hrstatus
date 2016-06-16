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

package br.com.hrstatus.rest.impl;

import br.com.hrstatus.dao.RolesInterface;
import br.com.hrstatus.dao.UserInterface;
import br.com.hrstatus.model.Role;
import br.com.hrstatus.model.User;
import br.com.hrstatus.rest.UsersResource;
import br.com.hrstatus.security.PasswordUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */

@Transactional
public class UsersResourceImpl implements UsersResource {

    private Logger log = Logger.getLogger(UsersResource.class.getName());

    @Inject
    private User user;
    @Inject
    private Role role;
    @Inject
    private UserInterface userDao;
    @Inject
    private RolesInterface roleDao;
    @Inject
    private PasswordUtils passwordUtils;

    public String newUserRest(String username, String password, String role, String name, String mail, boolean enabled) {
        return null;
    }

    /*
    * Register the user from a form
    * @params String username, String password, String verifyPassword, String[] roles, String nome, String mail, boolean enabled
    */
    public void newUserForm(@FormParam("username") String username, @FormParam("password") String password,
                            @FormParam("verifyPassword") String verifyPassword, @FormParam("roles") String[] roles,
                            @FormParam("nome") String nome, @FormParam("mail") String mail,
                            @FormParam("enabled") boolean enabled, @Context HttpServletRequest request,
                            @Context HttpServletResponse response) throws ServletException, IOException {

        log.fine("Parâmetro recebidos para cadastro de usuário: [Nome: " + nome + "]");
        log.fine("Parâmetro recebidos para cadastro de usuário: [Username: " + username + "]");
        log.fine("Parâmetro recebidos para cadastro de usuário: [Password: gotcha!]");
        log.fine("Parâmetro recebidos para cadastro de usuário: [Email: " + mail + "]");
        log.fine("Parâmetro recebidos para cadastro de usuário: [Enabled: " + enabled + "]");

        for (String temp : roles) {
            log.fine("Parâmetro recebidos para cadastro de usuário: [Role: " + temp + "]");
        }

        user.setNome(nome);
        user.setUsername(username);
        user.setPassword(passwordUtils.encryptUserPassword(password));
        user.setMail(mail);
        user.setEnabled(enabled);

        String result = register(roles);

        if ("success".equals(result)) {
            request.setAttribute("info", "success");
            request.setAttribute("user", nome);
        }else{
            request.setAttribute("error", true);
            request.setAttribute("message", result);
        }
        request.getRequestDispatcher("/admin/user/user_form.jsp").forward(request, response);
    }

    /*
    * @returns all users
    */
    public List<User> listUsers(@PathParam("form") String form, @QueryParam("status") String status, @QueryParam("userDeleted") String userDeleted,
                                @Context HttpServletRequest request, @Context HttpServletResponse response) throws ServletException, IOException {

        if ("".equals(form)) {
            return getUserAndRoles();
        } else {
            if (!"".equals(status)) {
                request.setAttribute("info", status);
                request.setAttribute("userDeleted", userDeleted);
            }
            request.setAttribute("userList", getUserAndRoles());
            request.getRequestDispatcher("/admin/user/edit_user.jsp").forward(request, response);
        }
        //if the request is not coming from the rest api or the form, ignore it
        return null;
    }

    /*
    * Delete the given user
    */
    public void deleteUser(@PathParam("username") String username, @Context HttpServletRequest request, @Context HttpServletResponse response)
            throws ServletException, IOException {

        log.fine("Usuário recebido para remoção [" + username + "]");
        userDao.delete(userDao.searchUser(username));
        roleDao.delete(username);
    }

    /*
    * Register the user
    * @returns success or the error message if it fail.
    */
    private String register(String[] roles) {
        try {
            userDao.registerUser(user);
            for (String role : roles) {
                this.role.setRole(role);
                this.role.setUsername(user.getUsername());
                log.fine("Mapping the role [" + role + "] to user [" + user.getUsername() + "]");
                roleDao.save(this.role);
                this.role = new Role();
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            String constraintViolation = e.getCause().getCause().getMessage();
            if (!constraintViolation.equals(null)) {
                return constraintViolation;
            } else {
                return e.getCause().getMessage();
            }
        }
    }


    /*
    * Get the roles for the given user
    * @return the user roles
    */
    private List<User> getUserAndRoles() {
        ArrayList<User> userList = new ArrayList<>();
        final List<User> userListfromDb = userDao.getUsers();
        for (User user : userListfromDb) {
            user.setRoles(roleDao.getRoles(user.getUsername()));
            userList.add(user);
        }
        return userList;
    }

}