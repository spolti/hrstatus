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

import br.com.hrstatus.repository.Repository;
import br.com.hrstatus.utils.notification.channel.Email;
import br.com.hrstatus.utils.system.HrstatusSystem;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Path("setup")
@Produces("application/json")
@Transactional
public class Setup {

    private Logger log = Logger.getLogger(Setup.class.getName());

    @Inject
    private Email emailChannel;
    @Inject
    private Repository repository;
    @Inject
    private HrstatusSystem sys;

    /*
    * Load the HrStatus configurations from database.
    */
    @GET
    @Path("load")
    public void load(@Context HttpServletRequest request,  @Context HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mailJndis", sys.mailSessios());
        request.setAttribute("setup", repository.loadConfiguration());
        request.getRequestDispatcher("/admin/setup.jsp").forward(request, response);
    }
}