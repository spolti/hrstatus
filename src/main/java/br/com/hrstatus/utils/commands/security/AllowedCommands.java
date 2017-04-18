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

package br.com.hrstatus.utils.commands.security;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public enum AllowedCommands {

    ECHO("/usr/bin/echo"),
    ECHO_TEST("/usr/bin/echo Hello World."),
    TYPE("/usr/bin/type"),
    DATE("/usr/bin/date"),
    UPDATE_CLIENT_DATE("/usr/bin/sudo /usr/bin/ntpdate -u"),

    // Windows net commands
    NET_TIME_I("/usr/bin/net time -I"),

    // This section below is used to place the command to check if a binary needed by HrStatus is installed
    // ntpdate: used to update the date/time from Unix like servers, local and remote
    // net (samba-common package): used to obtain date/time from Windows server
    // for fedora 24 or higher: samba-commom-tools
    VERIFY_NTPDATE_BINARY(AllowedCommands.TYPE.get() + " ntpdate ;  " + AllowedCommands.ECHO.get() + " $?"),
    VERIFY_NET_BYNARY(AllowedCommands.TYPE.get() + " net ;  " + AllowedCommands.ECHO.get() + " $?");

    private final String command;

    AllowedCommands(String command) {
        this.command = command;
    }

    /**
     * @return the command to be executed on the target host
     */
    public String get() {
        return command;
    }

}