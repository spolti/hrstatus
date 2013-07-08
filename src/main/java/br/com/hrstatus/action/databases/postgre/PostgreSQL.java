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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.hrstatus.model.BancoDados;

/*
 * @author spolti
 */

public class PostgreSQL {
	
	public String getDate(BancoDados db) throws SQLException, ClassNotFoundException { 
		Connection conn = ConnPostgreSQL.getConexaoPSQL(db.getIp(), db.getInstance(), db.getUser(), db.getPass());
		Statement stm = conn.createStatement();
		ResultSet rs = stm.executeQuery("SELECT now();");
		String dt_db = null;
		
        if(rs != null) {  
            while(rs.next()) {  
         	   dt_db  = rs.getString(1);
            }
        }
		//Formatando data.
        //DateParser dt_parser = new DateParser();
        //Removendo, caso exista o timestamp
        if (dt_db.contains(".")){
     	   dt_db = dt_db.replace(".","#");
     	   String dt_tmp[] = dt_db.split("#");
     	   dt_db = dt_tmp[0];
        }
        
		return dt_db;
	}
}