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

package br.com.hrstatus.servlets;

import br.com.hrstatus.repository.Repository;
import br.com.hrstatus.utils.PropertiesLoader;
import br.com.hrstatus.utils.system.HrstatusSystem;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@WebServlet(name = "aboutServlet", urlPatterns = {"/home/about"})
@ServletSecurity(@HttpConstraint(rolesAllowed={"ROLE_USER", "ROLE_ADMIN"}))
public class AboutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private Logger log = Logger.getLogger(AboutServlet.class.getName());

    @Inject
    private PropertiesLoader loader;
    @Inject
    private HrstatusSystem sys;
    @Inject
    private Repository repository;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Coletando informações sobre o servidor e colocando na sessão...");
        request.setAttribute("version", loader.getValor("version"));
        request.setAttribute("javaVersion", System.getProperty("java.runtime.version"));
        request.setAttribute("java", System.getProperty("java.vm.name"));
        request.setAttribute("javaVendor", System.getProperty("java.vendor"));
        request.setAttribute("osVersion", System.getProperty("os.version"));
        request.setAttribute("installationDate", repository.installationDate());
        request.setAttribute("uptime", sys.uptime());
        request.getRequestDispatcher("/home/about.jsp").forward(request, response);
    }
}