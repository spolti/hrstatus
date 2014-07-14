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

package br.com.hrstatus.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.hrstatus.model.Lock;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Repository
@Transactional
public class LockDAO implements LockIntrface{

	private EntityManager entityManager;
	UserInfo userInfo = new UserInfo();

	public LockDAO() {}

	@PersistenceContext(unitName = "pu-hr")
	protected final void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Session session() {
		return ((Session) entityManager.getDelegate());
	}

	public void insertLock(Lock lock) {
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] Gerando lock para o recurso " + lock.getRecurso()
						+ " solicitado pelo usuário " + lock.getUsername());
		session().save(lock);
	}

	public void removeLock(Lock lock) {
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] Removendo lock para o recurso " + lock.getRecurso()
						+ " solicitado pelo usuário " + lock.getUsername());
		session().refresh(lock);
		session().delete(lock);
	}

	@SuppressWarnings("unchecked")
	public List<Lock> listLockedServices(String recurso){
		Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Verificando se há algum recurso locado.");
		Criteria criteria = session().createCriteria(Lock.class);
		criteria.add(Restrictions.eq("recurso", recurso));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Lock> ListAllLocks(){
		Logger.getLogger(getClass()).debug("ListAllLocks() -> listing all locks.");
		return session().createCriteria(Lock.class).list();	
	}
	
	public Lock getLockByID(int id) {
		Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] getLockByID -> Lock ID selected: " + id);
		return (Lock) session().createCriteria(Lock.class)
				.add(Restrictions.eq("id", id)).uniqueResult();
	}
}