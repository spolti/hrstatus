package br.com.hrstatus.action.os.unix;

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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author spolti
 */
public class ExecuteLocalCommand {

    private Logger log = Logger.getLogger(ExecRemoteCommand.class.getName());

    public String executeCommand(final String command) {

        final ArrayList<String> commands = new ArrayList<String>();
        String retorno = null;
        commands.add("/bin/bash");
        commands.add("-c");
        commands.add(command);

        BufferedReader br = null;

        try {
            final ProcessBuilder p = new ProcessBuilder(commands);
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


    private void secureClose(final Closeable resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (IOException ex) {
            log.severe("Error: " + ex.getMessage());
        }
    }
}