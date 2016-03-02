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

import br.com.hrstatus.action.databases.helper.DatabaseConnectionHelper;
import br.com.hrstatus.action.databases.helper.IllegalVendorException;
import br.com.hrstatus.model.BancoDados;

import java.sql.*;
import java.util.logging.Logger;

/**
 * Created by fspolti on 2/29/16./**
 * @author spolti
 */
public class DatabaseConnection {

    protected final Logger log = Logger.getLogger(getClass().getName());

    /*
     * Returns the database connection
     */
    public Connection conn(BancoDados database) throws IllegalVendorException, ClassNotFoundException, SQLException {

        DatabaseConnectionHelper connHelper = null;
        Connection connection;

        switch (database.getVendor().toUpperCase()) {

            case "MYSQL":
                log.info("Preparing MySQL connection...");

                //is the class valid?
                Class.forName(connHelper.MYSQL.DRIVER());
                try {
                    connection = DriverManager.getConnection(String.format(connHelper.MYSQL.URL(),database.getIp(), database.getPort(),database.getDb_name()),
                            database.getUser(),
                            database.getPass());
                } catch (SQLException e) {
                    throw new SQLException("Failed to create connection: " + e);
                }
                log.fine("Connection successfully created: " + connection);
                return connection;

            case "ORACLE":
                log.info("Preparing Oracle connection...");

                //is the class valid?
                Class.forName(connHelper.ORACLE.DRIVER());
                try {
                    connection = DriverManager.getConnection(String.format(connHelper.ORACLE.URL(),database.getIp(), database.getPort(),database.getDb_name()),
                            database.getUser(),
                            database.getPass());
                } catch (SQLException e) {
                    throw new SQLException("Failed to create connection: " + e);
                }
                log.fine("Connection successfully created: " + connection);
                return connection;

            case "POSTGRESQL":
                log.info("Preparing PostgreSQL connection...");

                //is the class valid?
                Class.forName(connHelper.POSTGRESQL.DRIVER());
                try {
                    connection = DriverManager.getConnection(String.format(connHelper.POSTGRESQL.URL(),database.getIp(), database.getPort(),database.getDb_name()),
                            database.getUser(),
                            database.getPass());
                } catch (SQLException e) {
                    throw new SQLException("Failed to create connection: " + e);
                }
                log.fine("Connection successfully created: " + connection);
                return connection;

            case "DB2":
                log.info("Preparing DB2 connection...");

                //is the class valid?
                Class.forName(connHelper.DB2.DRIVER());
                try {
                    connection = DriverManager.getConnection(String.format(connHelper.DB2.URL(),database.getIp(), database.getPort(),database.getDb_name()),
                            database.getUser(),
                            database.getPass());
                } catch (SQLException e) {
                    throw new SQLException("Failed to create connection: " + e);
                }
                log.fine("Connection successfully created: " + connection);
                return connection;

            case "SQLSERVER":
                log.info("Preparing SQLSERVER connection...");

                //is the class valid?
                Class.forName(connHelper.SQLSERVER.DRIVER());
                try {
                    connection = DriverManager.getConnection(String.format(connHelper.SQLSERVER.URL(),database.getIp(), database.getPort(),database.getInstance()),
                            database.getUser(),
                            database.getPass());
                } catch (SQLException e) {
                    throw new SQLException("Failed to create connection: " + e);
                }
                log.fine("Connection successfully created: " + connection);
                return connection;

            default:
                throw new IllegalVendorException("The database vendor " + database.getVendor() + " is not supported.");
        }
    }
}
