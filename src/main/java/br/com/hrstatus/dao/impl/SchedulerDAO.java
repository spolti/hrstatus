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

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.hrstatus.dao.SchedulerInterface;
import br.com.hrstatus.model.VerificationScheduler;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Repository
@Transactional
public class SchedulerDAO implements SchedulerInterface {

	Logger log =  Logger.getLogger(SchedulerDAO.class.getCanonicalName());
	
	private EntityManager entityManager;
	UserInfo userInfo = new UserInfo();
	
	@PersistenceContext(unitName = "pu-hr")
	protected final void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Session session() {
		return ((Session) entityManager.getDelegate());
	}
	
	public SchedulerDAO() {}
	
	
	public String saveScheduler (VerificationScheduler scheduler) {
		log.fine("[ " + userInfo.getLoggedUsername() + " ] saveScheduler (VerificationScheduler scheduler)");
		session().saveOrUpdate(scheduler);
		return "success";
	}
	
	public String saveSchedulerNotLogged (VerificationScheduler scheduler) {
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
	
	
}