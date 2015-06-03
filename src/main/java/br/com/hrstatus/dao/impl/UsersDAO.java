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

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.model.PassExpire;
import br.com.hrstatus.model.Users;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Repository
@Transactional
public class UsersDAO implements UsersInterface {
	
	Logger log =  Logger.getLogger(UsersDAO.class.getCanonicalName());
	
	private EntityManager entityManager;
	private UserInfo userInfo = new UserInfo();

	public UsersDAO() {

	}

	@PersistenceContext(unitName = "pu-hr")
	protected final void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	private Session session() {
		return ((Session) entityManager.getDelegate());
	}
	
	public void saveORupdateUser(Users user){
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] saveORupdateUser(Users user)[" + user.getUsername() +"]");
		session().save(user);
	}
	
	public void saveORupdateUserNotLogged(Users user){
		
		log.fine("[ System ] saveORupdateUser(Users user)[" + user.getUsername() +"]");
		session().save(user);
	}
	
	@SuppressWarnings("unchecked")
	public List<Users> listUser(){
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] listUser()");
		Criteria criteria = session().createCriteria(Users.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	
	public boolean deleteUserByID(Users user) {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] deleteUserByID(Users user)[" + user.getUsername() +"]");
		
		try {
			
			session().refresh(user);
			session().delete(user);
			return true;
		} catch (Exception e) {

			return false;
		}
	}
	
	public Users getUserByID(String username) {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getUserByID(String username)[" + username +"]");
		return (Users) session().createCriteria(Users.class).add(Restrictions.eq("username", username)).uniqueResult();
	}
	
	public Users getUserByIDNotLogged(String username) {
		
		log.fine("[System ] getUserByID(String username)[" + username +"]");
		return (Users) session().createCriteria(Users.class).add(Restrictions.eq("username", username)).uniqueResult();
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateUser(Users user) {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] deleteUserByID(Users user)[" + user.getUsername() +"]");
		session().update(user);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateUserNotLogged(Users user) {
		
		log.fine("[ System ] deleteUserByID(Users user)[" + user.getUsername() +"]");
		session().update(user);
	}
	
	public String getPass(String username){
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getPass(String user)[" + username +"]");
		
		Criteria criteria = session().createCriteria(Users.class);
		criteria.add(Restrictions.eq("username", username));
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("password"));
		criteria.setProjection(proList);
		return criteria.uniqueResult().toString();
	}
	
	public int searchUser(String username){
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] searchUser(String username)[" + username +"]");
		Criteria criteria = session().createCriteria(Users.class);
		criteria.add(Restrictions.eq("username", username));
		criteria.setProjection(Projections.rowCount());
		int count = ((Long) criteria.uniqueResult()).intValue();
		return count;
	}
	
	public int searchUserNotLogged(String username) {
		
		log.fine("[ System ] searchUser(String username)[" + username +"]");
		Criteria criteria = session().createCriteria(Users.class);
		criteria.add(Restrictions.eq("username", username));
		criteria.setProjection(Projections.rowCount());
		int count = ((Long) criteria.uniqueResult()).intValue();
		return count;
	}
	
	public String getMail(String username) {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getMail(String username)[" + username +"]");
		Criteria criteria = session().createCriteria(Users.class);
		criteria.add(Restrictions.eq("username", username));
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("mail"));
		criteria.setProjection(proList);
		return criteria.uniqueResult().toString();
	}
	
	public String getMailNotLogged(String username) {
		
		log.fine("[ System ] getMail(String username)[" + username +"]");
		Criteria criteria = session().createCriteria(Users.class);
		criteria.add(Restrictions.eq("username", username));
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("mail"));
		criteria.setProjection(proList);
		return criteria.uniqueResult().toString();
	}
	
	public void setExpirePasswordTime(PassExpire passExpire) {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] setExpirePasswordTime((PassExpire passExpire)");
		session().save(passExpire);
	}
	
	public void setExpirePasswordTimeNotLogged(PassExpire passExpire) {
		
		log.fine("[ System ] setExpirePasswordTime((PassExpire passExpire)");
		session().save(passExpire);
	}
	
	public int searchUserChangePass(String username) {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] searchUserChangePass(String username)[" + username +"]");
		Criteria criteria = session().createCriteria(PassExpire.class);
		criteria.add(Restrictions.eq("username", username));
		criteria.setProjection(Projections.rowCount());
		int count = ((Long) criteria.uniqueResult()).intValue();
		return count;
	}
	
	public int searchUserChangePassNotLogged(String username) {
		
		log.fine("[ System ] searchUserChangePass(String username)[" + username +"]");
		Criteria criteria = session().createCriteria(PassExpire.class);
		criteria.add(Restrictions.eq("username", username));
		criteria.setProjection(Projections.rowCount());
		int count = ((Long) criteria.uniqueResult()).intValue();
		return count;
	}
	
	public Object getUniqUser(String username) {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getUniqUser(String username)[" + username +"]");
		Criteria criteria = session().createCriteria(PassExpire.class);
		criteria.add(Restrictions.eq("username", username));
		return criteria.uniqueResult();
	}
	
	public void delUserHasChangedPass(String username) {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] delUserHasChangedPass() -> Deleting the user "+username+" from temporary user table.");
		PassExpire passExpire = (PassExpire) session().load(PassExpire.class, username);
		session().refresh(passExpire);
		session().delete(passExpire);	
	}
	
	@SuppressWarnings("unchecked")
	public List<PassExpire> getExpireTime() {
		
		log.fine("Invoking getExpireTime()");
		Criteria criteria = session().createCriteria(PassExpire.class);
		return criteria.list();
	}
	
	public void delUserExpireTime(PassExpire passExpire) {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] delUserExpireTime() -> Deleting the user " + passExpire.getUsername() + " from the temporary table.");
		session().refresh(passExpire);
		session().delete(passExpire);
	
	}
	
	public void delUserExpireTimeNotLogged(PassExpire passExpire) {
		
		log.fine("[ System ] delUserExpireTime() -> Deleting the user " + passExpire.getUsername() + " from the temporary table.");
		session().refresh(passExpire);
		session().delete(passExpire);
	
	}
	
	public String getRole(String user) {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getUniqUser(String user)[" + user +"]");
		Criteria criteria = session().createCriteria(Users.class);
		criteria.add(Restrictions.eq("username", user));
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("authority"));
		criteria.setProjection(proList);
		return criteria.uniqueResult().toString();
	}
	
	public boolean getFirstLogin(String username){
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getFirstLogin(String username)[" + username +"]");
		Criteria criteria = session().createCriteria(Users.class);
		criteria.add(Restrictions.eq("username", username));
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("firstLogin"));
		criteria.setProjection(proList);
		return (Boolean) criteria.uniqueResult();
	}
	
	public void setLastLoginTime(String timeStamp) {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] setLastLoginTime()");
		session().save(timeStamp);
	}
	
	public void lastActivityTimeStamp(String lastActivityTimeStamp) {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] lastActivityTimeStamp()");
		session().save(lastActivityTimeStamp);
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getIds_access_server(String user) {
		
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getIds_access_server() -> Listing the id servers of the user " + user + " can access.");
		Criteria criteria = session().createCriteria(Users.class);
		criteria.add(Restrictions.eq("username", user));
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property("access_server"));
		return criteria.list();
	}
}