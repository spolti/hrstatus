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

import br.com.hrstatus.model.BancoDados;

import java.util.List;

/*
 * @author spolti
 */

public interface BancoDadosInterface {

    int insert_dataBase(BancoDados dataBase);

    List<BancoDados> listDataBases();

    List<BancoDados> listDataBasesScheduler(String schedulerName);

    List<BancoDados> listDataBaseByID(int id);

    BancoDados getDataBaseByID(int id);

    boolean deleteDataBase(BancoDados bancoDados);

    void updateDataBase(BancoDados dataBase);

    void updateDataBaseScheduler(BancoDados dataBase, String schedulerName);

    int countMysql();

    int countOracle();

    int countPostgre();

    int countSqlServer();

    int countDB2();

    int countAllDataBases();

    int countDataBasesOK();

    int countDataBasesNOK();

    int countMySQLOK();

    int countMySQLNOK();

    int countOracleOK();

    int countOracleNOK();

    int countPostgreOK();

    int countPostgreNOK();

    int countSqlServerOK();

    int countSqlServerNOK();

    int countDB2OK();

    int countDB2NOK();

    List<BancoDados> getdataBasesOK();

    List<BancoDados> getdataBasesNOK();

    List<BancoDados> getVendorMysql();

    List<BancoDados> getVendorOracle();

    List<BancoDados> getVendorPostgre();

}