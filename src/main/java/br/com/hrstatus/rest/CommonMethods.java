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

import br.com.hrstatus.model.Database;
import br.com.hrstatus.model.OperatingSystem;
import br.com.hrstatus.model.support.VerificationStatus;
import br.com.hrstatus.model.support.response.RequestResponse;
import br.com.hrstatus.repository.impl.DataBaseRepository;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Path("common")
@Transactional
public class CommonMethods {

    private Logger log = Logger.getLogger(CommonMethods.class.getName());

    @Inject
    private DataBaseRepository repository;
    @Inject
    private RequestResponse reqResponse;

    /**
     * Resources by {@link VerificationStatus}
     *
     * @param type   accepted values [os, db]
     * @param status {@link VerificationStatus}
     * @return a list of resources according with its status
     */
    @GET
    @Path("resources-status/{type}/{status}")
    @RolesAllowed({"ROLE_ADMIN, ROLE_USER", "ROLE_REST"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResourcesByStatus(@PathParam("type") String type, @PathParam("status") String status) {
        Class clazz = null;
        VerificationStatus i;

        if (type.equals("os")) {
            clazz = OperatingSystem.class;
        } else if (type.equals("db")) {
            clazz = Database.class;
        } else {
            reqResponse.setFailedSubject("Parâmetro Inválido");
            reqResponse.setResponseErrorMessage("O parâmetro " + type + " não é válido.");
            return Response.status(Response.Status.BAD_REQUEST).entity(reqResponse).build();
        }

        try {
            i = VerificationStatus.valueOf(status);
            return Response.ok(Response.Status.OK).entity(repository.getResourcesByStatus(clazz, i)).build();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            reqResponse.setFailedSubject("Parâmetro Inválido");
            reqResponse.setResponseErrorMessage(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(reqResponse).build();
        }
    }
}