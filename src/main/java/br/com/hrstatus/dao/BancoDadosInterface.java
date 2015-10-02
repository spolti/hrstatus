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

package br.com.hrstatus.dao;

import java.util.List;

import br.com.hrstatus.model.BancoDados;

/*
 * @author spolti
 */

public interface BancoDadosInterface {

	public int insert_dataBase(BancoDados dataBase);
	
	public List<BancoDados> listDataBases();
	
	public List<BancoDados> listDataBasesScheduler(String schedulerName);
	
	public List<BancoDados> listDataBaseByID(int id);
	
	public BancoDados getDataBaseByID(int id);
	
	public boolean deleteDataBase(BancoDados bancoDados);
	
	public void updateDataBase(BancoDados dataBase);
	
	public void updateDataBaseScheduler(BancoDados dataBase, String schedulerName);
	
	public int countMysql();
	
	public int countOracle();

	public int countPostgre();
	
	public int countSqlServer();
	
	public int countDB2();
	
	public int countAllDataBases();
	
	public int countDataBasesOK();
	
	public int countDataBasesNOK();
	
	public int countMySQLOK();
	
	public int countMySQLNOK();
	
	public int countOracleOK();
	
	public int countOracleNOK();
	
	public int countPostgreOK();
	
	public int countPostgreNOK();
	
	public int countSqlServerOK();
	
	public int countSqlServerNOK();
	
	public int countDB2OK();
	
	public int countDB2NOK();
	
	public List<BancoDados> getdataBasesOK();
	
	public List<BancoDados> getdataBasesNOK();
	
	public List<BancoDados> getVendorMysql();
	
	public List<BancoDados> getVendorOracle();
	
	public List<BancoDados> getVendorPostgre();

}