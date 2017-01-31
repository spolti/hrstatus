package br.com.hrstatus.rest;

import br.com.hrstatus.repository.impl.DataBaseRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Path("public")
public class PublicResources {

    @Inject
    private DataBaseRepository repository;

    /**
     * This message is displayed in the login page
     *
     * @return {@link Response} with welcome message
     */
    @GET
    @Path("welcome-message")
    public Response welcomeMessage() {
        return Response.ok(repository.welcomeMessage()).build();
    }
}
