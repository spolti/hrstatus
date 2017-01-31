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

package br.com.hrstatus.rest.resources;

import br.com.hrstatus.model.OperatingSystem;
import br.com.hrstatus.model.support.VerificationStatus;
import br.com.hrstatus.model.support.response.RequestResponse;
import br.com.hrstatus.repository.impl.DataBaseRepository;
import br.com.hrstatus.security.PasswordUtils;

import javax.annotation.security.RolesAllowed;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Path("resource/operating-system")
@Produces("application/json")
@Transactional
public class OperatingSystemResources {

    private Logger log = Logger.getLogger(OperatingSystemResources.class.getName());

    @Inject
    private DataBaseRepository repository;
    @Inject
    private RequestResponse reqResponse;

    /**
     * @return {@link Response} with tlist of {@link OperatingSystem}
     */
    @GET
    @Path("list")
    public Response list() {
        return Response.ok(repository.list(OperatingSystem.class)).build();
    }

    /**
     * Register a new {@link OperatingSystem}
     *
     * @param operatingSystem json object
     * @return {@link Response} with the operation result, success or failure
     */
    @POST
    @Path("new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newOperationgSystem(OperatingSystem operatingSystem) {
        log.fine("Sistema Operacional recebido para cadastro: " + operatingSystem.toString());
        operatingSystem.setStatus(VerificationStatus.NOT_VERIFIED);
        operatingSystem.setPassword(PasswordUtils.encode(operatingSystem.getPassword()));
        String result = repository.register(operatingSystem).toString();
        if ("success".equals(result)) {
            log.fine("Resource successfully created.");
            reqResponse.setResponseMessage("Recurso criado com sucesso");
            return Response.ok(reqResponse).build();
        } else {
            log.warning("Failed to persist object: " + result);
            reqResponse.setFailedSubject(operatingSystem.getHostname());
            reqResponse.setResponseErrorMessage(result);
            return Response.status(Response.Status.BAD_REQUEST).entity(reqResponse).build();
        }
    }

    /**
     * Delete the given Object by id
     *
     * @param id int
     * @return {@link Response} with the operation result, success or failure
     */
    @DELETE
    @Path("delete/{id}")
    public Response deleteOs(@PathParam("id") int id) {
        repository.delete(repository.search(OperatingSystem.class, "id", id));
        return Response.ok().build();
    }

    /**
     * Search a {@link OperatingSystem} by id
     *
     * @param os int
     * @return {@link Response} with {@link OperatingSystem} object
     */
    @GET
    @Path("search/{os}")
    @RolesAllowed({"ROLE_ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@PathParam("os") int os) {
        log.fine("OS Id {" + os + "} recebido para pesquisa.");
        return Response.ok(repository.search(OperatingSystem.class, "id", os)).build();
    }

    /**
     * Update the given {@link OperatingSystem}
     *  Note that, to make sure that the password of the given password was updated it first try to decode.
     *  If the decode operation fails it will go to the catch instruction and then encrypt the password.
     *
     * @param operatingSystem json object
     * @return {@link Response} with the operation result, success or failure
     */
    @POST
    @Path("update")
    @RolesAllowed({"ROLE_ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(OperatingSystem operatingSystem) {
        try {
            PasswordUtils.decode(operatingSystem.getPassword());
        } catch (Exception e) {
            try {
                operatingSystem.setPassword(PasswordUtils.encode(operatingSystem.getPassword()));
            } catch (Exception e1) {
                e.printStackTrace();
            }
        }
        log.fine("Operating System received to update: " + operatingSystem.toString());

        String result = String.valueOf(repository.update(operatingSystem));
        if (("success").equals(result)) {
            reqResponse.setResponseMessage(" O Sistema Operacional " + operatingSystem.getHostname() + " foi atualizado com sucesso.");
            return Response.ok(reqResponse).build();
        } else {
            reqResponse.setFailedSubject(operatingSystem.getHostname());
            reqResponse.setResponseErrorMessage(result);
            return Response.status(Response.Status.BAD_REQUEST).entity(reqResponse).build();
        }
    }
}