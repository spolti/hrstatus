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

import br.com.hrstatus.repository.impl.DataBaseRepository;
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
    @Inject
    private DataBaseRepository database;

    /*
    * Contains all needed binaries
    */
    private enum binary {
        //ntpdate: used to update the date/time from Unix like servers, local and remote
        //net (samba-common package): used to obtain date/time from Windows server
        //for fedora 24 or higher: samba-commom-tools
        ntpdate, net
    }

    @PostConstruct
    public void Startup() {
        log.info("Iniciando verificações de startup...");
        verifyBinaries();
    }

    @PreDestroy
    public void Shutdown() {
        log.info("HrStatus em processo de Shutdown...");
    }

    /*
    * Check if the needed binaries are installed on the server
    * just print in the logs if the if the target binary is installed or not
    */
    private void verifyBinaries() {
        String result = "1";
        for (binary bin : binary.values()) {
            result = command.UnixLikeCommand("type " + bin.name() + ";  echo $?");
            if (result.equals("0")) {
                log.info("Binário " + bin.name() + ": OK");
            } else {
                log.warning("Binário " + bin.name() + ": Não encontrado, isto pode causar alguns comportamentos inesperados no HrStatus.");
            }
        }

        log.info("Importing initial database data...");
        database.initialImport();
        log.info("Done.");
    }
}