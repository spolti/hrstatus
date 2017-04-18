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
import br.com.hrstatus.utils.commands.security.AllowedCommands;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class AbstractCommandExecutor implements Commands {

    private final Logger log = Logger.getLogger(AbstractCommandExecutor.class.getName());

    private JSch jsch;
    private Channel channel;
    private Session session;

    public String run(AllowedCommands command, InetAddress address, int port, HashMap<String, String> credentials) {
        // net time command is executed locally.
        if (address.isLoopbackAddress() || command.equals(AllowedCommands.NET_TIME_I)) {
            return runLocal(command, address);
        } else {
            return runRemote(command, address, port, credentials);
        }
    }

    /**
     * Perform a remote command on target host
     *
     * @param command
     * @param address
     * @param credentials
     * @return the command result
     */
    private String runRemote(AllowedCommands command, InetAddress address, int port, HashMap<String, String> credentials) {

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        String s = "";

        try {
            // Creating the server session and connecting
            jsch = new JSch();
            session = jsch.getSession(credentials.get("username"), address.getHostAddress(), port);
            session.setConfig(config);
            session.setPassword(credentials.get("password"));
            session.connect(10000);

            // Executing the command
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command.get());
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] tmp = new byte[1024];
            boolean test = true;
            while (test == true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    s += (new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    break;
                }
            }

            return s.replaceAll("\n", "");

        } catch (JSchException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            try {
                channel.disconnect();
                session.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Perform a local command
     *
     * @param command
     * @param address
     * @return the command result
     */
    private String runLocal(AllowedCommands command, InetAddress address) {
        String retorno = "";
        BufferedReader br = null;

        log.fine("Tentando executar o comando [" + command.get() + " " + address.getHostAddress() + "]");

        try {
            ProcessBuilder p;
            if (command.equals(AllowedCommands.NET_TIME_I)) {
                p = new ProcessBuilder(buildCommand(command.get() + " " + address.getHostAddress()));
            } else {
                p = new ProcessBuilder(buildCommand(command.get()));
            }
            final Process process = p.start();
            final InputStream is = process.getInputStream();
            final InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                retorno = line;
            }

        } catch (IOException ioe) {
            log.severe("Error while executing command: " + ioe.getMessage());
        } finally {
            secureClose(br);
        }
        return retorno;
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
        @SuppressWarnings("rawtypes")
        static java.util.Hashtable name = new java.util.Hashtable();
        private static Logger log = Logger.getLogger(MyLogger.class.getName());

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