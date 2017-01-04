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

import br.com.hrstatus.model.Setup;
import br.com.hrstatus.model.support.response.RequestResponse;
import br.com.hrstatus.repository.impl.DataBaseRepository;
import br.com.hrstatus.utils.notification.channel.Email;
import br.com.hrstatus.utils.system.HrstatusSystem;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Path("setup")
@Produces("application/json")
@Transactional
public class SetupResource {

    private Logger log = Logger.getLogger(SetupResource.class.getName());

    @Inject
    private Email emailChannel;
    @Inject
    private DataBaseRepository repository;
    @Inject
    private HrstatusSystem sys;
    @Inject
    private RequestResponse reqResponse;

    /*
    * Load the HrStatus configurations from database.
    */
    @GET
    @Path("available-mail-sessions")
    @RolesAllowed({"ROLE_ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response availableMailSessions() {
        return Response.ok(sys.mailSessios()).build();
    }

    /*
    * Load the HrStatus configurations from database.
    */
    @GET
    @Path("load")
    @RolesAllowed({"ROLE_ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response load() {
        return Response.ok(repository.loadConfiguration()).build();
    }

    /*
    * Update the hrstatus's configurations
    */
    @POST
    @Path("update")
    @RolesAllowed({"ROLE_ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Setup setup) {
        try {
            setup.setId(1);
            setup.setInstallationDate(repository.installationDate());
            log.fine("Configurations received to update: " + setup.toString());
            String result = String.valueOf(repository.update(setup));
            if ("success".equals(result)) {
                reqResponse.setResponseMessage("Configurações atualizadas con sucesso.");
                return Response.ok(reqResponse).build();
            } else {
                reqResponse.setResponseErrorMessage(result);
                return Response.status(Response.Status.BAD_REQUEST).entity(reqResponse).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            reqResponse.setResponseErrorMessage(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(reqResponse).build();
        }
    }
}