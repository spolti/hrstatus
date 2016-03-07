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

package br.com.hrstatus.action.databases;

import br.com.hrstatus.action.databases.helper.IllegalVendorException;
import br.com.hrstatus.model.BancoDados;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/*
 * @author spolti
 */
public class SQLStatementExecute {

    protected final Logger log = Logger.getLogger(getClass().getName());
    private DatabaseConnection getConnection = new DatabaseConnection();
    private Connection jdbcConnection;
    private Statement stm;
    private ResultSet rs;
    private String result = null;

    public String getDate(BancoDados dataBase) throws SQLException, ClassNotFoundException, IllegalVendorException {

        try {
            stm = conn(dataBase).createStatement();
            rs = stm.executeQuery(sqlQuery(dataBase));
            log.fine("Query executed: " + sqlQuery(dataBase));
            if(rs != null) {
                if (dataBase.getVendor().toUpperCase() == "MYSQL") {
                    while(rs.next()) {
                        result = rs.getString("date");
                    }
                } else {
                    while (rs.next()) {
                        result = rs.getString(1);
                    }
                }
            }

            return removeTimeStamp(result);

        } catch (SQLException e) {
            throw new SQLException("Failed to execute query {[" + dataBase.getVendor() + "] " + sqlQuery(dataBase)  + " }", e);

        } finally {
            if (jdbcConnection != null) {
                log.fine("Closing connection, statement and resultSet...");
                jdbcConnection.close();
                stm.close();
                rs.close();
            }
        }
    }

    /*
     * returns JDBC Connection
     */
    private Connection conn (BancoDados dataBase) throws ClassNotFoundException, SQLException, IllegalVendorException {
        jdbcConnection = getConnection.conn(dataBase);
        return jdbcConnection;
    }

    /*
     * returns the query which will be executed againts target database
     */
    private String sqlQuery (BancoDados dataBase) {
        return dataBase.getQueryDate();
    }

    /*
     * Remove timestamp from the date retrieved from database if it exists
     */
    private String removeTimeStamp (String date) {
        //Removing timestamp
        if (date.endsWith(".0")){
            date = date.replace(".","#");
            String dt_tmp[] = date.split("#");
            date = dt_tmp[0];
        }
        return date;
    }
}
