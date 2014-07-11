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

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.dao.Iteracoes;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Resource
public class ChartsController {
	
	private Result result;
	private Iteracoes iteracoesDAO;
	private BancoDadosInterface BancoDadosInterfaceDAO;

	UserInfo userInfo = new UserInfo();

	public ChartsController(Result result, Iteracoes iteracoesDAO, BancoDadosInterface BancoDadosInterfaceDAO) {
		this.result = result;
		this.iteracoesDAO = iteracoesDAO;
		this.BancoDadosInterfaceDAO = BancoDadosInterfaceDAO;

	}

	@Get("/charts/servers/consolidated")
	public void chartServidor(){
	// inserindo html title no result
			result.include("title", "Gráficos - Servidores");

			Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /charts/servers/consolidated");

			result.include("loggedUser", userInfo.getLoggedUsername());
			// ///////////////////////////////////////
			// Sending SERVERS % to plot SO's graph
			int linux = this.iteracoesDAO.countLinux();
			int windows = this.iteracoesDAO.countWindows();
			int unix = this.iteracoesDAO.countUnix();
			int other = this.iteracoesDAO.countOther();
			int total = this.iteracoesDAO.countAllServers();
			if (linux > 0 && windows > 0 && unix > 0) {
				result.include("linux", (linux * 100) / total);
				result.include("windows", (windows * 100) / total);
				result.include("unix", (unix * 100) / total);
				result.include("other", (other * 100) / total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] linux: " + linux);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Windows: " + windows);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Unix: " + unix);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Other: " + other);

			} else if (linux > 0) {
				result.include("linux", (linux * 100) / total);
				result.include("windows", 0);
				result.include("unix", 0);
				result.include("other", 0);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] linux: " + linux);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Windows: " + windows);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Unix: " + unix);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Other: " + other);

			} else if (windows > 0) {
				result.include("linux", 0);
				result.include("windows", (windows * 100) / total);
				result.include("unix", 0);
				result.include("other", 0);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] linux: " + linux);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Windows: " + windows);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Unix: " + unix);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Other: " + other);

			} else if (unix > 0) {
				result.include("linux", 0);
				result.include("windows", 0);
				result.include("unix", (unix * 100) / total);
				result.include("other", 0);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] linux: " + linux);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Windows: " + windows);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Unix: " + unix);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Other: " + other);

			} else if (other > 0) {
				result.include("linux", 0);
				result.include("windows", 0);
				result.include("unix", 0);
				result.include("other", (other * 100) / total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] linux: " + linux);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Windows: " + windows);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Unix: " + unix);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Other: " + other);
			}

			// Populating 2° graph (servers ok and not ok)
			result.include("serversOK", this.iteracoesDAO.countServersOK());
			result.include("serversNOK", this.iteracoesDAO.countServersNOK());

			// Ploting 3° graph.
			result.include("serversLinuxOK", this.iteracoesDAO.countLinuxOK());
			result.include("serversLinuxNOK", this.iteracoesDAO.countLinuxNOK());

			result.include("serversUnixOK", this.iteracoesDAO.countUnixOK());
			result.include("serversUnixNOK", this.iteracoesDAO.countUnixNOK());

			result.include("serversWindowsOK", this.iteracoesDAO.countWindowsOK());
			result.include("serversWindowsNOK", this.iteracoesDAO.countWindowsNOK());

			result.include("otherOK", this.iteracoesDAO.countOtherOK());
			result.include("otherNOK", this.iteracoesDAO.countOtherNOK());

			result.include("totalServer",total);
	}
	
	@Get("/charts/database/consolidated")
	public void chartDataBase(){
	// inserindo html title no result
			result.include("title", "Gráficos - Banco de Dados");

			Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /charts/servers/consolidated");

			result.include("loggedUser", userInfo.getLoggedUsername());
			// ///////////////////////////////////////
			// Sending DataBases % to plot SO's graph
			int mysql = this.BancoDadosInterfaceDAO.countMysql();
			int oracle = this.BancoDadosInterfaceDAO.countOracle();
			int postgre = this.BancoDadosInterfaceDAO.countPostgre();
			int total = this.BancoDadosInterfaceDAO.countAllDataBases();
			if (mysql > 0 && oracle > 0 && postgre > 0) {
				result.include("mysql", (mysql * 100) / total);
				result.include("oracle", (oracle * 100) / total);
				result.include("postgresql", (postgre * 100) / total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Mysql: " + mysql);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Oracle: " + oracle);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Postgre: " + postgre);

			} else if (mysql > 0) {
				result.include("mysql", (mysql * 100) / total);
				result.include("oracle", 0);
				result.include("postgresql", 0);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Mysql: " + mysql);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Oracle: " + oracle);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Postgre: " + postgre);

			} else if (oracle > 0) {
				result.include("mysql", 0);
				result.include("oracle", (oracle * 100) / total);
				result.include("postgresql", 0);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Mysql: " + mysql);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Oracle: " + oracle);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Postgre: " + postgre);

			} else if (postgre > 0) {
				result.include("mysql", 0);
				result.include("oracle", 0);
				result.include("postgresql", (postgre * 100) / total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Mysql: " + mysql);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Oracle: " + oracle);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Postgre: " + postgre);
			} 
			
			// Populating 2° graph (databases ok and not ok)
			int dbOK = this.BancoDadosInterfaceDAO.countDataBasesOK();
			int dbNOK = BancoDadosInterfaceDAO.countDataBasesNOK();
			result.include("databaseOK", dbOK);
			result.include("databaseNOK", dbNOK);

			// Ploting 3° graph.
			result.include("dbMysqlOK", this.BancoDadosInterfaceDAO.countMySQLOK());
			result.include("dbMysqlNOK", this.BancoDadosInterfaceDAO.countMySQLNOK());

			result.include("dbOracleOK", this.BancoDadosInterfaceDAO.countOracleOK());
			result.include("dbOracleNOK", this.BancoDadosInterfaceDAO.countOracleNOK());

			result.include("dbPostgreOK", this.BancoDadosInterfaceDAO.countPostgreOK());
			result.include("dbPostgreNOK", this.BancoDadosInterfaceDAO.countPostgreNOK());

			result.include("totalDB",total);
	}
}
