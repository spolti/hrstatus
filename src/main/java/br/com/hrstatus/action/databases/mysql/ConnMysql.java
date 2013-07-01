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

package br.com.hrstatus.action.databases.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * @author spolti
 */

public class ConnMysql {

	public static boolean status = false;
	
	public ConnMysql() {}

	public static Connection getConexaoMySQL(String serverAddress, String username, String password) {

		Connection connection = null;
		try {
			String driver = "com.mysql.jdbc.Driver";
			Class.forName(driver);
//			String serverAddress = "localhost";
			String database = "mysql";
			String url = "jdbc:mysql://" + serverAddress + "/" + database;
//			String username = "root";
//			String password = "kta25m69";
			connection = DriverManager.getConnection(url, username, password);

			if (connection != null) {
				status = (true);
				System.out.println(status);
			} else {
				status = (false);
				System.out.println(status);
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