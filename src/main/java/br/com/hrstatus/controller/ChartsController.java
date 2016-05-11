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

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.dao.InstallProcessInterface;
import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.utils.GetSystemInformation;
import br.com.hrstatus.utils.PropertiesLoaderImpl;
import br.com.hrstatus.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.logging.Logger;

/*
 * @author spolti
 */

@Resource
public class ChartsController {

    private Logger log = Logger.getLogger(ChartsController.class.getName());

    @Autowired
    private Result result;
    @Autowired
    private ServersInterface iteracoesDAO;
    @Autowired
    private BancoDadosInterface BancoDadosInterfaceDAO;
    @Autowired
    private InstallProcessInterface ipi;
    private UserInfo userInfo = new UserInfo();
    private GetSystemInformation getSys = new GetSystemInformation();

    @SuppressWarnings("static-access")
    @Get("/charts/servers/consolidated")
    public void chartServidor() {

        // Inserting HTML title in the result
        result.include("title", "Gráficos - Servers");

        //Sending information to "About" page
        final PropertiesLoaderImpl load = new PropertiesLoaderImpl();
        final String version = load.getValor("version");
        result.include("version", version);
        final List<String> info = getSys.SystemInformation();
        result.include("jvmName", info.get(2));
        result.include("jvmVendor", info.get(1));
        result.include("jvmVersion", info.get(0));
        result.include("osInfo", info.get(3));
        result.include("installDate", ipi.getInstallationDate());

        log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /charts/servers/consolidated");

        result.include("loggedUser", userInfo.getLoggedUsername());
        // ///////////////////////////////////////
        // Sending SERVERS % to plot SO's graph
        final int windows = this.iteracoesDAO.countWindows();
        final int unix = this.iteracoesDAO.countUnix();
        final int total = this.iteracoesDAO.countAllServers();
        result.include("windows", windows);
        result.include("unix", unix);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Windows: " + windows);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Unix: " + unix);

        // Populating 2° graph (servers ok and not ok)
        final int serverOK = this.iteracoesDAO.countServersOK();
        result.include("serversOK", serverOK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Servers OK: " + serverOK);
        final int serverNOK = this.iteracoesDAO.countServersNOK();
        result.include("serversNOK", serverNOK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Servers not OK: " + serverNOK);

        // Ploting 3° graph
        final int countUnixOK = this.iteracoesDAO.countUnixOK();
        result.include("serversUnixOK", countUnixOK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Servers Unix OK: " + countUnixOK);
        final int countUnixNOK = this.iteracoesDAO.countUnixNOK();
        result.include("serversUnixNOK", countUnixNOK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Servers Unix not OK: " + countUnixNOK);

        final int countWindowsOK = this.iteracoesDAO.countWindowsOK();
        result.include("serversWindowsOK", countWindowsOK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Servers Windows OK: " + countWindowsOK);
        final int countWindowsNOK = this.iteracoesDAO.countWindowsNOK();
        result.include("serversWindowsNOK", countWindowsNOK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Servers Windows not OK: " + countWindowsNOK);

        result.include("totalServer", total);
    }

    @SuppressWarnings("static-access")
    @Get("/charts/database/consolidated")
    public void chartDataBase() {

        // Inserting HTML title in the result
        result.include("title", "Gráficos - Databases");

        //Sending information to "About" page
        final PropertiesLoaderImpl load = new PropertiesLoaderImpl();
        final String version = load.getValor("version");
        result.include("version", version);
        final List<String> info = getSys.SystemInformation();
        result.include("jvmName", info.get(2));
        result.include("jvmVendor", info.get(1));
        result.include("jvmVersion", info.get(0));
        result.include("osInfo", info.get(3));
        result.include("installDate", ipi.getInstallationDate());

        log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /charts/database/consolidated");

        result.include("loggedUser", userInfo.getLoggedUsername());
        // ///////////////////////////////////////
        // Sending DataBases % to plot database's graph
        final int mysql = this.BancoDadosInterfaceDAO.countMysql();
        final int oracle = this.BancoDadosInterfaceDAO.countOracle();
        final int postgre = this.BancoDadosInterfaceDAO.countPostgre();
        final int sqlserver = this.BancoDadosInterfaceDAO.countSqlServer();
        final int db2 = this.BancoDadosInterfaceDAO.countDB2();
        final int total = this.BancoDadosInterfaceDAO.countAllDataBases();

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
        final int dbOK = this.BancoDadosInterfaceDAO.countDataBasesOK();
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases OK: " + dbOK);
        result.include("databaseOK", dbOK);
        final int dbNOK = BancoDadosInterfaceDAO.countDataBasesNOK();
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases not OK: " + dbNOK);
        result.include("databaseNOK", dbNOK);

        // Ploting 3° graph.
        final int countMySQLOK = this.BancoDadosInterfaceDAO.countMySQLOK();
        result.include("dbMysqlOK", countMySQLOK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases MySQL OK: " + countMySQLOK);
        final int countMySQLNOK = this.BancoDadosInterfaceDAO.countMySQLNOK();
        result.include("dbMysqlNOK", countMySQLNOK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases MySQL not OK: " + countMySQLNOK);

        final int countOracleOK = this.BancoDadosInterfaceDAO.countOracleOK();
        result.include("dbOracleOK", countOracleOK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases Oracle OK: " + countOracleOK);
        final int countOracleNOK = this.BancoDadosInterfaceDAO.countOracleNOK();
        result.include("dbOracleNOK", countOracleNOK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases Oracle not OK: " + countOracleNOK);

        final int countPostgreOK = this.BancoDadosInterfaceDAO.countPostgreOK();
        result.include("dbPostgreOK", countPostgreOK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases PostgreSQL OK: " + countPostgreOK);
        final int countPostgreNOK = this.BancoDadosInterfaceDAO.countPostgreNOK();
        result.include("dbPostgreNOK", countPostgreNOK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases PostgreSQL not OK: " + countPostgreNOK);

        final int countSqlServerOK = this.BancoDadosInterfaceDAO.countSqlServerOK();
        result.include("dbSqlServerOK", countSqlServerOK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases SqlServer OK: " + countSqlServerOK);
        final int countSqlServerNOK = this.BancoDadosInterfaceDAO.countSqlServerNOK();
        result.include("dbSqlServerNOK", countSqlServerNOK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases SqlServer not OK: " + countSqlServerNOK);

        final int countDB2OK = this.BancoDadosInterfaceDAO.countDB2OK();
        result.include("dbDB2OK", countDB2OK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases DB2 OK: " + countDB2OK);
        final int countDB2NOK = this.BancoDadosInterfaceDAO.countDB2NOK();
        result.include("dbDB2NOK", countDB2NOK);
        log.fine("[ " + userInfo.getLoggedUsername() + " ] Databases DB2 not OK: " + countDB2NOK);

        result.include("totalDB", total);
    }
}