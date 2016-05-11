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

package br.com.hrstatus.action.databases.helper;

/**
 * @author spolti
 *
 * This ENUM stores all default parameters for database connections
 * {jdbc Driver, jdbc URL connection, database default port}
 */
public enum DatabaseConnectionHelper {

    MYSQL("com.mysql.jdbc.Driver","jdbc:mysql://%s:%d/%s"),
    ORACLE("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@%s:%d/%s"),
    POSTGRESQL("org.postgresql.Driver", "jdbc:postgresql://%s:%d/%s"),
    DB2("com.ibm.db2.jcc.DB2Driver", "jdbc:db2://%s:%d/%s"),
    SQLSERVER("net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sqlserver://%s:%d/db_name;instance=%s");

    private final String driver;
    private final String url;

    DatabaseConnectionHelper(String driver, String url) {
        this.driver = driver;
        this.url = url;
    }

    public String DRIVER() {
        return driver;
    }

    public String URL() {
        return url;
    }

}