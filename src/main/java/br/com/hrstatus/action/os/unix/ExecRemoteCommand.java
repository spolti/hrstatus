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

package br.com.hrstatus.action.os.unix;

import br.com.hrstatus.action.os.CommandExecutionHelper;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/*
 * @author spolti
 */

public class ExecRemoteCommand extends CommandExecutionHelper {

    protected static final Logger log = Logger.getLogger(ExecRemoteCommand.class.getName());
    private static ExecuteLocalCommand shell = new ExecuteLocalCommand();

    public static String exec(String user, String host, String password, int port, String command) throws JSchException, IOException {

        String s = "";
        log.fine("Executing command " + command + " against " + host);
        if (!isLocalhost(host)) {
            // Disabling host key check
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");

            // Creating the server session and connecting
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setConfig(config);
            session.setPassword(password);
            session.connect(10000);

            // Executing the command
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
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

            channel.disconnect();
            session.disconnect();
            return s.replaceAll("\n", "");

        } else {
            log.fine("Host received is localhost, performing local command execution...");
            return shell.executeCommand("/bin/date");
        }
    }
}