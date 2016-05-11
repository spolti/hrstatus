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

import br.com.hrstatus.action.os.unix.ExecuteLocalCommand;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Logger;

/*
 * @author spolti
 * Classes executeCommand e secureClose retrieved from:
 * http://www.devmedia.com.br/executando-shell-scripts-utilizando-java/26494
 */

@Singleton
@Startup
public class StartupSingletonVerifications {

    protected final Logger log = Logger.getLogger(StartupSingletonVerifications.class.getCanonicalName());
    private ExecuteLocalCommand shell = new ExecuteLocalCommand();

    private enum binaries {
        //ntpdate: used to update the date/time from Unix like servers, local and remote
        //net (samba-common package): used to obtain date/time from Windows server
        ntpdate, net
    }

    @PostConstruct
    public void verifyBinaries() {
        String result = "";
        log.info("Checking binaries required for the execution of HrStatus...");

        for (binaries bin : binaries.values()) {
            result = shell.executeCommand("type " + bin.name() + ";  echo $?");

            if ("0".equals(result)) {
                log.info("Binary " + bin.name() + ": OK");
            } else {
                log.warning("Binary " + bin.name() + ": Not found, this can cause strange behavior of some functionalities of HrStatus.");
            }
        }
    }
}