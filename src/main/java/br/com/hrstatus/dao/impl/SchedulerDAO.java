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

import br.com.hrstatus.dao.SchedulerInterface;
import br.com.hrstatus.model.VerificationScheduler;
import br.com.hrstatus.model.VerificationSchedulerHistory;
import br.com.hrstatus.utils.UserInfo;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Logger;

/*
 * @author spolti
 */

@Repository
@Transactional
public class SchedulerDAO implements SchedulerInterface {

    private Logger log = Logger.getLogger(SchedulerDAO.class.getName());

    private EntityManager entityManager;
    private UserInfo userInfo = new UserInfo();

    public SchedulerDAO() {
    }

    @PersistenceContext(unitName = "pu-hr")
    protected final void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private Session session() {
        return ((Session) entityManager.getDelegate());
    }

    public String saveScheduler(VerificationScheduler scheduler) {
        log.fine("[ " + userInfo.getLoggedUsername() + " ] saveScheduler (VerificationScheduler scheduler)");
        session().saveOrUpdate(scheduler);
        return "success";
    }

    public void updateScheduler(VerificationScheduler scheduler) {
        log.fine("[ " + userInfo.getLoggedUsername() + " ] updateScheduler (VerificationScheduler scheduler)");
        session().update(scheduler);
    }

    public String saveSchedulerNotLogged(VerificationScheduler scheduler) {
        log.fine("[ System ] saveSchedulerNotLogged (VerificationScheduler scheduler)");
        session().saveOrUpdate(scheduler);
        return "success";
    }

    @SuppressWarnings("unchecked")
    public List<VerificationScheduler> schedulers() {
        log.fine("[ " + userInfo.getLoggedUsername() + " ] List<VerificationScheduler> schedulers()");
        return session().createCriteria(VerificationScheduler.class).list();
    }

    public VerificationScheduler getSchedulerDefault(String schedulerName) {
        log.fine("[ " + schedulerName + " ] getSchedulerDefault()");
        return (VerificationScheduler) session().createCriteria(VerificationScheduler.class).add(Restrictions.eq("schedulerName", schedulerName)).uniqueResult();
    }

    public void saveHistory(VerificationSchedulerHistory schedulerHistory, String schedulerName) {
        log.fine("[ " + schedulerName + " ] saveHistory(VerificationSchedulerHistory schedulerHistory, String schedulerName)");
        session().save(schedulerHistory);
    }

    @SuppressWarnings("unchecked")
    public List<VerificationSchedulerHistory> getSchedulerHistory(String schedulerName) {
        log.fine("[ " + userInfo.getLoggedUsername() + " ] getSchedulerHistory (String schedulerName)");
        return session().createCriteria(VerificationSchedulerHistory.class).list();
    }

    public VerificationScheduler getSchedulerByID(int id) {
        log.fine("[ " + userInfo.getLoggedUsername() + " ] getSchedulerByID(int id)");
        return (VerificationScheduler) session().createCriteria(VerificationScheduler.class).add(Restrictions.eq("schedulerId", id)).uniqueResult();
    }

}