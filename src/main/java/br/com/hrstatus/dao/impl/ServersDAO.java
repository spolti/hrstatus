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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Repository
@Transactional
public class ServersDAO implements ServersInterface {

	Logger log =  Logger.getLogger(ServersDAO.class.getCanonicalName());
	
	private EntityManager entityManager;
	UserInfo userInfo = new UserInfo();

	public ServersDAO() {

	}

	@PersistenceContext(unitName = "pu-hr")
	protected final void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Session session() {
		return ((Session) entityManager.getDelegate());
	}

	public int insert_server(Servidores server) {

		log.info("[ " + userInfo.getLoggedUsername() + " ] insert_server -> Retrieving parameters");
		log.fine("Server: " + server.getHostname());
		log.fine("IP: " + server.getIp());
		log.fine("User: " + server.getUser());
		log.fine("Pass: " + server.getPass());
		log.fine("Port: " + server.getPort());
		log.fine("OS: " + server.getSO());

		try {

			Criteria hostname = session().createCriteria(Servidores.class).add(Restrictions.eq("hostname",
							new String(server.getHostname()))).setProjection(Projections.property("hostname"));

			if (hostname.uniqueResult() == null) {
				log.info("[ " + userInfo.getLoggedUsername() + " ] insert_server -> Servidor " + server.getHostname() + " não encontrado");
				log.info("[ " + userInfo.getLoggedUsername() + " ] insert_server -> Saving data");
				session().save(server);
				return 0;
			} else {
				log.info("[ " + userInfo.getLoggedUsername() + " ] insert_server -> Servidor " + server.getHostname() + " já existe, dados não inseridos.");
				return 1;
			}

		} catch (Exception e) {
			log.severe("[ " + userInfo.getLoggedUsername() + " ] insert_server -> Erro: " + e);
			return 1;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Servidores> getHostnames() {
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getHostnames -> Populating ComboBox updateServer.");

		try {

			Criteria criteriaHostname = session().createCriteria(Servidores.class);
			ProjectionList proList = Projections.projectionList();
			proList.add(Projections.property("id"));
			proList.add(Projections.property("hostname"));
			criteriaHostname.setProjection(proList);
			return criteriaHostname.list();

		} catch (Exception e) {
			log.severe("Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}

	public Servidores getServerByID(int id) {
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getServerByID -> ID server selected: " + id);
		Criteria criteria = session().createCriteria(Servidores.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (Servidores) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Servidores> listServerByID(int id) {
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getServerByID -> ID server selected: " + id);
		Criteria criteria = session().createCriteria(Servidores.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.add(Restrictions.eq("id", id)).list();
	}
	
	public void updateServer(Servidores server) {

		log.info("[ " + userInfo.getLoggedUsername() + " ] updateServer -> Retrieving parameters");
		log.finest("[ " + userInfo.getLoggedUsername() + " ] Parametros recebidos para update");
		log.finest("Server " + server.getHostname());
		log.finest("IP: " + server.getIp());
		log.finest("SO: " + server.getSO());
		log.finest("Port:" + server.getPort());
		log.finest("User: " + server.getUser());
		log.finest("Pass: " + server.getPass());
		log.finest("Difference: " + server.getDifference());		
		log.finest("ID: " + server.getId());

		session().update(server);
		
	}

	public boolean deleteServerByID(Servidores server) {

		log.info("[ " + userInfo.getLoggedUsername() + " ] deleteServerByID -> Server " + server.getHostname() + " deleted.");

		try {
			session().refresh(server);
			session().delete(server);
			return true;
		} catch (Exception e) {

			return false;
		}

	}

	public int countLinux() {
		Criteria criteria = session().createCriteria(Servidores.class);
		criteria.add(Restrictions.eq("SO", "LINUX"));
		criteria.setProjection(Projections.rowCount());
		int count = ((Long) criteria.uniqueResult()).intValue();
		log.fine("[ " + userInfo.getLoggedUsername() + " ] countLinux() -> Found " + count + " servers.");
		return count;

	}

	public int countWindows() {
		Criteria criteria = session().createCriteria(Servidores.class);
		criteria.add(Restrictions.eq("SO", "WINDOWS"));
		criteria.setProjection(Projections.rowCount());
		int count = ((Long) criteria.uniqueResult()).intValue();
		log.fine("[ " + userInfo.getLoggedUsername() + " ] countLinux() -> Found " + count + " servers.");
		return count;
	}

	public int countUnix() {
		Criteria criteria = session().createCriteria(Servidores.class);
		criteria.add(Restrictions.eq("SO", "UNIX"));
		criteria.setProjection(Projections.rowCount());
		int count = ((Long) criteria.uniqueResult()).intValue();
		log.fine("[ " + userInfo.getLoggedUsername() + " ] countLinux() -> Found " + count + " servers.");
		return count;
	}
	
	public int countOther(){
		Criteria criteria = session().createCriteria(Servidores.class);
		criteria.add(Restrictions.ne("SO", "LINUX"));
		criteria.add(Restrictions.ne("SO", "Windows"));
		criteria.add(Restrictions.ne("SO", "Unix"));
		criteria.setProjection(Projections.rowCount());
		int count = ((Long) criteria.uniqueResult()).intValue();
		log.fine("[ " + userInfo.getLoggedUsername() + " ] countOther() -> Found " + count + " servers.");
		return count;
		
	}

	public int countAllServers() {
		Criteria criteria = session().createCriteria(Servidores.class);
		criteria.setProjection(Projections.rowCount());
		int count = ((Long) criteria.uniqueResult()).intValue();
		log.fine("[ " + userInfo.getLoggedUsername() + " ] countAllServers() -> Found " + count + " servers.");
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Servidores> listServers() {
		log.fine("[ " + userInfo.getLoggedUsername() + " ] listServers() -> Select * from Servidores executed.");
		Criteria criteria = session().createCriteria(Servidores.class);
		criteria.addOrder(Order.asc("hostname"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Servidores> listServersVerActive() {
		log.fine("[ " + userInfo.getLoggedUsername() + " ] listServersVerActive() -> Select * executed.");
		Criteria criteria = session().createCriteria(Servidores.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.add(Restrictions.eq("verify", "SIM")).list();
	}

	@SuppressWarnings("unchecked")
	public List<Servidores> getServersOK() {

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return criteria.add(Restrictions.eq("status", "OK")).list();

		} catch (Exception e) {
			System.out.println(e);
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Servidores> getServersNOK() {

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.or(Restrictions.eq("trClass", "error"), Restrictions.eq("status", "NOK")));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return criteria.list();

		} catch (Exception e) {
			System.out.println(e);
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Servidores> getServersNOKVerActive() {

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.or(Restrictions.eq("trClass", "error"), Restrictions.eq("status", "NOK")));
			criteria.add(Restrictions.eq("verify", "SIM"));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);		
			return criteria.list();

		} catch (Exception e) {
			System.out.println(e);
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}


	public int countServersOK() {

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.eq("status", "OK"));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			return count;

		} catch (Exception e) {
			System.out.println(e);
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return 0;
		}
	}

	public int countServersNOK() {

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.or(Restrictions.eq("trClass", "error"), Restrictions.eq("status", "NOK")));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			return count;

		} catch (Exception e) {
			System.out.println(e);
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return 0;
		}
	}

	public int countLinuxOK() {

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.and(Restrictions.eq("SO", "LINUX"), Restrictions.eq("status", "OK")));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			log.fine("[ " + userInfo.getLoggedUsername() + " ] countLinuxOK -> " + count + " found.");
			return count;

		} catch (Exception e) {
			System.out.println(e);
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return 0;
		}
	}

	public int countLinuxNOK() {

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.and(Restrictions.eq("SO", "LINUX"),(Restrictions.eq("trClass", "error"))));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			log.fine("[ " + userInfo.getLoggedUsername() + " ] countLinuxNOK -> " + count + " found.");
			return count;

		} catch (Exception e) {
			System.out.println(e);
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return 0;
		}
	}

	public int countUnixOK() {

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.and(Restrictions.eq("SO", "UNIX"), Restrictions.eq("status", "OK")));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			log.fine("[ " + userInfo.getLoggedUsername() + " ] countUnixOK -> " + count + " found.");
			return count;

		} catch (Exception e) {
			System.out.println(e);
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return 0;
		}
	}

	public int countUnixNOK() {

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.and(Restrictions.eq("SO", "UNIX"),(Restrictions.eq("trClass", "error"))));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			log.fine("[ " + userInfo.getLoggedUsername() + " ] countUnixNOK -> " + count + " found.");
			return count;

		} catch (Exception e) {
			System.out.println(e);
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return 0;
		}
	}

