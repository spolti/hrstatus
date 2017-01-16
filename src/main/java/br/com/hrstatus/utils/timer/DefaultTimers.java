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

package br.com.hrstatus.utils.timer;

import br.com.hrstatus.model.User;
import br.com.hrstatus.repository.Repository;
import br.com.hrstatus.utils.date.DateUtils;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Stateless
public class DefaultTimers {

    private Logger log = Logger.getLogger(DefaultTimers.class.getName());

    @Inject
    private Repository repository;
    @Inject
    private DateUtils dateUtils;

    /*
    * Este timer será executado a cada 30 minutos.
    * Se houver algum usuário bloqueado por tentativas excendentes de login,
    * O mesmo será desbloqueado.
    */
    @Schedule(hour = "*", minute = "*/15", persistent = false)
    private void unlockUser() {
        repository.getLockedUsers().stream().filter(user -> !user.getUserLockTime().equals("00") && LocalDateTime.parse(user.getUserLockTime())
                .plusMinutes(30).isBefore(dateUtils.now()))
            .forEach(user ->{
                log.info("Desbloqueando usuário " + user.getUsername());
                user.enable();
                repository.update(user);
            });
    }
}