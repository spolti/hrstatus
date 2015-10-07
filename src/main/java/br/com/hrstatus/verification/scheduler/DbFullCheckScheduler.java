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

package br.com.hrstatus.verification.scheduler;

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
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import br.com.hrstatus.action.databases.db2.DB2;
import br.com.hrstatus.action.databases.mysql.MySQL;
import br.com.hrstatus.action.databases.oracle.Oracle;
import br.com.hrstatus.action.databases.postgre.PostgreSQL;
import br.com.hrstatus.action.databases.sqlserver.SqlServer;
import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.dao.LockIntrface;
import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.model.Lock;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.utils.date.DateUtils;

import com.jcraft.jsch.JSchException;

/*
 * @author spolti
 */

@Service
@Configurable
public class DbFullCheckScheduler {

	Logger log =  Logger.getLogger(DbFullCheckScheduler.class.getCanonicalName());
	
	@Autowired
	private LockIntrface lockDAO;
	@Autowired
	private BancoDadosInterface dbDAO;
	@Autowired
	private Configuration configurationDAO;

	private DateUtils dt = new DateUtils();
	private Lock lockedResource = new Lock();
	private Crypto encodePass = new Crypto();
	private MySQL runMySQL = new MySQL();
	private PostgreSQL runPSQL = new PostgreSQL();
	private SqlServer runSqlServer = new SqlServer();
	private Oracle runOracle = new Oracle();
	private DB2 runDB2 = new DB2();
	
	
	@SuppressWarnings("static-access")
	public void startFullDataBaseVerification(String schedulerName) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
		String dateSTR = null;

		log.info("[ " + schedulerName + " ] Initializing Full DB Verification");

		// Verifica se já tem alguma verificação ocorrendo...
		lockedResource.setRecurso("fullDBVerification");
		lockedResource.setUsername(schedulerName);
		List<Lock> lockList = this.lockDAO.listLockedServicesScheduler("fullDBVerification", schedulerName);
		
		if (lockList.size() != 0) {
			for (Lock lock : lockList) {
				log.info("[ " + schedulerName + " ] The resource fullDBVerification is locked by the user " + schedulerName);
				log.info("O recurso fullDBVerification está locado pelo usuário " + lock.getUsername() + ", aguarde o término da mesma");
			}
		} else {
			
			log.info("[ " + schedulerName + " ] O recurso fullDBVerification não está locado, locando e proseguindo");
			// locking resource
			lockDAO.insertLockScheduler(lockedResource, schedulerName);

			List<BancoDados> listdb = this.dbDAO.listDataBasesScheduler(schedulerName);
			for (BancoDados bancoDados : listdb) {
				bancoDados.setServerTime(dt.getTime());
				bancoDados.setLastCheck(bancoDados.getServerTime());

				// Decrypting password
			    bancoDados.setPass(String.valueOf(Crypto.decode(bancoDados.getPass())));

				try {

					if (bancoDados.getVendor().toUpperCase().equals("MYSQL")) {
						dateSTR = runMySQL.getDateMySQL(bancoDados, schedulerName);
					} else if (bancoDados.getVendor().toUpperCase().equals("POSTGRESQL")) {
						dateSTR = runPSQL.getDatePSQL(bancoDados, schedulerName);
					} else if (bancoDados.getVendor().toUpperCase().equals("SQLSERVER")) {
						dateSTR = runSqlServer.getDateSqlServer(bancoDados, schedulerName);
					} else if (bancoDados.getVendor().toUpperCase().equals("ORACLE")) {
						dateSTR = runOracle.getDateOracle(bancoDados, schedulerName);
					} else if (bancoDados.getVendor().toUpperCase().equals("DB2")) {
						dateSTR = runDB2.getDate(bancoDados, schedulerName);
					}
					log.fine("[ " + schedulerName + " ] Hora obtida do servidor " + bancoDados.getHostname() + ": " + dateSTR);
					bancoDados.setClientTime(dateSTR);
					// Calculating time difference
					bancoDados.setDifference((dt.diffrenceTime(bancoDados.getServerTime(), dateSTR,"Dont Need this, Remove!!!")));

					if (bancoDados.getDifference() < 0) {
						bancoDados.setDifference(bancoDados.getDifference() * -1);
					}

					if (bancoDados.getDifference() <= this.configurationDAO.getDiffirenceSecsScheduler(schedulerName)) {
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
						log.severe("[ " + schedulerName + " ] Error: " + e1);
					}
					this.dbDAO.updateDataBaseScheduler(bancoDados, schedulerName);

				} catch (JSchException e) {
					bancoDados.setStatus(e + "");
					bancoDados.setTrClass("error");
					try {
						// Encrypting the password
						bancoDados.setPass(encodePass.encode(bancoDados.getPass()));

					} catch (Exception e1) {
						
						log.severe("[ " + schedulerName + " ] Error: " + e1);
					}
					this.dbDAO.updateDataBaseScheduler(bancoDados, schedulerName);
				} catch (IOException e) {
					bancoDados.setStatus(e + "");
					bancoDados.setTrClass("error");
					try {

						// Encrypting the password
						bancoDados.setPass(encodePass.encode(bancoDados.getPass()));

					} catch (Exception e1) {
						
						log.severe("[ " + schedulerName + " ] Error: " + e1);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
					bancoDados.setStatus("Erro: " + e.getMessage());
					bancoDados.setTrClass("error");
					bancoDados.setPass(encodePass.encode(bancoDados.getPass()));
					this.dbDAO.updateDataBaseScheduler(bancoDados, schedulerName);
				}
			}

			lockDAO.removeLockScheduler(lockedResource, schedulerName);
		}
		
	}
}