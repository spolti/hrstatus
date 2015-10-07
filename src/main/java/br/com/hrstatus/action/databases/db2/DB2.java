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

package br.com.hrstatus.action.databases.db2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import br.com.hrstatus.model.BancoDados;

/*
 * @author spolti
 */

public class DB2 {

	static Logger log = Logger.getLogger(DB2.class.getCanonicalName());

	public String getDate(BancoDados dataBase, String loggedUser) throws SQLException,	ClassNotFoundException {

		Connection conn = ConnDB2.getConexaoDB2(dataBase.getIp(), dataBase.getPort(), dataBase.getUser(), dataBase.getPass(), dataBase.getInstance(), loggedUser);
		String sql = dataBase.getQueryDate();

		log.fine("DB2 Query: " + sql);
		
		Statement stm = conn.createStatement();
		ResultSet rs = stm.executeQuery(sql);
		String dt_db = null;
		
		if (rs != null) {
			while (rs.next()) {
				dt_db = rs.getString(1);
				
			}
		}

		stm.close();
		conn.close();

		return dt_db;
	}
}