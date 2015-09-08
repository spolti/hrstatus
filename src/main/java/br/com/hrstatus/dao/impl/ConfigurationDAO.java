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

import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.model.Configurations;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Repository
@Transactional
public class ConfigurationDAO implements Configuration {

	Logger log =  Logger.getLogger(ConfigurationDAO.class.getCanonicalName());
	
	private EntityManager entityManager;
	UserInfo userInfo = new UserInfo();
	
	public ConfigurationDAO() {	}

	@PersistenceContext(unitName = "pu-hr")
	protected final void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Session session() {
		return ((Session) entityManager.getDelegate());
	}

	public void updateConfig(Configurations config) {

		log.fine("[ " + userInfo.getLoggedUsername() + " ] updateConfig(Configurations config)");
		session().saveOrUpdate(config);
	}
	
	public void saveConfigNotLogged(Configurations config) {

		log.fine("[ System ] updateConfig(Configurations config)");
		session().save(config);
	}

	public Configurations getConfigs() {
		
		log.fine("Invoking getConfigs()");
		return (Configurations) session().createCriteria(Configurations.class).uniqueResult();
	}

	public String getMailSender() {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getMailSender()");
		
		Criteria mailFrom = session().createCriteria(Configurations.class);
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("mailFrom"));
		mailFrom.setProjection(proList);
		return (String) mailFrom.uniqueResult();
	}
	
	public String getMailSenderNotLogged() {
		
		log.fine("[ System ] getMailSender()");
		
		Criteria mailFrom = session().createCriteria(Configurations.class);
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("mailFrom"));
		mailFrom.setProjection(proList);
		return (String) mailFrom.uniqueResult();
	}
	
	public boolean sendNotification() {
		
		log.fine("Invoking sendNotification() database query,");
		
		Criteria sendNotification = session().createCriteria(Configurations.class);
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("sendNotification"));
		sendNotification.setProjection(proList);
		return (Boolean) sendNotification.uniqueResult();
	}

	public String getSubject() {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getSubject()");
		
		Criteria subject = session().createCriteria(Configurations.class);
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("subject"));
		subject.setProjection(proList);
		return (String) subject.uniqueResult();
	}

	public String getDests() {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getDests()");
		
		Criteria subject = session().createCriteria(Configurations.class);
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("dests"));
		subject.setProjection(proList);
		return (String) subject.uniqueResult();
	}

	public String getJndiMail() {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getJndiMail()");
		
		Criteria subject = session().createCriteria(Configurations.class);
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("jndiMail"));
		subject.setProjection(proList);
		return (String) subject.uniqueResult();
	}
	
	public String getJndiMailNotLogged() {
		
		log.fine("[ System ] getJndiMail()");
		
		Criteria subject = session().createCriteria(Configurations.class);
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("jndiMail"));
		subject.setProjection(proList);
		return (String) subject.uniqueResult();
	}

	public int getDiffirenceSecs() {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getDiffirenceSecs()");
		
		Criteria difference = session().createCriteria(Configurations.class);
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("difference"));
		difference.setProjection(proList);
		int value = (Integer) difference.uniqueResult();
		return value;
	}

	public String getNtpServerAddress() {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getNtpServerAddress()");
		
		Criteria ntpServer = session().createCriteria(Configurations.class);
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("ntpServer"));
		ntpServer.setProjection(proList);
		return (String) ntpServer.uniqueResult();
	}
}