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

package br.com.hrstatus.action.databases.postgre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

public class ConnPostgreSQL {

	static Logger log =  Logger.getLogger(ConnPostgreSQL.class.getCanonicalName());
	public static boolean status = false;
	public ConnPostgreSQL() {}

	public static Connection getConexaoPSQL(String serverAddress, String database, String username, String password) {

		UserInfo userInfo = new UserInfo();
		Connection connection = null;

		try {
			String driver = "org.postgresql.Driver";
			Class.forName(driver);
			connection = DriverManager.getConnection("jdbc:postgresql://"+serverAddress+":5432/"+database+"",username,password);
			//connection = DriverManager.getConnection(url, username, password);

			
			if (connection != null) {
				status = (true);
				log.fine("[ " + userInfo.getLoggedUsername() + " ] PostgreSQL datbase connection status: " + status);
			} else {
				status = (false);
				log.fine("[ " + userInfo.getLoggedUsername() + " ] PostgreSQL datbase connection status: " + status);
			}
			return connection;

		} catch (ClassNotFoundException e) {
			//System.out.println("O driver expecificado nao foi encontrado.");
			System.out.println(e.fillInStackTrace());
			System.out.println(e.getMessage());
			return null;
		} catch (SQLException e) {
			//System.out.println("Nao foi possivel conectar ao Banco de Dados." + e);
			System.out.println(e.fillInStackTrace());
			System.out.println(e.getMessage());
			return null;
		}
	}
}