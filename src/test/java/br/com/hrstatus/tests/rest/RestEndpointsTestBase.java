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

package br.com.hrstatus.tests.rest;

import br.com.hrstatus.model.Database;
import br.com.hrstatus.model.OperatingSystem;
import br.com.hrstatus.model.Setup;
import br.com.hrstatus.model.User;
import br.com.hrstatus.model.support.VerificationStatus;
import br.com.hrstatus.model.support.response.RequestResponse;
import br.com.hrstatus.repository.Repository;
import br.com.hrstatus.repository.impl.DataBaseRepository;
import br.com.hrstatus.rest.VerificationEndpoint;
import br.com.hrstatus.rest.config.JaxRSConfig;
import br.com.hrstatus.utils.StartupTasks;
import br.com.hrstatus.utils.commands.Commands;
import br.com.hrstatus.utils.commands.impl.AbstractCommandExecutor;
import br.com.hrstatus.utils.commands.security.AllowedCommands;
import br.com.hrstatus.utils.date.DateUtils;
import br.com.hrstatus.utils.date.impl.DateUtilsImpl;
import br.com.hrstatus.utils.notification.Channel;
import br.com.hrstatus.utils.notification.channel.Email;
import br.com.hrstatus.utils.system.HrstatusSystem;
import br.com.hrstatus.utils.system.impl.AbstractSystemImpl;
import br.com.hrstatus.verification.executor.VerificationExecutor;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jcraft.jsch.Logger;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class RestEndpointsTestBase {

    public static WebArchive webArchive() {
        return ShrinkWrap.create(WebArchive.class, "hs.war")
                .addPackage(User.class.getPackage())
                .addPackage(Setup.class.getPackage())
                .addPackage(OperatingSystem.class.getPackage())
                .addPackage(VerificationStatus.class.getPackage())
                .addPackage(Database.class.getPackage())
                .addPackage(StartupTasks.class.getPackage())
                .addPackage(Logger.class.getPackage())
                .addPackage(VerificationEndpoint.class.getPackage())
                .addClasses(Repository.class, DataBaseRepository.class, JaxRSConfig.class, Commands.class, DateUtilsImpl.class,
                        VerificationExecutor.class, ThreadFactoryBuilder.class, DateUtils.class, Channel.class, Email.class, HrstatusSystem.class,
                        RequestResponse.class, AbstractCommandExecutor.class, AbstractSystemImpl.class, AllowedCommands.class)
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("test-version.properties", "version.properties")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    public static WebTarget webTarget(String path) {
        Client client = ClientBuilder.newClient();
        return client.target("http://localhost:8080/hs/rest" + path);
    }
}