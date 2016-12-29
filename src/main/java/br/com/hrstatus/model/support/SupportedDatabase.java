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

package br.com.hrstatus.model.support;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public enum SupportedDatabase {

    MYSQL("com.mysql.jdbc.Driver","jdbc:mysql://%s:%d/%s", 3306),
    ORACLE("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@%s:%d/%s", 1501),
    POSTGRESQL("org.postgresql.Driver", "jdbc:postgresql://%s:%d/%s", 5432),
    DB2("com.ibm.db2.jcc.DB2Driver", "jdbc:db2://%s:%d/%s", 50000),
    SQLSERVER("net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sqlserver://%s:%d/db_name;instance=%s", 1433),
    MONGODB("nil","mongodb://%s:%d/%s", 27017);

    private final String driver;
    private final String url;
    private int port;

    SupportedDatabase(String driver, String url, int port) {
        this.driver = driver;
        this.url = url;
        this.port = port;
    }

    public String DRIVER() {
        return driver;
    }

    public String URL() {
        return url;
    }

    public void definePort(int port) {
        this.port = port > 0 ? port : this.port;
    }
}