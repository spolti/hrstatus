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

package br.com.hrstatus.action.databases.sqlserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

public class ConnSqlServer {

	static Logger log = Logger.getLogger(ConnSqlServer.class.getCanonicalName());
	public static boolean status = false;

	public static Connection getConexaoMySQL(String serverAddress, String username, String password, String instance) {

		UserInfo userInfo = new UserInfo();
		Connection connection = null;
		String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
		String url = "jdbc:sqlserver://" + serverAddress + ";databaseName=" + instance;
		
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			log.severe("Erro ao conectar no banco de dados: " + e.getMessage());
			e.printStackTrace();
		}

		/*
		 * Testa se a conexão foi obtida com sucesso
		*/
		if (connection != null) {
			status = (true);
			log.fine("[ " + userInfo.getLoggedUsername() + " ] SqlServer datbase connection status: " + status);
		} else {
			status = (false);
			log.severe("[ " + userInfo.getLoggedUsername() + " ] SqlServer datbase connection status: " + status);
		}
		return connection;
	}
}