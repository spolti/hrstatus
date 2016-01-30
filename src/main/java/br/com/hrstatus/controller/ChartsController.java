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

package br.com.hrstatus.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.dao.InstallProcessInterface;
import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.utils.GetSystemInformation;
import br.com.hrstatus.utils.PropertiesLoaderImpl;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Resource
public class ChartsController {

	Logger log = Logger.getLogger(ChartsController.class.getCanonicalName());

	@Autowired
	private Result result;
	@Autowired
	private ServersInterface iteracoesDAO;
	@Autowired
	private BancoDadosInterface BancoDadosInterfaceDAO;
	@Autowired
	private InstallProcessInterface ipi;
	UserInfo userInfo = new UserInfo();
	private GetSystemInformation getSys = new GetSystemInformation();

	@SuppressWarnings("static-access")
	@Get("/charts/servers/consolidated")
	public void chartServidor() {

		// Inserting HTML title in the result
		result.include("title", "Gráficos - Servers");

		//Sending information to "About" page
		PropertiesLoaderImpl load = new PropertiesLoaderImpl();
		String version = load.getValor("version");
		result.include("version", version);
		List<String> info = getSys.SystemInformation();
		result.include("jvmName", info.get(2));
		result.include("jvmVendor",info.get(1));
		result.include("jvmVersion",info.get(0));
		result.include("osInfo",info.get(3));
		result.include("installDate", ipi.getInstallationDate());
		
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /charts/servers/consolidated");

		result.include("loggedUser", userInfo.getLoggedUsername());
		// ///////////////////////////////////////
		// Sending SERVERS % to plot SO's graph
		int linux = this.iteracoesDAO.countLinux();
		int windows = this.iteracoesDAO.countWindows();
		int unix = this.iteracoesDAO.countUnix();
		int other = this.iteracoesDAO.countOther();
		int total = this.iteracoesDAO.countAllServers();
		result.include("linux", linux);
		result.include("windows", windows);
		result.include("unix", unix);
		result.include("other", other);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] linux: " + linux);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Windows: " + windows);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Unix: " + unix);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Other: " + other);

		// Populating 2° graph (servers ok and not ok)
		int serverOK = this.iteracoesDAO.countServersOK();
		result.include("serversOK", serverOK);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Servers OK: " + serverOK);
		int serverNOK = this.iteracoesDAO.countServersNOK();
		result.include("serversNOK", serverNOK);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Servers not OK: " + serverNOK);

		// Ploting 3° graph.
		int countLinuxOK = this.iteracoesDAO.countLinuxOK();
		result.include("serversLinuxOK", countLinuxOK);
		log.fine("[ " + userInfo.getLoggedUsername()  + " ] Servers Linux OK: " + countLinuxOK);
		int countLinuxNOK = this.iteracoesDAO.countLinuxNOK();
		result.include("serversLinuxNOK", countLinuxNOK);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Servers Linux not OK: " + countLinuxNOK);

		int countUnixOK = this.iteracoesDAO.countUnixOK();
		result.include("serversUnixOK", countUnixOK);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Servers Unix OK: " + countUnixOK);
		int countUnixNOK = this.iteracoesDAO.countUnixNOK();
		result.include("serversUnixNOK", countUnixNOK);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Servers Unix not OK: " + countUnixNOK);

		int countWindowsOK = this.iteracoesDAO.countWindowsOK();
		result.include("serversWindowsOK", countWindowsOK);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Servers Windows OK: " + countWindowsOK);
		int countWindowsNOK = this.iteracoesDAO.countWindowsNOK();
		result.include("serversWindowsNOK", countWindowsNOK);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Servers Windows not OK: " + countWindowsNOK);

		int countOtherOK = this.iteracoesDAO.countOtherOK();
		result.include("otherOK", countOtherOK);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Others Servers OK: " + countOtherOK);
		int countOtherNOK = this.iteracoesDAO.countOtherNOK();
		result.include("otherNOK", countOtherNOK);
		log.fine("[ " + userInfo.getLoggedUsername()  + " ] Other Servers not OK: " + countOtherNOK);

		result.include("totalServer", total);
	}

	@SuppressWarnings("static-access")
	@Get("/charts/database/consolidated")
	public void chartDataBase() {

		// Inserting HTML title in the result
		result.include("title", "Gráficos - Databases");

		//Sending information to "About" page
		PropertiesLoaderImpl load = new PropertiesLoaderImpl();
		String version = load.getValor("version");
		result.include("version", version);
		List<String> info = getSys.SystemInformation();
		result.include("jvmName", info.get(2));
		result.include("jvmVendor",info.get(1));
		result.include("jvmVersion",info.get(0));
		result.include("osInfo",info.get(3));
		result.include("installDate", ipi.getInstallationDate());
		
		log.info("[ " + userInfo.getLoggedUsername()  + " ] URI Called: /charts/database/consolidated");

		result.include("loggedUser", userInfo.getLoggedUsername());
		// ///////////////////////////////////////
		// Sending DataBases % to plot SO's graph
		int mysql = this.BancoDadosInterfaceDAO.countMysql();
		int oracle = this.BancoDadosInterfaceDAO.countOracle();
		int postgre = this.BancoDadosInterfaceDAO.countPostgre();
		int sqlserver = this.BancoDadosInterfaceDAO.countSqlServer();
		int db2 = this.BancoDadosInterfaceDAO.countDB2();
		int total = this.BancoDadosInterfaceDAO.countAllDataBases();
		
		if (total > 0) {
			result.include("mysql", (mysql * 100) / total);
			result.include("oracle", (oracle * 100) / total);
			result.include("postgresql", (postgre * 100) / total);
			result.include("sqlserver", (sqlserver * 100) / total);
			result.include("db2", (db2 * 100) / total);
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Mysql: " + mysql);
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Oracle: " + oracle);
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Postgre: " + postgre);
			log.fine("[ " + userInfo.getLoggedUsername() + " ] SqlServer: " + sqlserver);
			log.fine("[ " + userInfo.getLoggedUsername() + " ] DB2: " + db2);
		} else {
			result.include("error", "Não há banco de dados cadastrados no HrStatus");
		}

		// Populating 2° graph (databases ok and not ok)
		int dbOK = this.BancoDadosInterfaceDAO.countDataBasesOK();
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases OK: " + dbOK);
		result.include("databaseOK", dbOK);
		int dbNOK = BancoDadosInterfaceDAO.countDataBasesNOK();
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases not OK: " + dbNOK);
		result.include("databaseNOK", dbNOK);

		// Ploting 3° graph.
		int countMySQLOK = this.BancoDadosInterfaceDAO.countMySQLOK();
		result.include("dbMysqlOK", countMySQLOK);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases MySQL OK: " + countMySQLOK);
		int countMySQLNOK = this.BancoDadosInterfaceDAO.countMySQLNOK();
		result.include("dbMysqlNOK", countMySQLNOK);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases MySQL not OK: " + countMySQLNOK);

		int countOracleOK = this.BancoDadosInterfaceDAO.countOracleOK();
		result.include("dbOracleOK", countOracleOK);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases Oracle OK: " + countOracleOK);
		int countOracleNOK = this.BancoDadosInterfaceDAO.countOracleNOK();
		result.include("dbOracleNOK", countOracleNOK);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases Oracle not OK: " + countOracleNOK);

		int countPostgreOK = this.BancoDadosInterfaceDAO.countPostgreOK();
		result.include("dbPostgreOK", countPostgreOK);
		log.fine("[ " + userInfo.getLoggedUsername()  + " ] Databases PostgreSQL OK: " + countPostgreOK);
		int countPostgreNOK = this.BancoDadosInterfaceDAO.countPostgreNOK();
		result.include("dbPostgreNOK", countPostgreNOK);
		log.fine("[ " + userInfo.getLoggedUsername()  + " ] Databases PostgreSQL not OK: " + countPostgreNOK);
		
		int countSqlServerOK = this.BancoDadosInterfaceDAO.countSqlServerOK();
		result.include("dbSqlServerOK", countSqlServerOK);
		log.fine("[ " + userInfo.getLoggedUsername()  + " ] Databases SqlServer OK: " + countSqlServerOK);
		int countSqlServerNOK = this.BancoDadosInterfaceDAO.countSqlServerNOK();
		result.include("dbSqlServerNOK", countSqlServerNOK);
		log.fine("[ " + userInfo.getLoggedUsername()  + " ] Databases SqlServer not OK: " + countSqlServerNOK);
		
		int countDB2OK = this.BancoDadosInterfaceDAO.countDB2OK();
		result.include("dbDB2OK", countDB2OK);
		log.fine("[ " + userInfo.getLoggedUsername()  + " ] Databases DB2 OK: " + countDB2OK);
		int countDB2NOK = this.BancoDadosInterfaceDAO.countDB2NOK();
		result.include("dbDB2NOK", countDB2NOK);
		log.fine("[ " + userInfo.getLoggedUsername()  + " ] Databases DB2 not OK: " + countDB2NOK);

		result.include("totalDB", total);
	}
}