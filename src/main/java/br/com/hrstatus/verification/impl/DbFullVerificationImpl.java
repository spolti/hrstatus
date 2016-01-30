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

package br.com.hrstatus.verification.impl;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.hrstatus.action.databases.db2.DB2;
import br.com.hrstatus.action.databases.mysql.MySQL;
import br.com.hrstatus.action.databases.oracle.Oracle;
import br.com.hrstatus.action.databases.postgre.PostgreSQL;
import br.com.hrstatus.action.databases.sqlserver.SqlServer;
import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.utils.UserInfo;
import br.com.hrstatus.utils.date.DateUtils;

import com.jcraft.jsch.JSchException;

/*
 * @author spolti
 */

@Service
public class DbFullVerificationImpl {
	
	Logger log =  Logger.getLogger(DbFullVerificationImpl.class.getCanonicalName());
	

	@Autowired
	private BancoDadosInterface dbDAO;
	@Autowired
	private Configuration configurationDAO;
	
	private UserInfo userInfo = new UserInfo();
	private DateUtils dt = new DateUtils();
	private Crypto encodePass = new Crypto();
	private MySQL runMySQL = new MySQL();
	private PostgreSQL runPSQL = new PostgreSQL();
	private SqlServer runSqlServer = new SqlServer();
	private Oracle runOracle = new Oracle();
	private DB2 runDB2 = new DB2();
	
	@SuppressWarnings("static-access")
	public void performFullVerification() throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
		
		String dateSTR = null;

		List<BancoDados> listdb = this.dbDAO.listDataBases();
		
		for (BancoDados bancoDados : listdb) {
			bancoDados.setServerTime(dt.getTime());
			bancoDados.setLastCheck(bancoDados.getServerTime());

			// Decrypting password
		    bancoDados.setPass(String.valueOf(Crypto.decode(bancoDados.getPass())));

			try {

				if (bancoDados.getVendor().toUpperCase().equals("MYSQL")) {
					dateSTR = runMySQL.getDateMySQL(bancoDados, userInfo.getLoggedUsername());
				} else if (bancoDados.getVendor().toUpperCase().equals("POSTGRESQL")) {
					dateSTR = runPSQL.getDatePSQL(bancoDados, userInfo.getLoggedUsername());
				} else if (bancoDados.getVendor().toUpperCase().equals("SQLSERVER")) {
					dateSTR = runSqlServer.getDateSqlServer(bancoDados, userInfo.getLoggedUsername());
				} else if (bancoDados.getVendor().toUpperCase().equals("ORACLE")) {
					dateSTR = runOracle.getDateOracle(bancoDados, userInfo.getLoggedUsername());
				} else if (bancoDados.getVendor().toUpperCase().equals("DB2")) {
					dateSTR = runDB2.getDate(bancoDados, userInfo.getLoggedUsername());
				}
				log.fine("[ " + userInfo.getLoggedUsername() + " ] Hora obtida do servidor " + bancoDados.getHostname() + ": " + dateSTR);
				bancoDados.setClientTime(dateSTR);
				// Calculating time difference
				bancoDados.setDifference((dt.diffrenceTime(bancoDados.getServerTime(), dateSTR,"Dont Need this, Remove!!!")));

				if (bancoDados.getDifference() < 0) {
					bancoDados.setDifference(bancoDados.getDifference() * -1);
				}

				if (bancoDados.getDifference() <= this.configurationDAO.getDiffirenceSecs()) {
					bancoDados.setTrClass("success");
					bancoDados.setStatus("OK");
				} else {
					bancoDados.setTrClass("error");
					bancoDados.setStatus("não OK");
				}
				try {
					// Encrypting the password
					bancoDados.setPass(encodePass.encode(bancoDados.getPass()));

				} catch (Exception e1) {
					log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e1);
				}
				this.dbDAO.updateDataBase(bancoDados);

			} catch (JSchException e) {
				bancoDados.setStatus(e + "");
				bancoDados.setTrClass("error");
				try {
					// Encrypting the password
					bancoDados.setPass(encodePass.encode(bancoDados.getPass()));

				} catch (Exception e1) {
					
					log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e1);
				}
				this.dbDAO.updateDataBase(bancoDados);
			} catch (IOException e) {
				bancoDados.setStatus(e + "");
				bancoDados.setTrClass("error");
				try {

					// Encrypting the password
					bancoDados.setPass(encodePass.encode(bancoDados.getPass()));

				} catch (Exception e1) {
					
					log.severe("[ " + userInfo.getLoggedUsername()+ " ] Error: " + e1);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
				bancoDados.setStatus("Erro: " + e.getMessage());
				bancoDados.setTrClass("error");
				bancoDados.setPass(encodePass.encode(bancoDados.getPass()));
				this.dbDAO.updateDataBase(bancoDados);
			}
			
		}
		
	}
	
}