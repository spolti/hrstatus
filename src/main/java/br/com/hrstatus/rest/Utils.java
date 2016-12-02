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
import br.com.hrstatus.repository.Repository;
import br.com.hrstatus.repository.impl.DataBaseRepository;
import br.com.hrstatus.utils.notification.Notification;
import br.com.hrstatus.utils.notification.channel.Email;
import br.com.hrstatus.utils.system.HrstatusSystem;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
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

    /*
    * Send an Email test.
    */
    @POST
    @Path("mail/test")
    public void sendTestEmail(@FormParam("testMailJndi") String mailJndi, @FormParam("dest") String dest,
                              @Context HttpServletRequest request, @Context HttpServletResponse response) throws ServletException, IOException {

        // send the test email in the same thread, client side needs a feedback.
        request.setAttribute("message", new Notification()
                .usingJndi(mailJndi)
                .send("Não responder esta mensagem, é apenas uma mensagem de teste")
                .subject("HrStatus - Mensagem de teste")
                .to(dest).by(emailChannel));
        request.setAttribute("mailJndis", sys.mailSessios());
        request.setAttribute("setup", repository.loadConfiguration());
        request.getRequestDispatcher("/admin/setup.jsp").forward(request, response);
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
    @Path("resource/suported-databases")
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