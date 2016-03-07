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

package br.com.hrstatus.action.os;

import java.util.logging.Logger;

/*
 * @author spolti
 */
public abstract class CommandExecutionHelper {

    private final static Logger log = Logger.getLogger(CommandExecutionHelper.class.getName());

    @SuppressWarnings("unchecked")
    public static class MyLogger implements com.jcraft.jsch.Logger {
        @SuppressWarnings("rawtypes")
        static java.util.Hashtable name = new java.util.Hashtable();
        static {
            name.put(new Integer(DEBUG), "DEBUG: ");
            name.put(new Integer(INFO), "INFO: ");
            name.put(new Integer(WARN), "WARN: ");
            name.put(new Integer(ERROR), "ERROR: ");
            name.put(new Integer(FATAL), "FATAL: ");
        }

        public boolean isEnabled(int level) {
            return true;
        }

        public void log(int level, String message) {
            System.out.print(name.get(new Integer(level)));
            log.fine(message);
        }
    }

   /*
    * Returns true if the host is localhost or 127.0.0.1
    */
    public static boolean isLocalhost (String host) {
        return true ? host.equals("localhost") || host.equals("127.0.0.1") : false;
    }
}