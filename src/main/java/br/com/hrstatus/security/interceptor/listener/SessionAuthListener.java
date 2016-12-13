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

import br.com.hrstatus.model.User;
import br.com.hrstatus.repository.Repository;
import br.com.hrstatus.security.interceptor.event.AuthenticatedEvent;
import br.com.hrstatus.security.interceptor.event.FailedAuthenticatedEvent;
import br.com.hrstatus.security.interceptor.event.LoggedOutEvent;
import br.com.hrstatus.utils.date.DateUtils;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Stateless
public class SessionAuthListener implements Serializable {

    private Logger log = Logger.getLogger(SessionAuthListener.class.getName());

    @Inject
    private User user;
    @Inject
    private Repository repository;
    @Inject
    private DateUtils dateUtils;

    public void onAuthenticated(@Observes AuthenticatedEvent event) {
        log.fine("Successfull login for " + event.getUserPrincipal().getName() + " at " + dateUtils.now());
        user = repository.search(User.class, "username", event.getUserPrincipal().getName());
        user.setLastLogin(dateUtils.now().toString());
        repository.update(user);
    }

    public void onAuthenticationFailure(@Observes FailedAuthenticatedEvent event) {
        user = repository.search(User.class, "username",event.getUsername());
        user.setFailedLogins(user.getFailedLogins() + 1);
        log.fine("Falha de autenticação usuário " +  event.getUsername() + ", número de tentativas: [" + user.getFailedLogins() + "]");
        if (user.getFailedLogins() >= 3) {
            log.fine("Tentativas de login excedidas, bloqueando usuário " + user.getUsername());
            user.disable();
            user.setUserLockTime(String.valueOf(dateUtils.now()));
        }
        repository.update(user);
    }

    public void onLoggedOut(@Observes LoggedOutEvent event) {
        log.fine("Usuário " +  event.getUserPrincipal().getName() + " efetuou logout.");
    }
}