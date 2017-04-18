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

    /**
     * All the queries should not be editable to avoid users to use this functionality to get
     * unauthorized information from database using custom queries.
     */
    MYSQL("com.mysql.jdbc.Driver","jdbc:mysql://%s:%d/%s", 3306, "SELECT NOW() AS date;"),
    ORACLE("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@%s:%d/%s", 1501, "select sysdate from dual"),
    POSTGRESQL("org.postgresql.Driver", "jdbc:postgresql://%s:%d/%s", 5432, "SELECT now();"),
    DB2("com.ibm.db2.jcc.DB2Driver", "jdbc:db2://%s:%d/%s", 50000, "select VARCHAR_FORMAT(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MM:SS') FROM SYSIBM.SYSDUMMY1"),
    SQLSERVER("net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sqlserver://%s:%d/db_name;instance=%s", 1433, "SELECT GETDATE();"),
    MONGODB("nil","mongodb://%s:%d/%s", 27017, "under development");

    private final String driver;
    private final String url;
    private int port;
    private final String query;

    SupportedDatabase(String driver, String url, int port, String query) {
        this.driver = driver;
        this.url = url;
        this.port = port;
        this.query = query;
    }

    /**
     * @return the JDBC Driver
     */
    public String DRIVER() {
        return driver;
    }

    /**
     * @return the Database connection url
     */
    public String URL() {
        return url;
    }

    /**
     * @param port Define a custom port
     */
    public void definePort(int port) {
        this.port = port > 0 ? port : this.port;
    }

    /**
     * @return the query to retrieve the timestamp from database. This query cannot be modified by the user.
     */
    public String QUERY () {
        return query;
    }

    @Override
    public String toString() {
        return "SupportedDatabase{" +
                "driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", port=" + port +
                ", query='" + query + '\'' +
                '}';
    }
}