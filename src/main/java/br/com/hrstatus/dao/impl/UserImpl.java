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
import br.com.hrstatus.model.User;
import org.omg.PortableInterceptor.USER_EXCEPTION;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
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
    public void registerUser(User user) {
        log.fine("Salvando usuário " + user.getNome());
        em.persist(user);
        em.flush();
    }

    /*
    * Delete the given user object
    * @param Users
    */
    public void delete(User user){
        em.remove(user);
        em.flush();
    }

    /*
    * List the registered users
    * @returns list containing all users
    */
    public List<User> getUsers() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.multiselect(cq.from(User.class));
        return em.createQuery(cq).getResultList();
    }

    /*
    * Search the given user
    * @returns the User object if found
    */
    public User searchUser (String username) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot);
        criteria.where(builder.equal(userRoot.get("username"), username));
        Query query = em.createQuery(criteria);
        return (User) query.getSingleResult();
    }

}
