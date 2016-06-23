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

package br.com.hrstatus.security.events;

import java.security.Principal;
import java.util.EventObject;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class SecurityEvent extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    private Principal userPrincipal;
    private String username;

    public SecurityEvent(Object source, Principal userPrincipal) {
        super(source);
        this.userPrincipal = userPrincipal;
    }

    /*
    * There is no Principal set when a authentication fails, set the username manually.
    */
    public SecurityEvent(Object source, String username) {
        super(source);
        this.username = username;
    }

    public Principal getUserPrincipal() {
        return userPrincipal;
    }

    public String getUsername() {
        return username;
    }
}
