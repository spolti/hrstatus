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

import br.com.hrstatus.dao.RolesInterface;
import br.com.hrstatus.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class RolesImpl implements RolesInterface {

    private Logger log = Logger.getLogger(RolesImpl.class.getName());

    @PersistenceContext(unitName = "hrstatus")
    protected EntityManager em;

    /*
    * Map user to target role
    * @param Object Roles
    */
    public void save(Role role) {
        em.persist(role);
        em.flush();
    }

    /*
    * Delete all roles for the given username
    * @param String username
    */
    public void delete(String username) {
        Query query = em.createNativeQuery("DELETE FROM ROLE WHERE username = '" + username + "';");
        query.executeUpdate();
        em.flush();
    }

    /*
    * Select all roles from the given user
    * @returns a String[] containing the roles
    */
    public List<String> getRoles(String username) {
        ArrayList<String> list = new ArrayList<>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Role> cq = cb.createQuery(Role.class);
        Root<Role> c = cq.from(Role.class);
        cq.select(c);
        cq.where(cb.equal(c.get("username"),username));

        Query query = em.createQuery(cq);
        List<Role> result = query.getResultList();

        for (Role tempRoles : result) {
            log.fine("Usuário [" + username +"] - [" + tempRoles.getRole() + "]");
            list.add(tempRoles.getRole());
        }

        return list;
    }
}