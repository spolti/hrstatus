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

package br.com.hrstatus.utils;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;
import java.util.logging.Logger;

/*
 * @author spolti
 */

public class GetServerIPAddress {

    Logger log = Logger.getLogger(GetServerIPAddress.class.getCanonicalName());

    public String returnServerAddres() throws UnknownHostException {

        final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        String url = null;
        try {

            final ObjectName http = new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http");
            final String jbossHttpAddress = (String) mBeanServer.getAttribute(http, "boundAddress");
            final int jbossHttpPort = (Integer) mBeanServer.getAttribute(http, "boundPort");
            url = jbossHttpAddress + ":" + jbossHttpPort;
            log.fine("Url obtained from the system: " + url);
        } catch (Exception e) {
            log.severe(e.getStackTrace().toString());
        }
        return url;
    }
}