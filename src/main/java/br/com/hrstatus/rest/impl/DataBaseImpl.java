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

package br.com.hrstatus.rest.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.rest.DataBaseResource;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Component
public class DataBaseImpl extends SpringBeanAutowiringSupport implements DataBaseResource {

	Logger log = Logger.getLogger(DataBaseImpl.class.getCanonicalName());

	@Autowired(required = true)
	private BancoDadosInterface databaseDAO;

	private UserInfo userInfo = new UserInfo();
	private BancoDados database = new BancoDados();
	private Crypto encodePass = new Crypto();
	
	@PostConstruct
	public void init() {
		log.info("initializing Autowired Service.");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public List<BancoDados> bancodados() {
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Returning the database list.");
			return this.databaseDAO.listDataBases();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<BancoDados> bancodadosOK() {
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Returning the database ok list.");
			return this.databaseDAO.getdataBasesOK();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<BancoDados> bancodadosNOK() {
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Returning the database nok list.");
			return this.databaseDAO.getdataBasesNOK();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String removeDatabase(int id) {
		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Deleting database id: " + id);
			this.databaseDAO.deleteDataBase(this.databaseDAO.getDataBaseByID(id));
			return "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL, check logs for details";
		}
	}

	@SuppressWarnings("static-access")
	public String addDatabase(String ip, String hostname, String dbInstance,
			String username, String password, String dbVendor) {

		
		if (dbVendor.toUpperCase().equals("MYSQL")) {
			database.setQueryDate("SELECT NOW() AS date;");
		}
		if (dbVendor.toUpperCase().equals("ORACLE")) {
			database.setQueryDate("select sysdate from dual");
		}
		if (dbVendor.toUpperCase().equals("SQLSERVER")) {
			database.setQueryDate("SELECT GETDATE();");
		}
		if (dbVendor.toUpperCase().equals("POSTGRESQL")) {
			database.setQueryDate("SELECT now();");
		}
		if (dbVendor.toUpperCase().equals("DB2")) {
			database.setQueryDate("select VARCHAR_FORMAT(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MM:SS') FROM SYSIBM.SYSDUMMY1");
		}
		
		
		if (dbVendor.toUpperCase().equals("MYSQL")) {
			database.setPort(3306);
		}
		if (dbVendor.toUpperCase().equals("ORACLE")) {
			database.setPort(1521);
		}
		if (dbVendor.toUpperCase().equals("SQLSERVER")) {
			database.setPort(1433);
		}
		if (dbVendor.toUpperCase().equals("POSTGRESQL")) {
			database.setPort(5432);
		}
		if (dbVendor.toUpperCase().equals("DB2")) {
			database.setPort(50000);
		}
		
		database.setIp(ip);
		database.setHostname(hostname);
		database.setDb_name(dbInstance);
		database.setUser(username);
		database.setVendor(dbVendor.toUpperCase());
		database.setStatus("NOK");
		database.setTrClass("error");
		database.setInstance(dbInstance);
		
		try {
			// Encrypting the password
			database.setPass(encodePass.encode(password));
		} catch (Exception e) {
			log.severe("{REST} Error: " + e);
		}
		

		try {
			log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Adding database: " + hostname);
			if ( this.databaseDAO.insert_dataBase(database) == 0){
				log.info("{REST} DataBase " + database.getHostname() + " was sucessfully registred.");
				return "SUCCESS";
			} else {
				log.info("{REST} DataBase " + database.getHostname() + " was not registred because it already exists.");
				return "DataBase " + database.getHostname() + " was not registred because it already exists.";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL, check logs for details";
		}
	}

	@SuppressWarnings("static-access")
	public String addDatabaseSqlServer(String ip, String hostname,
			String dbInstance, String username, String password,
			String dbVendor, String dbName) {


		if (dbVendor.toUpperCase().equals("SQLSERVER")) {
			database.setQueryDate("SELECT GETDATE();");
			database.setPort(1433);
			database.setIp(ip);
			database.setHostname(hostname);
			database.setDb_name(dbName);
			database.setUser(username);
			database.setVendor(dbVendor.toUpperCase());
			database.setStatus("NOK");
			database.setTrClass("error");
			database.setInstance(dbInstance);
			
			try {
				// Encrypting the password
				database.setPass(encodePass.encode(password));
			} catch (Exception e) {
				log.severe("{REST} Error: " + e);
			}
			
			
			try {
				log.info(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> Adding database: " + hostname);
				if ( this.databaseDAO.insert_dataBase(database) == 0){
					log.info("{REST} DataBase " + database.getHostname() + " was sucessfully registred.");
					return "SUCCESS";
				} else {
					log.info("{REST} DataBase " + database.getHostname() + " was not registred because it already exists.");
					return "DataBase " + database.getHostname() + " was not registred because it already exists.";
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "FAIL, check logs for details";
			}
		} else {
			log.warning(" [ " + userInfo.getLoggedUsername() + " ]{REST} -> This method only allows SqlServer ");
			return "FAIL, check logs for details";
		}
	
	}

}