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

package br.com.hrstatus.utils.mail;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/*
 * @author spolti
 */

public class GetAvailableMailSession {
	
	Logger log =  Logger.getLogger(GetAvailableMailSession.class.getCanonicalName());
	
	public ArrayList<String> listAllMailSessions() throws Exception {
		
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		ObjectName serviceRef = new ObjectName("jboss.as:subsystem=mail,mail-session=*");
		Set<ObjectName> mbeans = mBeanServer.queryNames(serviceRef, null);
		ArrayList<String> mailSessions = new ArrayList<String>();

		for (ObjectName on : mbeans) {
			log.fine("Available Mail Sessions: " + mBeanServer.getAttribute(on, "jndi-name"));
			if (on.toString().contains("jboss.as")){
				mailSessions.add((String) mBeanServer.getAttribute(on, "jndi-name"));
			}
		}
		return mailSessions;
	}
}