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

import br.com.hrstatus.model.OperatingSystem;
import br.com.hrstatus.model.Setup;
import br.com.hrstatus.model.User;
import br.com.hrstatus.repository.Repository;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class DataBaseRepository implements Repository {

    private Logger log = Logger.getLogger(DataBaseRepository.class.getName());

    @PersistenceContext(unitName = "hrstatus")
    protected EntityManager em;

    /*
    * Import the initial configuration in the database if it is a fresh database.
    */
    public void initialImport() {
        String sql1 = "insert into USER (username, enabled, firstLogin, mail, nome, password, failedLogins) VALUES ('root',true,false,'changeme@example.com','Administrador', 'sD3fPKLnFKZUjnSV4qA/XoJOqsmDfNfxWcZ7kPtLc0I=',0);";
        String sql2 = "insert into User_roles (user_username, roles) values ('root', 'ROLE_ADMIN');";
        String sql3 = "insert into SETUP (id, mailJndi, mailFrom) values (1, 'java:jboss/mail/HrStatus','hrstatus@hrstatus.com.br');";
        log.fine("Initial database data: " + sql1 + "\n" +
                sql2 + "\n" +
                sql3);
        Query q1 = em.createNativeQuery(sql1);
        Query q2 = em.createNativeQuery(sql2);
        Query q3 = em.createNativeQuery(sql3);
        Query checkQuery = em.createQuery("SELECT e.username from User e where e.username ='root'");
        if (checkQuery.getResultList().size() == 1) {
            log.info("Initial setup already performed, skipping.");
        } else {
            q1.executeUpdate();
            q2.executeUpdate();
            q3.executeUpdate();
        }
    }

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

    /***************************************************************
    * Users management
    ****************************************************************/
    /*
    * Register the given Object
    * @param Object
    */
    public Object register(Object obj) {
        try {
            log.fine("Persistindo objeto: " + obj.toString());
            em.persist(obj);
            em.flush();
            return "success";
        } catch (Exception e) {
            //Returns com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
            if (e.getCause().getCause().toString().contains("ConstraintViolation")) {
                return e.getCause().getCause();
            }
            return e.getCause();
        }
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
    public String update(User user) {
        try {
            em.merge(user);
            em.flush();
            return "success";
        } catch (Exception e) {
            return e.getMessage();

        }
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

    /***************************************************************
    * Resources repository - Operating Systems
    ****************************************************************/
    /*
    * Save the give Resource (Operating System)
    */
    public void save(OperatingSystem object) {
        em.persist(object);
        em.flush();
    }

    /*
    * Save the give Resource (Operating System)
    */
    public List<OperatingSystem> load() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<OperatingSystem> criteria = builder.createQuery(OperatingSystem.class);
        Root<OperatingSystem> peratingSystemRoot = criteria.from(OperatingSystem.class);
        criteria.select(peratingSystemRoot);
        Query query = em.createQuery(criteria);
        return query.getResultList();
    }

}