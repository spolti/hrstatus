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
import br.com.hrstatus.utils.PropertiesLoader;
import br.com.hrstatus.utils.notification.Notification;
import br.com.hrstatus.utils.notification.channel.Email;
import br.com.hrstatus.utils.system.HrstatusSystem;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
    @Inject
    private PropertiesLoader loader;

    /**
     * Sends a test email to test the mail session configuration in the WildFly Server
     *
     * @param jndi String - mail-jndi
     * @param dest String - Receipt
     * @return {@link Response} with the operation's result
     * @throws IOException for IO problems
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

    /**
     * list all available and supported OSs
     *
     * @return a String[] with all Enum constants
     */
    @GET
    @Path("resource/suported-os")
    @Produces("application/json")
    public String[] supportedOs() {
        return getNames(SupportedOperatingSystem.class);
    }

    /**
     * list all available and supported databases
     *
     * @return a String[] with all Enum constants
     */
    @GET
    @Path("resource/suported-db")
    public String[] supportedDatabases() {
        return getNames(SupportedDatabase.class);
    }

    /**
     * list all available and supported databases
     *
     * @return a String[] with all Enum constants
     */
    @GET
    @Path("resource/server-info")
    @Produces(MediaType.APPLICATION_JSON)
    public Response AboutServerInformation() {

        class ServerInfo {

            String version;
            String javaVersion;
            String java;
            String javaVendor;
            String osVersion;
            String installationDate;
            String uptime;

            ServerInfo() {
                version = loader.getValor("version");
                javaVersion = System.getProperty("java.runtime.version");
                java = System.getProperty("java.vm.name");
                javaVendor = System.getProperty("java.vendor");
                osVersion = System.getProperty("os.version");
                installationDate = repository.installationDate().toString();
                uptime = sys.uptime();
            }

            public String getVersion() {
                return version;
            }

            public String getJavaVersion() {
                return javaVersion;
            }

            public String getJava() {
                return java;
            }

            public String getJavaVendor() {
                return javaVendor;
            }

            public String getOsVersion() {
                return osVersion;
            }

            public String getInstallationDate() {
                return installationDate;
            }

            public String getUptime() {
                return uptime;
            }
        }
        return Response.ok(new ServerInfo()).build();
    }

    /**
     * Auxiliary method to get all enum names from a Enum
     *
     * @param e Enum Class
     * @return a String[] with all Enum constants
     */
    private String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }

}