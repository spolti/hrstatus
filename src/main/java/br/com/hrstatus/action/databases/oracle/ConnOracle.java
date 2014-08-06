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

package br.com.hrstatus.action.databases.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

public class ConnOracle {
	
	public static boolean status = false;
	
	public ConnOracle() {}

	public static Connection getConexaoOracle(String serverAddress, String username, String password, String instance) throws ClassNotFoundException, SQLException {

		UserInfo userInfo = new UserInfo();
		Connection connection = null;
			String driver = "oracle.jdbc.driver.OracleDriver";
			Class.forName(driver);
			String url = "jdbc:oracle:thin:@" + serverAddress + ":" + 1521 + "/" + instance;
			connection = DriverManager.getConnection(url, username, password);

			if (connection != null) {
				status = (true);
				Logger.getLogger(ConnOracle.class).debug("[ " + userInfo.getLoggedUsername() + " ] Oracle datbase connection status: " + status);
			} else {
				status = (false);
				Logger.getLogger(ConnOracle.class).debug("[ " + userInfo.getLoggedUsername() + " ] Oracle datbase connection status: " + status);
			}
			return connection;
	}
}