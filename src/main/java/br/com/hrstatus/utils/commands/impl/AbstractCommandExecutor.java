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

package br.com.hrstatus.utils.commands.impl;

import br.com.hrstatus.utils.commands.Commands;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class AbstractCommandExecutor implements Commands {

    private final Logger log = Logger.getLogger(AbstractCommandExecutor.class.getName());


    public String UnixLikeCommand(String cmd) {

        String retorno = "";
        BufferedReader br = null;

        try {
            final ProcessBuilder p = new ProcessBuilder(buildCommand(cmd));
            final Process process = p.start();
            final InputStream is = process.getInputStream();
            final InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                retorno = line;
            }

        } catch (IOException ioe) {
            log.severe("Error executing shell command: " + ioe.getMessage());
        } finally {
            secureClose(br);
        }
        return retorno;
    }

    public boolean isLocalhost(String host) {
        return true ? "localhost".equals(host) || "127.0.0.1".equals(host) : false;
    }

    /**
     * Build the command to be executed
     *
     * @param cmd command
     * @return the full command line
     */
    private ArrayList<String> buildCommand(String cmd) {
        final ArrayList<String> command = new ArrayList<String>();
        command.add("/bin/bash");
        command.add("-c");
        command.add(cmd);
        return command;
    }

    /**
     * Safely close the given closeable resource
     *
     * @param resource that will be closed - Closeable
     */
    private void secureClose(final Closeable resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (IOException ex) {
            log.severe("Error: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static class MyLogger implements com.jcraft.jsch.Logger {
        private static Logger log = Logger.getLogger(MyLogger.class.getName());
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

}