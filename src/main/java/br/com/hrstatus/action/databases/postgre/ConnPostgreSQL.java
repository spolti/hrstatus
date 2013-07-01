package br.com.hrstatus.action.databases.postgre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnPostgreSQL {

	public static boolean status = false;
	public ConnPostgreSQL() {}

	public static Connection getConexaoMySQL() {

		Connection connection = null;

		try {
			String driver = "org.firebirdsql.jdbc.FBDriver";
			Class.forName(driver);
			String serverAddress = "localhost";
			String database = "teste";
			String url = "jdbc:firebirdsql" + serverAddress + "/3050:" + database;
			String username = "root";
			String password = "5555";
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