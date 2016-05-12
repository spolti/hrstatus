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

import br.com.hrstatus.utils.commands.Commands;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */

@Startup
@Singleton
public class StartupTasks {

    private final Logger log = Logger.getLogger(StartupTasks.class.getName());

    @Inject
    private Commands command;

    @PostConstruct
    public void Startup() {
        log.info("Iniciando verificações do startup...");
        verifyBiniries();
    }


    @PreDestroy
    public void Shutdown() {
        log.info("HrStatus em processo de Shutdown...");
    }

    /*
    * Check if the neede binaries are installed on the server
    * just print in the logs if the if the target binarie is installed or not
    */
    private void verifyBiniries() {
        String result = "1";
        for (binaries bin : binaries.values()) {
            result = command.UnixLikeCommand("type " + bin.name() + ";  echo $?");
            if (result.equals("0")) {
                log.info("Binário " + bin.name() + ": OK");
            } else {
                log.warning("Binário " + bin.name() + ": Não encontrado, isto pode causar alguns comportamentos inesperados no HrStatus.");
            }
        }
    }

    /*
    * Contains all needed binaries
    */
    private enum binaries {
        //ntpdate: used to update the date/time from Unix like servers, local and remote
        //net (samba-common package): used to obtain date/time from Windows server
        ntpdate, net
    }
}