	public int countWindowsOK() {
		log.fine("[ " + userInfo.getLoggedUsername() + " ] countWindowsOK -> Counting Windows Servers not OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.and(Restrictions.eq("SO", "WINDOWS"), Restrictions.eq("status", "OK")));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			log.fine("[ " + userInfo.getLoggedUsername() + " ] countWindowsOK -> " + count + " found.");
			return count;

		} catch (Exception e) {
			System.out.println(e);
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return 0;
		}
	}

	public int countWindowsNOK() {

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.and(Restrictions.eq("SO", "WINDOWS"),(Restrictions.eq("trClass", "error"))));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			log.fine("[ " + userInfo.getLoggedUsername() + " ] countWindowsNOK -> " + count + " found.");
			return count;

		} catch (Exception e) {
			log.severe("Erro: " + e);
			return 0;
		}
	}

	public int countOtherOK() {

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.ne("SO", "LINUX"));
			criteria.add(Restrictions.ne("SO", "Windows"));
			criteria.add(Restrictions.ne("SO", "Unix"));
			criteria.add(Restrictions.eq("status", "OK"));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			log.fine("[ " + userInfo.getLoggedUsername() + " ] countOtherOK -> " + count + " found.");
			return count;
		} catch (Exception e) {
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return 0;
		}
	}
	
	public int countOtherNOK() {

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.ne("SO", "LINUX"));
			criteria.add(Restrictions.ne("SO", "Windows"));
			criteria.add(Restrictions.ne("SO", "Unix"));
			criteria.add(Restrictions.eq("trClass", "error"));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			log.fine("[ " + userInfo.getLoggedUsername() + " ] countOtherNOK -> " + count + " found.");
			return count;
		} catch (Exception e) {
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Servidores> getSOLinux() {
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getServersOK -> Getting Servers OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return criteria.add(Restrictions.eq("SO", "LINUX")).list();

		} catch (Exception e) {
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Servidores> getSOWindows() {
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getServersOK -> Getting Servers OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return criteria.add(Restrictions.eq("SO", "WINDOWS")).list();

		} catch (Exception e) {
			System.out.println(e);
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Servidores> getSOUnix() {
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getServersOK -> Getting Servers OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return criteria.add(Restrictions.eq("SO", "UNIX")).list();

		} catch (Exception e) {
			System.out.println(e);
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Servidores> getSOOthers() {

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.ne("SO", "LINUX"));
			criteria.add(Restrictions.ne("SO", "Windows"));
			criteria.add(Restrictions.ne("SO", "Unix"));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return criteria.list();

		} catch (Exception e) {
			System.out.println(e);
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Servidores> getListOfSO() {

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			ProjectionList proList = Projections.projectionList();
			proList.add(Projections.distinct(Projections.property("SO")));
			criteria.setProjection(proList);

			return criteria.list();

		} catch (Exception e) {
			System.out.println(e);
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Servidores> getHostnamesWithLogDir() {
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getHostnames -> Showing servers that have log dir configurated.");

		try {

			Criteria getHostnamesWithLogDir = session().createCriteria(Servidores.class);
			getHostnamesWithLogDir.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			getHostnamesWithLogDir.add(Restrictions.ne("logDir","")); 
			return getHostnamesWithLogDir.list();

		} catch (Exception e) {
			log.severe("Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}
	
	public Servidores getServerByHostname(String hostname) {
		log.fine("[ " + userInfo.getLoggedUsername() + " ] getServerByID -> hostname server selected: " + hostname);
		return (Servidores) session().createCriteria(Servidores.class).add(Restrictions.eq("hostname", hostname)).uniqueResult();
	}
	
	public int countServerWithLog() {

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.not(Restrictions.eq("logDir","")));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			log.fine("[ " + userInfo.getLoggedUsername() + " ] countServerWithLog -> found: " + count + ".");
			return count;
		} catch (Exception e) {
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Erro: " + e);
			return 0;
		}
	}
}