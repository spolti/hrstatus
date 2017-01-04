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

import br.com.hrstatus.model.support.SupportedDatabase;
import br.com.hrstatus.model.support.SupportedOperatingSystem;
import br.com.hrstatus.model.support.response.RequestResponse;
import br.com.hrstatus.repository.impl.DataBaseRepository;
import br.com.hrstatus.utils.notification.Notification;
import br.com.hrstatus.utils.notification.channel.Email;
import br.com.hrstatus.utils.system.HrstatusSystem;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Path("utils")
public class Utils {


    @Inject
    private Email emailChannel;
    @Inject
    private DataBaseRepository repository;
    @Inject
    private HrstatusSystem sys;
    @Inject
    private RequestResponse reqResponse;

    /*
    * Send an Email test.
    */
    @POST
    @Path("mail/test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendTestEmail(@QueryParam("jndi") String jndi, @QueryParam("dest") String dest) throws IOException {
        // send the test email in the same thread, client side needs feedback.
        reqResponse.setResponseMessage(new Notification()
                .usingJndi(jndi)
                .send("Não responder esta mensagem, é apenas uma mensagem de teste")
                .subject("HrStatus - Mensagem de teste")
                .to(dest).by(emailChannel));
        return Response.ok(reqResponse).build();
    }

    /*
    * Returns the supported operating systems
    */
    @GET
    @Path("resource/suported-os")
    @Produces("application/json")
    public String[] supportedOs() {
        return getNames(SupportedOperatingSystem.class);
    }

    /*
    * Returns the supported databases
    */
    @GET
    @Path("resource/suported-db")
    public String[] supportedDatabases() {
        return getNames(SupportedDatabase.class);
    }

    /*
    * Returns a String[] with all Enum connstants.
    */
    private String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }

}