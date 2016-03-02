package br.com.hrstatus.action.databases;

import br.com.hrstatus.action.databases.helper.IllegalVendorException;
import br.com.hrstatus.model.BancoDados;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Created by fspolti on 3/1/16.
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
            log.fine("Closing connection, statement and resultSet...");
            jdbcConnection.close();
            stm.close();
            rs.close();
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
