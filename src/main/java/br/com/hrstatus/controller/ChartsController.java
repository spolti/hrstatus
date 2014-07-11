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
			result.include("linux", linux);
			result.include("windows", windows);
			result.include("unix", unix);
			result.include("other", other);	
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] linux: " + linux);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Windows: " + windows);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Unix: " + unix);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Other: " + other);


			// Populating 2° graph (servers ok and not ok)
			int serverOK = this.iteracoesDAO.countServersOK();
			result.include("serversOK",serverOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Servidores OK: " + serverOK);
			int serverNOK = this.iteracoesDAO.countServersNOK();
			result.include("serversNOK", serverNOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Servidores não OK: " + serverNOK);

			// Ploting 3° graph.
			int countLinuxOK = this.iteracoesDAO.countLinuxOK();
			result.include("serversLinuxOK", countLinuxOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Servidores Linux OK: " + countLinuxOK);
			int countLinuxNOK = this.iteracoesDAO.countLinuxNOK();
			result.include("serversLinuxNOK", countLinuxNOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Servidores Linux não OK: " + countLinuxNOK);

			int countUnixOK = this.iteracoesDAO.countUnixOK();
			result.include("serversUnixOK", countUnixOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Servidores Unix OK: " + countUnixOK);
			int countUnixNOK = this.iteracoesDAO.countUnixNOK();
			result.include("serversUnixNOK", countUnixNOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Servidores Unix não OK: " + countUnixNOK);

			int countWindowsOK = this.iteracoesDAO.countWindowsOK();
			result.include("serversWindowsOK", countWindowsOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Servidores Windows OK: " + countWindowsOK);
			int countWindowsNOK = this.iteracoesDAO.countWindowsNOK();
			result.include("serversWindowsNOK", countWindowsNOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Servidores Windows não OK: " + countWindowsNOK);

			int countOtherOK = this.iteracoesDAO.countOtherOK();
			result.include("otherOK", countOtherOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Outros Servidores OK: " + countOtherOK);
			int countOtherNOK = this.iteracoesDAO.countOtherNOK();
			result.include("otherNOK", countOtherNOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Outros Servidores não OK: " + countOtherNOK);

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

			result.include("mysql", (mysql * 100) / total);
			result.include("oracle", (oracle * 100) / total);
			result.include("postgresql", (postgre * 100) / total);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Mysql: " + mysql);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Oracle: " + oracle);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Postgre: " + postgre);

			// Populating 2° graph (databases ok and not ok)
			int dbOK = this.BancoDadosInterfaceDAO.countDataBasesOK();
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Banco de Dados OK: " + dbOK);
			result.include("databaseOK", dbOK);
			int dbNOK = BancoDadosInterfaceDAO.countDataBasesNOK();
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Banco de Dados Não OK: " + dbNOK);
			result.include("databaseNOK", dbNOK);

			// Ploting 3° graph.
			int countMySQLOK = this.BancoDadosInterfaceDAO.countMySQLOK();
			result.include("dbMysqlOK", countMySQLOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Banco de Dados MySQL OK: " + countMySQLOK);
			int countMySQLNOK = this.BancoDadosInterfaceDAO.countMySQLNOK();
			result.include("dbMysqlNOK", countMySQLNOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Banco de Dados MySQL Não OK: " + countMySQLNOK);

			int countOracleOK = this.BancoDadosInterfaceDAO.countOracleOK();
			result.include("dbOracleOK", countOracleOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Banco de Dados Oracle OK: " + countOracleOK);
			int countOracleNOK = this.BancoDadosInterfaceDAO.countOracleNOK();
			result.include("dbOracleNOK", countOracleNOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Banco de Dados Oracle Não OK: " + countOracleNOK);

			int countPostgreOK = this.BancoDadosInterfaceDAO.countPostgreOK();
			result.include("dbPostgreOK", countPostgreOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Banco de Dados PostgreSQL OK: " + countPostgreOK);
			int countPostgreNOK = this.BancoDadosInterfaceDAO.countPostgreNOK();
			result.include("dbPostgreNOK", countPostgreNOK);
			Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Banco de Dados PostgreSQL Não OK: " + countPostgreNOK);

			result.include("totalDB",total);
	}
}