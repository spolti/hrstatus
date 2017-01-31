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

package br.com.hrstatus.utils.system.impl;

import br.com.hrstatus.utils.system.HrstatusSystem;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class AbstractSystemImpl implements HrstatusSystem {

    private final Logger log = Logger.getLogger(AbstractSystemImpl.class.getName());

    public String getServerHttpAddress() {
        try {
            final ObjectName http = new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http");
            return mBeanServer().getAttribute(http, "boundAddress") + ":" + mBeanServer().getAttribute(http, "boundPort");
        } catch (Exception e ){
            e.printStackTrace();
            return "localhost:8080";
        }
    }

    public String uptime() {
        Duration duration = Duration.ofMillis(ManagementFactory.getRuntimeMXBean().getUptime());
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        long seconds = duration.minusHours(hours).minusMinutes(minutes).getSeconds();
        return hours + " Hora(s), " + minutes + " Minuto(s) e " + seconds + " Segundo(s)";
    }

    public List<String> mailSessios() {

        final ArrayList<String> mailSessions = new ArrayList<String>();

        try {
            mBeanServer().queryNames(new ObjectName("jboss.as:subsystem=mail,mail-session=*"),null).stream().forEach(mbean ->{
                try {
                    mailSessions.add((String) mBeanServer().getAttribute(mbean, "jndi-name"));
                } catch (MBeanException e) {
                    e.printStackTrace();
                } catch (AttributeNotFoundException e) {
                    e.printStackTrace();
                } catch (InstanceNotFoundException e) {
                    e.printStackTrace();
                } catch (ReflectionException e) {
                    e.printStackTrace();
                }
            });
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }

        return mailSessions;
    }


    /**
     * @return {@link MBeanServer}
     */
    private MBeanServer mBeanServer() {
        return ManagementFactory.getPlatformMBeanServer();
    }
}