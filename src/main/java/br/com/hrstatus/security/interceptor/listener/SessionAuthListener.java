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

package br.com.hrstatus.security.interceptor.listener;

import br.com.hrstatus.security.interceptor.event.AuthenticatedEvent;
import br.com.hrstatus.security.interceptor.event.FailedAuthenticatedEvent;
import br.com.hrstatus.security.interceptor.event.LoggedOutEvent;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@SessionScoped
public class SessionAuthListener implements Serializable {

    private Logger log = Logger.getLogger(SessionAuthListener.class.getName());

    public void onAuthenticated(@Observes AuthenticatedEvent event) {
        log.fine("Successfull login for " + event.getUserPrincipal().getName());

    }
    public void onAuthenticationFailure(@Observes FailedAuthenticatedEvent event) {
        log.fine("Falha de autenticação usuário " +  event.getUsername() + ", número de tentativas: IMPLEMENTARRRRRRRRRRRRRRRRR");

    }

    public void onLoggedOut(@Observes LoggedOutEvent event) {
        log.fine("Usuário " +  event.getUserPrincipal().getName() + " efetuou logout.");
    }
}