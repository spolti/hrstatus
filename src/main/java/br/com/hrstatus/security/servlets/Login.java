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

package br.com.hrstatus.security.servlets;

import br.com.hrstatus.model.User;
import br.com.hrstatus.repository.Repository;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Transactional
@WebServlet(name = "loginServlet", urlPatterns = {"/login"})
public class Login extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final Logger log = Logger.getLogger(Login.class.getName());

    @Inject
    private User user;
    @Inject
    private Repository repository;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



       try {
            try {
                user = repository.search(User.class, "username", request.getParameter("j_username"));
            } catch (Exception e) {
                //user not found
                log.fine("Usuário " + request.getParameter("j_username") + " não cadastrado.");
                request.getRequestDispatcher("login.jsp?failed=true").forward(request,response);
            }
            if (user.isEnabled()) {
                log.info("PASSWORD: " +  request.getParameter("j_username") + " - " + request.getParameter("j_password"));
                user.setLastLoginAddressLocation(request.getRemoteAddr());
                repository.update(user);
                request.login(request.getParameter("j_username"), request.getParameter("j_password"));
                request.getRequestDispatcher("/home/home.jsp").forward(request,response);
            } else {
                request.getRequestDispatcher("login.jsp?failed=true").forward(request,response);
            }
        } catch (ServletException ex ) {
            response.setHeader("UT010031", request.getParameter("j_username"));
            request.getRequestDispatcher("login.jsp?failed=true").forward(request,response);
        }
    }
}