package br.com.hrstatus.action.databases.postgre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import br.com.hrstatus.utils.UserInfo;

public class ConnPostgreSQL {

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
				Logger.getLogger(ConnPostgreSQL.class).debug("[ " + userInfo.getLoggedUsername() + " ] " + status);
			} else {
				status = (false);
				Logger.getLogger(ConnPostgreSQL.class).debug("[ " + userInfo.getLoggedUsername() + " ] " + status);
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