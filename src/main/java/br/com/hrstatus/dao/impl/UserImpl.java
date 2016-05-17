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

package br.com.hrstatus.dao.impl;

import br.com.hrstatus.dao.UserInterface;
import br.com.hrstatus.model.Users;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class UserImpl implements UserInterface {

    @PersistenceContext(unitName = "hrstatus")
    protected EntityManager em;
    private Logger log = Logger.getLogger(UserImpl.class.getName());

    /*
    * Register the given user
    * @param Object Users
    */
    public void registerUser(Users user) {
        log.fine("Salvando usuário " + user.getNome());
        em.persist(user);
        em.flush();
    }

}
