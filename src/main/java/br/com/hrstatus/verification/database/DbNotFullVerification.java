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

package br.com.hrstatus.verification.database;

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

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.hrstatus.action.databases.mysql.MySQL;
import br.com.hrstatus.action.databases.oracle.Oracle;
import br.com.hrstatus.action.databases.postgre.PostgreSQL;
import br.com.hrstatus.action.databases.sqlserver.SqlServer;
import br.com.hrstatus.controller.HomeController;
import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.dao.LockIntrface;
import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.model.Lock;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.utils.UserInfo;
import br.com.hrstatus.utils.date.DateUtils;

import com.jcraft.jsch.JSchException;

/*
 * @author spolti
 */

@Resource
public class DbNotFullVerification {

	Logger log =  Logger.getLogger(DbNotFullVerification.class.getCanonicalName());
	
	@Autowired
	private Result result;
	@Autowired
	private LockIntrface lockDAO;
	@Autowired
	private BancoDadosInterface dbDAO;
	@Autowired
	private Configuration configurationDAO;
	@Autowired
	private Validator validator;
	UserInfo userInfo = new UserInfo();
	DateUtils dt = new DateUtils();
	private Lock lockedResource = new Lock();
	private Crypto encodePass = new Crypto();
	private MySQL runMySQL = new MySQL();
	private PostgreSQL runPSQL = new PostgreSQL();
	private SqlServer runSqlServer = new SqlServer();
	private Oracle runOracle = new Oracle();
	
	@SuppressWarnings("static-access")
	@Get("/database/startDataBaseVerification/notFullDBVerification")
	public void startNotFullDataBaseVerification() throws ClassNotFoundException, SQLException{
		String dateSTR = null;

		// Inserting HTML title in the result
		result.include("title", "Hr Status Home");
		String LoggedUsername = userInfo.getLoggedUsername();
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI called: /database/startDataBaseVerification/notFullDBVerification");

		log.info("[ " + userInfo.getLoggedUsername() + " ] Initializing a notFullDBVerification verification.");
		
		lockedResource.setRecurso("notFullDBVerification");
		lockedResource.setUsername(LoggedUsername);
		List<Lock> lockList = this.lockDAO.listLockedServices("notFullDBVerification");
		if (lockList.size() != 0) {
			for (Lock lock : lockList) {
				log.info("[ " + userInfo.getLoggedUsername() + " ] The resource notFullDBVerification is locked by the user " + lock.getUsername());
				result.include("class", "activeBanco");
				result.include("info","O recurso notFullDBVerification está locado pelo usuário "+ lock.getUsername()
								+ ", aguarde o término da mesma").forwardTo(HomeController.class).home("");

			}
		} else {
			log.info("[ "+ userInfo.getLoggedUsername() + " ] The resource notFullDBVerification is not locked, locking and continuing");
			// locking resource
			lockDAO.insertLock(lockedResource);

			List<BancoDados> listdb = this.dbDAO.getdataBasesNOK();
			for (BancoDados bancoDados : listdb) {
				bancoDados.setServerTime(dt.getTime());
				bancoDados.setLastCheck(bancoDados.getServerTime());

				// Decrypting password
				try {
					bancoDados.setPass(String.valueOf(Crypto.decode(bancoDados.getPass())));
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (BadPaddingException e) {
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
				}

				try {

					if (bancoDados.getVendor().toUpperCase().equals("MYSQL")) {
						dateSTR = runMySQL.getDateMySQL(bancoDados);
					} else if (bancoDados.getVendor().toUpperCase().equals("POSTGRESQL")) {
						dateSTR = runPSQL.getDatePSQL(bancoDados);
					} else if (bancoDados.getVendor().toUpperCase().equals("SQLSERVER")) {
						dateSTR = runSqlServer.getDateSqlServer(bancoDados);
					} else if (bancoDados.getVendor().toUpperCase().equals("ORACLE")) {
						dateSTR = runOracle.getDateOracle(bancoDados);
					}
					log.fine("[ " + userInfo.getLoggedUsername() + " ] Time retrieved from the server " + bancoDados.getHostname() + ": " + dateSTR);
					bancoDados.setClientTime(dateSTR);
					// Calculating time difference
					bancoDados.setDifference((dt.diffrenceTime(bancoDados.getServerTime(), dateSTR,	"Dont Need this, Remove!!!")));

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
						log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e1);
					}

				}
			}

			result.include("class", "activeBanco");
			result.include("bancoDados", listdb).forwardTo(HomeController.class).home("");
		}
		lockDAO.removeLock(lockedResource);
	}
}