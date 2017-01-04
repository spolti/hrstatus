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

import br.com.hrstatus.model.Setup;
import br.com.hrstatus.model.User;
import br.com.hrstatus.repository.Repository;
import br.com.hrstatus.utils.date.DateUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class DataBaseRepository implements Repository {

    private Logger log = Logger.getLogger(DataBaseRepository.class.getName());

    @PersistenceContext(unitName = "hrstatus")
    protected EntityManager em;

    @Inject
    private DateUtils dt;

    /*
    * Import, the initial configuration in the database if it is a fresh database.
    */
    public void initialImport() {
        // Default password is P@ssw0rd
        String sql1 = "insert into USERS (username, enabled, firstLogin, mail, nome, password, failedLogins) VALUES ('root', true, false,'changeme@example.com','Administrador', 'sD3fPKLnFKZUjnSV4qA/XoJOqsmDfNfxWcZ7kPtLc0I=', 0);";
        String sql2 = "insert into User_roles (user_username, roles) values ('root', 'ROLE_ADMIN');";
        String sql3 = "insert into SETUP (id, mailJndi, mailFrom, welcomeMessage, difference, ntpServer, sendNotification, subject, updateNtpIsActive, installationDate) values (1, 'java:jboss/mail/HrStatus','hrstatus@hrstatus.com.br','Bem vindo ao Servidor HrStatus',30, 'a.ntp.br', false, 'NO REPLY - Status Horario de Verao', false,  '" + dt.now() + "');";
        log.fine("Initial database data: " + sql1 + "\n" +
                sql2 + "\n" +
                sql3);

        Query checkQuery = em.createQuery("SELECT e.username from User e where e.username ='root'");
        if (checkQuery.getResultList().size() == 1) {
            log.info("Initial setup already performed, skipping.");
        } else {
            em.createNativeQuery(sql1).executeUpdate();
            em.createNativeQuery(sql2).executeUpdate();
            em.createNativeQuery(sql3).executeUpdate();
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

    @Override
    public String welcomeMessage() {
        Query q = em.createQuery("SELECT e.welcomeMessage from Setup e");
        return String.valueOf(q.getSingleResult());
    }

    @Override
    public LocalDateTime installationDate() {
        Query q = em.createQuery("SELECT e.installationDate from Setup e");
        return LocalDateTime.parse(String.valueOf(q.getSingleResult()));
    }

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
    * Delete the given object <T>
    * @param Users
    */
    public <T, Object> void delete(Object object) {
        em.remove(object);
        em.flush();
    }

    /*
    * List all persisted objects on the database of the given type
    * @param clazz
    * @returns List<T>
    */
    public <T, Clazz> List<T> list(Clazz clazz) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery((Class<T>) clazz);
        Root<T> userRoot = criteria.from((Class<T>) clazz);
        criteria.select(userRoot);
        if (User.class.equals(clazz)){
            criteria.where(builder.notEqual(userRoot.get("username"), "root"));
        }
        Query query = em.createQuery(criteria);
        return query.getResultList();
    }

    /*
    * Search objects based on a query parameter
    */
    public <T, Clazz> T search(Clazz clazz, String parameterName, Object parameterValue) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery((Class<T>) clazz);
        Root<T> userRoot = criteria.from((Class<T>) clazz);
        criteria.select(userRoot);
        criteria.where(builder.equal(userRoot.get(parameterName), parameterValue));
        Query query = em.createQuery(criteria);
        return (T) query.getSingleResult();
    }

    /*
    * Update the given object
    */
    public Object update(Object object) {
        try {
            em.merge(object);
            em.flush();
            return "success";
        } catch (Exception e) {
            if (e.getCause().getCause().toString().contains("ConstraintViolation")) {
                return e.getCause().getCause();
            }
            return e.getCause();
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


}