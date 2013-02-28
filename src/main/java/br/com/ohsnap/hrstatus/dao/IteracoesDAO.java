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

package br.com.ohsnap.hrstatus.dao;

/*
 * @author spolti
 */

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.ohsnap.hrstatus.model.Servidores;

@Repository
@Transactional
public class IteracoesDAO implements Iteracoes {

	private EntityManager entityManager;

	public IteracoesDAO() {

	}

	@PersistenceContext(unitName = "pu-hr")
	protected final void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Session session() {
		return ((Session) entityManager.getDelegate());
	}

	public int insert_server(Servidores server) {

		Logger.getLogger(getClass()).info(
				"insert_server -> Retrieving parameters");
		Logger.getLogger(getClass()).debug("Server: " + server.getHostname());
		Logger.getLogger(getClass()).debug("IP: " + server.getIp());
		Logger.getLogger(getClass()).debug("User: " + server.getUser());
		Logger.getLogger(getClass()).debug("Pass: " + server.getPass());
		Logger.getLogger(getClass()).debug("Port: " + server.getPort());
		Logger.getLogger(getClass()).debug("OS: " + server.getSO());

		try {

			Criteria hostname = session()
					.createCriteria(Servidores.class)
					.add(Restrictions.eq("hostname",
							new String(server.getHostname())))
					.setProjection(Projections.property("hostname"));

			if (hostname.uniqueResult() == null) {
				Logger.getLogger(getClass()).info(
						"insert_server -> Servidor " + server.getHostname()
								+ " não encontrado");
				Logger.getLogger(getClass()).info(
						"insert_server -> Saving data");
				session().save(server);
				return 0;
			} else {
				Logger.getLogger(getClass()).info(
						"insert_server -> Servidor " + server.getHostname()
								+ " já existe, dados não inseridos.");
				return 1;
			}

		} catch (Exception e) {
			Logger.getLogger(getClass()).error("insert_server -> Erro: " + e);
			return 1;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Servidores> getHostnames() {
		Logger.getLogger(getClass()).debug(
				"getHostnames -> Populating ComboBox updateServer.");

		try {

			Criteria criteriaHostname = session().createCriteria(
					Servidores.class);
			ProjectionList proList = Projections.projectionList();
			proList.add(Projections.property("id"));
			proList.add(Projections.property("hostname"));
			criteriaHostname.setProjection(proList);
			return criteriaHostname.list();

		} catch (Exception e) {
			Logger.getLogger(getClass()).error("Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}

	public Servidores getServerByID(int id) {
		Logger.getLogger(getClass()).debug(
				"getServerByID -> ID server selected: " + id);
		return (Servidores) session().createCriteria(Servidores.class)
				.add(Restrictions.eq("id", id)).uniqueResult();
	}

	public void updateServer(Servidores server) {

		Logger.getLogger(getClass()).debug("Parametros recebidos para update");
		Logger.getLogger(getClass()).debug("Server " + server.getHostname());
		Logger.getLogger(getClass()).debug("IP: " + server.getIp());
		Logger.getLogger(getClass()).debug("SO: " + server.getSO());
		Logger.getLogger(getClass()).debug("Port:" + server.getPort());
		Logger.getLogger(getClass()).debug("User: " + server.getUser());
		Logger.getLogger(getClass()).debug("Pass: " + server.getPass());
		Logger.getLogger(getClass()).debug("Difference: " + server.getDifference());		
		Logger.getLogger(getClass()).debug("ID: " + server.getId());

		session().update(server);

	}

	public boolean deleteServerByID(Servidores server) {

		Logger.getLogger(getClass()).info(
				"deleteServerByID -> Server " + server.getHostname()
						+ " deleted.");
		Logger.getLogger(getClass()).debug("Server " + server.getHostname());
		Logger.getLogger(getClass()).debug("IP: " + server.getIp());
		Logger.getLogger(getClass()).debug("SO: " + server.getSO());
		Logger.getLogger(getClass()).debug("Port:" + server.getPort());
		Logger.getLogger(getClass()).debug("User " + server.getUser());
		Logger.getLogger(getClass()).debug("Pass " + server.getPass());
		Logger.getLogger(getClass()).debug("ID " + server.getId());

		try {
			session().refresh(server);
			session().delete(server);
			return true;
		} catch (Exception e) {

			return false;
		}

	}

	public int countLinux() {
		Logger.getLogger(getClass()).debug(
				"countLinux() -> Count Linux Servers.");
		Criteria criteria = session().createCriteria(Servidores.class);
		criteria.add(Restrictions.eq("SO", "LINUX"));
		criteria.setProjection(Projections.rowCount());
		int count = ((Long) criteria.uniqueResult()).intValue();
		Logger.getLogger(getClass()).debug(
				"countLinux() -> Found " + count + " servers.");
		return count;

	}

	public int countWindows() {
		Logger.getLogger(getClass()).debug(
				"countWindows() -> Count Linux Servers.");
		Criteria criteria = session().createCriteria(Servidores.class);
		criteria.add(Restrictions.eq("SO", "WINDOWS"));
		criteria.setProjection(Projections.rowCount());
		int count = ((Long) criteria.uniqueResult()).intValue();
		Logger.getLogger(getClass()).debug(
				"countLinux() -> Found " + count + " servers.");
		return count;
	}

	public int countUnix() {
		Logger.getLogger(getClass()).debug(
				"countUnix() -> Count Linux Servers.");
		Criteria criteria = session().createCriteria(Servidores.class);
		criteria.add(Restrictions.eq("SO", "UNIX"));
		criteria.setProjection(Projections.rowCount());
		int count = ((Long) criteria.uniqueResult()).intValue();
		Logger.getLogger(getClass()).debug(
				"countLinux() -> Found " + count + " servers.");
		return count;
	}
	
	public int countOther(){
		Logger.getLogger(getClass()).debug(
				"countOther() -> Count Other SO Servers.");
		Criteria criteria = session().createCriteria(Servidores.class);
		criteria.add(Restrictions.ne("SO", "LINUX"));
		criteria.add(Restrictions.ne("SO", "Windows"));
		criteria.add(Restrictions.ne("SO", "Unix"));
		criteria.setProjection(Projections.rowCount());
		int count = ((Long) criteria.uniqueResult()).intValue();
		Logger.getLogger(getClass()).debug(
				"countOther() -> Found " + count + " servers.");
		return count;
		
	}

	public int countAllServers() {
		Logger.getLogger(getClass()).debug(
				"countLinux() -> Count Linux Servers.");
		Criteria criteria = session().createCriteria(Servidores.class);
		criteria.setProjection(Projections.rowCount());
		int count = ((Long) criteria.uniqueResult()).intValue();
		Logger.getLogger(getClass()).debug(
				"countAllServers() -> Found " + count + " servers.");
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Servidores> listServers() {
		Logger.getLogger(getClass()).debug(
				"listServers() -> Select * executed.");
		return session().createCriteria(Servidores.class).list();
	}

	@SuppressWarnings("unchecked")
	public List<Servidores> getServersOK() {
		Logger.getLogger(getClass()).debug(
				"getServersOK -> Getting Servers OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			return criteria.add(Restrictions.eq("status", "OK")).list();

		} catch (Exception e) {
			System.out.println(e);
			Logger.getLogger(getClass()).error("Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Servidores> getServersNOK() {
		Logger.getLogger(getClass()).debug(
				"getServersOK -> Getting Servers no OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.or(Restrictions.eq("trClass", "error"),
					Restrictions.eq("status", "NOK")));
			return criteria.list();

		} catch (Exception e) {
			System.out.println(e);
			Logger.getLogger(getClass()).error("Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}

	public int countServersOK() {
		Logger.getLogger(getClass()).debug(
				"countServersOK -> Counting Servers OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.eq("status", "OK"));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			return count;

		} catch (Exception e) {
			System.out.println(e);
			Logger.getLogger(getClass()).error("Erro: " + e);
			return 0;
		}
	}

	public int countServersNOK() {
		Logger.getLogger(getClass()).debug(
				"countServersNOK -> Counting Servers not OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.or(Restrictions.eq("trClass", "error"),
					Restrictions.eq("status", "NOK")));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			return count;

		} catch (Exception e) {
			System.out.println(e);
			Logger.getLogger(getClass()).error("Erro: " + e);
			return 0;
		}
	}

	public int countLinuxOK() {
		Logger.getLogger(getClass()).debug(
				"countLinuxOK -> Counting Linux Servers OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.and(Restrictions.eq("SO", "LINUX"),
					Restrictions.eq("status", "OK")));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			Logger.getLogger(getClass()).debug(
					"countLinuxOK -> " + count + " found.");
			return count;

		} catch (Exception e) {
			System.out.println(e);
			Logger.getLogger(getClass()).error("Erro: " + e);
			return 0;
		}
	}

	public int countLinuxNOK() {
		Logger.getLogger(getClass()).debug(
				"countLinuxNOK -> Counting Linux Servers not OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.and(Restrictions.eq("SO", "LINUX"),
					Restrictions.and(Restrictions.eq("trClass", "error"),
							Restrictions.eq("status", "NOK"))));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			Logger.getLogger(getClass()).debug(
					"countLinuxNOK -> " + count + " found.");
			return count;

		} catch (Exception e) {
			System.out.println(e);
			Logger.getLogger(getClass()).error("Erro: " + e);
			return 0;
		}
	}

	public int countUnixOK() {
		Logger.getLogger(getClass()).debug(
				"countUnixOK -> Counting Unix Servers OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.and(Restrictions.eq("SO", "UNIX"),
					Restrictions.eq("status", "OK")));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			Logger.getLogger(getClass()).debug(
					"countUnixOK -> " + count + " found.");
			return count;

		} catch (Exception e) {
			System.out.println(e);
			Logger.getLogger(getClass()).error("Erro: " + e);
			return 0;
		}
	}

	public int countUnixNOK() {
		Logger.getLogger(getClass()).debug(
				"countUnixNOK -> Counting Linux Servers not OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.and(Restrictions.eq("SO", "UNIX"),
					Restrictions.and(Restrictions.eq("trClass", "error"),
							Restrictions.eq("status", "NOK"))));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			Logger.getLogger(getClass()).debug(
					"countUnixNOK -> " + count + " found.");
			return count;

		} catch (Exception e) {
			System.out.println(e);
			Logger.getLogger(getClass()).error("Erro: " + e);
			return 0;
		}
	}

	public int countWindowsOK() {
		Logger.getLogger(getClass()).debug(
				"countWindowsOK -> Counting Windows Servers not OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.and(Restrictions.eq("SO", "WINDOWS"),
					Restrictions.eq("status", "OK")));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			Logger.getLogger(getClass()).debug(
					"countWindowsOK -> " + count + " found.");
			return count;

		} catch (Exception e) {
			System.out.println(e);
			Logger.getLogger(getClass()).error("Erro: " + e);
			return 0;
		}
	}

	public int countWindowsNOK() {
		Logger.getLogger(getClass()).debug(
				"countWindowsNOK -> Counting Windows Servers not OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.and(Restrictions.eq("SO", "WINDOWS"),
					Restrictions.and(Restrictions.eq("trClass", "error"),
							Restrictions.eq("status", "NOK"))));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			Logger.getLogger(getClass()).debug(
					"countWindowsNOK -> " + count + " found.");
			return count;

		} catch (Exception e) {
			Logger.getLogger(getClass()).error("Erro: " + e);
			return 0;
		}
	}

	public int countOtherOK() {
		Logger.getLogger(getClass()).debug(
				"countOtherOK -> Counting Others SO Servers not OK.");
		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.ne("SO", "LINUX"));
			criteria.add(Restrictions.ne("SO", "Windows"));
			criteria.add(Restrictions.ne("SO", "Unix"));
			criteria.add(Restrictions.eq("status", "OK"));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			Logger.getLogger(getClass()).debug(
					"countOtherOK -> " + count + " found.");
			return count;
		} catch (Exception e) {
			Logger.getLogger(getClass()).error("Erro: " + e);
			return 0;
		}
	}
	
	public int countOtherNOK() {
		Logger.getLogger(getClass()).debug(
				"countOtherNOK -> Counting Others SO Servers not OK.");
		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			criteria.add(Restrictions.ne("SO", "LINUX"));
			criteria.add(Restrictions.ne("SO", "Windows"));
			criteria.add(Restrictions.ne("SO", "Unix"));
			criteria.add(Restrictions.eq("trClass", "error"));
			criteria.add(Restrictions.eq("status", "NOK"));
			criteria.setProjection(Projections.rowCount());
			int count = ((Long) criteria.uniqueResult()).intValue();
			Logger.getLogger(getClass()).debug(
					"countOtherNOK -> " + count + " found.");
			return count;
		} catch (Exception e) {
			Logger.getLogger(getClass()).error("Erro: " + e);
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Servidores> getSOLinux() {
		Logger.getLogger(getClass()).debug(
				"getServersOK -> Getting Servers OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			return criteria.add(Restrictions.eq("SO", "LINUX")).list();

		} catch (Exception e) {
			System.out.println(e);
			Logger.getLogger(getClass()).error("Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Servidores> getSOWindows() {
		Logger.getLogger(getClass()).debug(
				"getServersOK -> Getting Servers OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			return criteria.add(Restrictions.eq("SO", "WINDOWS")).list();

		} catch (Exception e) {
			System.out.println(e);
			Logger.getLogger(getClass()).error("Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Servidores> getSOUnix() {
		Logger.getLogger(getClass()).debug(
				"getServersOK -> Getting Servers OK.");

		try {
			Criteria criteria = session().createCriteria(Servidores.class);
			return criteria.add(Restrictions.eq("SO", "UNIX")).list();

		} catch (Exception e) {
			System.out.println(e);
			Logger.getLogger(getClass()).error("Erro: " + e);
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
			return criteria.list();

		} catch (Exception e) {
			System.out.println(e);
			Logger.getLogger(getClass()).error("Erro: " + e);
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
			Logger.getLogger(getClass()).error("Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Servidores> getHostnamesWithLogDir() {
		Logger.getLogger(getClass()).debug(
				"getHostnames -> Showing servers that have log dir configurated.");

		try {

			Criteria getHostnamesWithLogDir = session().createCriteria(
					Servidores.class);
			ProjectionList proList = Projections.projectionList();
			proList.add(Projections.property("id"));
			proList.add(Projections.property("hostname"));
			proList.add(Projections.property("logDir"));
			getHostnamesWithLogDir.setProjection(proList);
			getHostnamesWithLogDir.add(Restrictions.ne("logDir","")); 
			return getHostnamesWithLogDir.list();

		} catch (Exception e) {
			Logger.getLogger(getClass()).error("Erro: " + e);
			return new ArrayList<Servidores>();
		}
	}
	
	public Servidores getServerByHostname(String hostname) {
		Logger.getLogger(getClass()).debug(
				"getServerByID -> hostname server selected: " + hostname);
		return (Servidores) session().createCriteria(Servidores.class)
				.add(Restrictions.eq("hostname", hostname)).uniqueResult();
	}
	
}