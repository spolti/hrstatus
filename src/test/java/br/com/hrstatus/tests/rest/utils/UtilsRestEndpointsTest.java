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

package br.com.hrstatus.tests.rest.utils;

import br.com.hrstatus.tests.rest.RestEndpointsTestBase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@RunWith(Arquillian.class)
public class UtilsRestEndpointsTest {

    @Deployment(testable = false)
    public static WebArchive create() {
        return RestEndpointsTestBase.webArchive();
    }

    @Test
    @RunAsClient
    public void testSupportedOsEndpoint() {
        Response response = RestEndpointsTestBase.webTarget("/utils/resource/suported-os")
                .request(MediaType.APPLICATION_JSON)
                .get();
        String expected = "[\"UNIX\",\"WINDOWS\"]";
        String result = response.readEntity(String.class);
        Assert.assertEquals(expected, result);
    }

    @Test
    @RunAsClient
    public void testSupportedDBEndpoint() {
        Response response = RestEndpointsTestBase.webTarget("/utils/resource/suported-db")
                .request(MediaType.APPLICATION_JSON)
                .get();
        String expected = "[\"MYSQL\",\"ORACLE\",\"POSTGRESQL\",\"DB2\",\"SQLSERVER\",\"MONGODB\"]";
        String result = response.readEntity(String.class);
        Assert.assertEquals(expected, result);
    }

}