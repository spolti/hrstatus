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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Repository
@Transactional
public class BancoDadosDAO implements BancoDadosInterface {

	private EntityManager entityManager;
	UserInfo userInfo = new UserInfo();

	public BancoDadosDAO() {
	}

	@PersistenceContext(unitName = "pu-hr")
	protected final void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Session session() {
		return ((Session) entityManager.getDelegate());
	}

	public int insert_dataBase(BancoDados dataBase) {

		Logger.getLogger(getClass()).info(
				"[ " + userInfo.getLoggedUsername()
						+ " ] insert_dataBase -> Retrieving parameters");
		Logger.getLogger(getClass()).debug("Server: " + dataBase.getHostname());
		Logger.getLogger(getClass()).debug("IP: " + dataBase.getIp());
		Logger.getLogger(getClass()).debug("User: " + dataBase.getUser());
		Logger.getLogger(getClass()).debug("Pass: " + dataBase.getPass());
		Logger.getLogger(getClass()).debug("Port: " + dataBase.getPort());
		Logger.getLogger(getClass()).debug("VENDOR: " + dataBase.getVendor());
		Logger.getLogger(getClass()).debug("Query: " + dataBase.getQueryDate());

		try {

			Criteria hostname = session()
					.createCriteria(BancoDados.class)
					.add(Restrictions.eq("hostname",
							new String(dataBase.getHostname())))
					.setProjection(Projections.property("hostname"));

			if (hostname.uniqueResult() == null) {
				Logger.getLogger(getClass()).info(
						"[ " + userInfo.getLoggedUsername()
								+ " ] insert_dataBase -> Banco de dados "
								+ dataBase.getHostname() + " não encontrado");
				Logger.getLogger(getClass()).info(
						"[ " + userInfo.getLoggedUsername()
								+ " ] insert_dataBase -> Saving data");
				session().save(dataBase);
				return 0;
			} else {
				Logger.getLogger(getClass()).info(
						"[ " + userInfo.getLoggedUsername()
								+ " ] insert_dataBase -> Servidor "
								+ dataBase.getHostname()
								+ " já existe, dados não inseridos.");
				return 1;
			}

		} catch (Exception e) {
			Logger.getLogger(getClass()).error(
					"[ " + userInfo.getLoggedUsername()
							+ " ] insert_dataBase -> Erro: " + e);
			return 1;
		}

	}
	
	@SuppressWarnings("unchecked")
	public List<BancoDados> listDataBases() {
		Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] listDataBases() -> Select * executed.");
		return session().createCriteria(BancoDados.class).list();
	}
	
	public BancoDados getDataBaseByID(int id) {
		Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] getDataBaseByID -> DataBase ID selected: " + id);
		return (BancoDados) session().createCriteria(BancoDados.class)
				.add(Restrictions.eq("id", id)).uniqueResult();
	}
	
	public boolean deleteDataBase(BancoDados bancoDados) {

		
		try {
			session().refresh(bancoDados);
			session().delete(bancoDados);
			Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] deleteDataBaseByID -> Data Base " + bancoDados.getHostname()
					+ " deleted.");
			return true;
		} catch (Exception e) {
			Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] deleteDataBaseByID -> Data Base " + bancoDados.getHostname()
					+ "Delete Operation failed.");
			return false;
		}
	}
	
	public void updateDataBase(BancoDados dataBase){
		
		Logger.getLogger(getClass()).info(
				"[ " + userInfo.getLoggedUsername()
						+ " ] updateDataBase -> Retrieving parameters");
		Logger.getLogger(getClass()).debug("Server: " + dataBase.getHostname());
		Logger.getLogger(getClass()).debug("IP: " + dataBase.getIp());
		Logger.getLogger(getClass()).debug("User: " + dataBase.getUser());
		Logger.getLogger(getClass()).debug("Pass: " + dataBase.getPass());
		Logger.getLogger(getClass()).debug("Port: " + dataBase.getPort());
		Logger.getLogger(getClass()).debug("VENDOR: " + dataBase.getVendor());
		Logger.getLogger(getClass()).debug("Query: " + dataBase.getQueryDate());
		
		session().update(dataBase);
	}
	
}
