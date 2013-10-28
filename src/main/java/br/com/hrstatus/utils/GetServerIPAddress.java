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

import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.log4j.Logger;

/*
 * @author spolti
 */

public class GetServerIPAddress {

	public String returnIP() throws UnknownHostException {

		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		String url = null;
		try {

			ObjectName http = new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http");
			String jbossWebHost = 	(String) mBeanServer.getAttribute(http, "boundAddress");
			int jbossWebPort = (Integer) mBeanServer.getAttribute(http, "boundPort");
			url = jbossWebHost +":"+jbossWebPort;
			Logger.getLogger(getClass()).info("A url obtida do sistema : ");
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e);
		}
		return url;
	}
}