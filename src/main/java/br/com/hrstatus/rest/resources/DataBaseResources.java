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

import br.com.hrstatus.model.Database;
import br.com.hrstatus.model.support.VerificationStatus;
import br.com.hrstatus.model.support.response.RequestResponse;
import br.com.hrstatus.repository.impl.DataBaseRepository;
import br.com.hrstatus.security.PasswordUtils;

import javax.annotation.security.RolesAllowed;
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
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Path("resource/database")
@Produces("application/json")
@Transactional
public class DataBaseResources {

    private Logger log = Logger.getLogger(DataBaseResources.class.getName());

    @Inject
    private DataBaseRepository repository;
    @Inject
    private RequestResponse reqResponse;

    /**
     * @return {@link Response} List&#60;@{@link Database}&#62; as json
     */
    @Path("list")
    @GET
    @RolesAllowed({"ROLE_ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response list() {
        return Response.ok(repository.list(Database.class)).build();
    }

    /**
     * Persists the given json object in the database
     *
     * @param database {@link Database}
     * @return {@link Response} with the operation result, success or failure
     */
    @POST
    @Path("new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newDatabase(Database database) {
        log.fine("Banco de Dados recebido para cadastro: " + database.toString());
        database.setStatus(VerificationStatus.NOT_VERIFIED);
        database.setPassword(PasswordUtils.encode(database.getPassword()));
        String result = repository.register(database).toString();
        if ("success".equals(result)) {
            log.fine("Resource successfully created.");
            reqResponse.setResponseMessage("Recurso criado com sucesso");
            return Response.ok(reqResponse).build();
        } else {
            log.warning("Failed to persist object: " + result);
            reqResponse.setFailedSubject(database.getHostname());
            reqResponse.setResponseErrorMessage(result);
            return Response.status(Response.Status.BAD_REQUEST).entity(reqResponse).build();
        }
    }

    /**
     * Delete the given {@link Database} by id
     *
     * @param id int
     * @return {@link Response} with the operation result, success or failure
     */
    @DELETE
    @Path("delete/{id}")
    public Response deleteOs(@PathParam("id") int id) {
        repository.delete(repository.search(Database.class, "id", id));
        return Response.ok().build();
    }

    /**
     * Search a {@link Database} by id
     *
     * @param db int
     * @return {@link Response} with {@link Database} object
     */
    @GET
    @Path("search/{db}")
    @RolesAllowed({"ROLE_ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@PathParam("db") int db) {
        log.fine("Database Id {" + db + "} recebido para pesquisa.");
        return Response.ok(repository.search(Database.class, "id", db)).build();
    }

    /**
     * Update the given {@link Database}
     *  Note that, to make sure that the password of the given password was updated it first try to decode.
     *  If the decode operation fails it will go to the catch instruction and then encrypt the password.
     *
     * @param database json object
     * @return {@link Response} with the operation result, success or failure
     */
    @POST
    @Path("update")
    @RolesAllowed({"ROLE_ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Database database) {
        try {
            PasswordUtils.decode(database.getPassword());
        } catch (Exception e) {
            try {
                database.setPassword(PasswordUtils.encode(database.getPassword()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        log.fine("Operating System received to update: " + database.toString());
        String result = String.valueOf(repository.update(database));
        if (("success").equals(result)) {
            reqResponse.setResponseMessage(" O Banco de Dados " + database.getHostname() + " foi atualizado com sucesso.");
            return Response.ok(reqResponse).build();
        } else {
            reqResponse.setFailedSubject(database.getHostname());
            reqResponse.setResponseErrorMessage(result);
            return Response.status(Response.Status.BAD_REQUEST).entity(reqResponse).build();
        }
    }

}