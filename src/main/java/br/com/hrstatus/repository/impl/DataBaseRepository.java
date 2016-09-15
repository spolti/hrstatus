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

package br.com.hrstatus.repository.impl;

import br.com.hrstatus.model.Role;
import br.com.hrstatus.model.Setup;
import br.com.hrstatus.model.User;
import br.com.hrstatus.repository.Repository;

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
public class DataBaseRepository implements Repository {

    @PersistenceContext(unitName = "hrstatus")
    protected EntityManager em;
    private Logger log = Logger.getLogger(DataBaseRepository.class.getName());

    /*
    * load all configurations
    */
    @Override
    public Setup loadConfiguration() {
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Setup> criteriaQuery = builder.createQuery(Setup.class);
        Root<Setup> setupRoot = criteriaQuery.from(Setup.class);
        criteriaQuery.select(setupRoot);
        Query query = em.createQuery(criteriaQuery);
        return (Setup) query.getSingleResult();
    }

    /*
    * Return the mail session
    */
    @Override
    public String mailJndi() {
        Query q = em.createQuery("SELECT e.mailJndi from Setup e");
        return String.valueOf(q.getSingleResult());
    }

    /*
    * Return the mail from
    */
    @Override
    public String mailFrom() {
        Query q = em.createQuery("SELECT e.mailFrom from Setup e");
        return String.valueOf(q.getSingleResult());
    }


    /*
    * Register the given user
    * @param Object Users
    */
    public void registerUser(User user) throws Exception{
        log.fine("Salvando usuário " + user.getNome());
        em.persist(user);
        em.flush();
    }

    /*
    * Delete the given user object
    * @param Users
    */
    public void delete(User user) {
        em.remove(user);
        em.flush();
    }

    /*
    * List the registered users
    * @returns list containing all users
    */
    public List<User> getUsers() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot);
        criteria.where(builder.notEqual(userRoot.get("username"), "root"));
        Query query = em.createQuery(criteria);
        return query.getResultList();
    }

    /*
    * Search the given user
    * @returns the User object if found
    */
    public User searchUser(String username) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot);
        criteria.where(builder.equal(userRoot.get("username"), username));
        Query query = em.createQuery(criteria);
        return (User) query.getSingleResult();
    }

    /*
    * Update the given user
    */
    public void update(User user) {
        em.merge(user);
        em.flush();
    }

    /*
    * Get the locked users
    */
    public List<User> getLockedUsers() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot);
        criteria.where(builder.equal(userRoot.get("enabled"), false));
        Query query = em.createQuery(criteria);
        return query.getResultList();
    }

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
    public List<String> getRoles(String username) throws Exception{
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