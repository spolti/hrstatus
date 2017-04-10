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

package br.com.hrstatus.tests.commands;

import br.com.hrstatus.utils.GetLocalAddress;
import br.com.hrstatus.utils.commands.impl.AbstractCommandExecutor;
import br.com.hrstatus.utils.commands.security.AllowedCommands;
import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class CommandsTest {

    final InetAddress LOCALHOST = InetAddress.getByName("127.0.0.1");

    AbstractCommandExecutor abstractCommandExecutor = new AbstractCommandExecutor();

    public CommandsTest() throws UnknownHostException {}

    @Test
    public void testLocalCommands() {
        Assert.assertNotNull(abstractCommandExecutor.run(AllowedCommands.DATE, LOCALHOST, 0, null));
    }

    /**
     * This test is disabled by default, to enable it just add a new user as follows in the credentials setup below
     * and uncomment the @Test annotation
     */
    //@Test
    public void testRemoteCommand() {
        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("username", "usertest");
        credentials.put("password", "testpassword");
        Assert.assertEquals("Hello World.", abstractCommandExecutor.run(AllowedCommands.ECHO_TEST, GetLocalAddress.getHostAddress(), 22, credentials));
    }

    @Test
    public void testBinaries() {
        Assert.assertEquals("Binário ntpdate é necessário.","0", abstractCommandExecutor.run(AllowedCommands.VERIFY_NTPDATE_BINARY, LOCALHOST, 0, null));
        Assert.assertEquals("Binário net é necessário.","0", abstractCommandExecutor.run(AllowedCommands.VERIFY_NET_BYNARY, LOCALHOST, 0, null));
    }
}