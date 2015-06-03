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

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.hrstatus.dao.InstallProcessInterface;
import br.com.hrstatus.model.InstallationProcess;
import br.com.hrstatus.utils.UserInfo;

/* 
 * @author spolti
 */

@Repository
@Transactional
public class InstallProcessDAO implements InstallProcessInterface {

	Logger log = Logger.getLogger(InstallProcessDAO.class.getCanonicalName());

	private EntityManager entityManager;
	UserInfo userInfo = new UserInfo();

	public InstallProcessDAO() {
	}

	@PersistenceContext(unitName = "pu-hr")
	protected final void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Session session() {
		return ((Session) entityManager.getDelegate());
	}

	public boolean freshInstall() {

		log.fine("[ System ] invoking freshInstall()");

		Criteria freshInstall = session().createCriteria(InstallationProcess.class);
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("freshInstall"));
		freshInstall.setProjection(proList);

		boolean result = false;

		try {
			String temp = freshInstall.uniqueResult().toString();

			if (new Boolean(temp)) {
				result = true;
			} else if (!new Boolean(temp)) { 
				result = false;
			}
		} catch (java.lang.NullPointerException NPE) {
			result = true;
		}
		return result;
	}
	

	public void saveInstallationProcess (InstallationProcess ipi){
		log.fine("[ System ] invoking updateInstallationProcess()");
		session().save(ipi);
	}
	
}