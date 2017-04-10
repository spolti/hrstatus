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
import br.com.hrstatus.utils.commands.security.AllowedCommands;
import br.com.hrstatus.verification.executor.VerificationExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Startup
@Singleton
public class StartupTasks {

    private final Logger log = Logger.getLogger(StartupTasks.class.getName());
    private final InetAddress LOCALHOST = InetAddress.getByName("127.0.0.1");

    @Inject
    private Commands command;
    @Inject
    private DataBaseRepository repository;
    @Inject
    private VerificationExecutor executor;

    public StartupTasks() throws UnknownHostException {
    }

    @PostConstruct
    public void Startup() {
        log.info("Iniciando verificações de startup...");
        verifyBinaries();
        log.info("Importing initial database data...");
        repository.initialImport();
        log.info("Done.");

        executor.startExecutorService();
    }

    @PreDestroy
    public void Shutdown() {
        log.info("HrStatus em processo de Shutdown...");
        executor.shutdownExecutorService();
    }

    /**
     * Check if the needed binaries are installed on the server
     * just print in the logs if the if the target binary is installed or not
     * This was changed to use ENUMs to avoid the {@link br.com.hrstatus.utils.commands.impl.AbstractCommandExecutor} accped any kind of commands
     */
    private void verifyBinaries() {
        log.info("Verificando os binários necessários...");
        if (command.run(AllowedCommands.VERIFY_NTPDATE_BINARY, LOCALHOST, 0, null).equals("0")) {
            log.info("Binário ntpdate OK");
        } else {
            log.warning("Binário ntpdate: Não encontrado, isto pode causar alguns comportamentos inesperados no HrStatus.");
        }

        if (command.run(AllowedCommands.VERIFY_NET_BYNARY, LOCALHOST, 0,null).equals("0")) {
            log.info("Binário net: OK");
        } else {
            log.warning("Binário net: Não encontrado, isto pode causar alguns comportamentos inesperados no HrStatus.");
        }
    }
}