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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import br.com.hrstatus.utils.DateParser;

/*
 * @author spolti
 */

public class MySQL {

	   public Date getDate() throws SQLException {  
		   Connection conn = ConnMysql.getConexaoMySQL();
		   String sql = "SELECT NOW() AS date;";
		   
		   Statement stm = conn.createStatement();
		   ResultSet rs = stm.executeQuery(sql);
		   String dt_db = null;
		   
           if(rs != null) {  
               while(rs.next()) {  
            	   dt_db  = rs.getString("date");
               }
           }
           
           //Formatando data.
           DateParser dt_parser = new DateParser();
           //Removendo, caso exista o .0 do final
           if (dt_db.endsWith(".0")){
        	   dt_db = dt_db.replace(".","#");
        	   String dt_tmp[] = dt_db.split("#");
        	   dt_db = dt_tmp[0];
           }
           
		   stm.close();
		   conn.close();
		   
		   return dt_parser.parser(dt_db);
	   }
	   
	   public static void main (String args[]) throws SQLException{
		   
		   MySQL geta = new MySQL();
		   System.out.println(geta.getDate());
	   }
}
