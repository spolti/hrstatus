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

package br.com.hrstatus.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.hrstatus.action.databases.mysql.MySQL;
import br.com.hrstatus.action.databases.postgre.PostgreSQL;
import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.dao.LockIntrface;
import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.model.Lock;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.utils.DateUtils;
import br.com.hrstatus.utils.UserInfo;

import com.jcraft.jsch.JSchException;

/*
 * @author spolti
 */

@Resource
public class VerifyDataBase {

	private Result result;
	private LockIntrface lockDAO;
	private BancoDadosInterface dbDAO;
	private Configuration configurationDAO;
	private Validator validator;
	UserInfo userInfo = new UserInfo();

	public VerifyDataBase(Result result, LockIntrface lockDAO, BancoDadosInterface dbDAO,
			Configuration configurationDAO, Validator validator) {

		this.result = result;
		this.lockDAO = lockDAO;
		this.dbDAO = dbDAO;
		this.configurationDAO = configurationDAO;
		this.validator = validator;

	}

	@SuppressWarnings("static-access")
	@Get("/database/startDataBaseVerification/{value}")
	public void startDataBaseVerification(String value)
			throws InterruptedException, JSchException, SQLException, ClassNotFoundException {
		DateUtils dt = new DateUtils();
		Lock lockedResource = new Lock();
		Crypto encodePass = new Crypto();
		MySQL runMySQL = new MySQL();
		PostgreSQL runPSQL = new PostgreSQL();
		String dateSTR = null;

		// inserindo html title no result
		result.include("title", "Hr Status Home");
		String LoggedUsername = userInfo.getLoggedUsername();
		Logger.getLogger(getClass()).info(
				"[ " + userInfo.getLoggedUsername()
						+ " ] URI called: /database/startDataBaseVerification/"
						+ value);

		// Verifica se já tem alguma verificação ocorrendo...
		Logger.getLogger(getClass()).info(
				"[ " + userInfo.getLoggedUsername() + " ] Initializing a "
						+ value + " verification.");

		if (value.equals("fullDBVerification")) {
			lockedResource.setRecurso("fullDBVerification");
			lockedResource.setUsername(LoggedUsername);
			List<Lock> lockList = this.lockDAO
					.listLockedServices("fullDBVerification");
			if (lockList.size() != 0) {
				for (Lock lock : lockList) {
					Logger.getLogger(getClass()).info(
							"[ " + userInfo.getLoggedUsername()
									+ " ] O recurso " + value
									+ " está locado pelo usuário "
									+ lock.getUsername()
									+ ", aguarde o término da mesma.");
					result.include("class","activeBanco");
					result.include(
							"info",
							"O recurso " + value + " está locado pelo usuário "
									+ lock.getUsername()
									+ ", aguarde o término da mesma")
							.forwardTo(HomeController.class).home("");

				}
			} else {
				Logger.getLogger(getClass())
						.info("[ "
								+ userInfo.getLoggedUsername()
								+ " ] O recurso fullDBVerification não está locado, locando e proseguindo");
				// locar recurso.
				lockDAO.insertLock(lockedResource);

				List<BancoDados> listdb = this.dbDAO.listDataBases();
				for (BancoDados bancoDados : listdb) {
						bancoDados.setServerTime(dt.getTime());
						bancoDados.setLastCheck(bancoDados.getServerTime());

						// Decripting password
						try {
							bancoDados.setPass(String.valueOf(Crypto
									.decode(bancoDados.getPass())));
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
								dateSTR = runMySQL.getDate(bancoDados);
							}else if (bancoDados.getVendor().toUpperCase().equals("POSTGRESQL")){
								dateSTR = runPSQL.getDate(bancoDados);
							}else if (bancoDados.getVendor().toUpperCase().equals("SQLSERVER")){
								dateSTR = runMySQL.getDate(bancoDados);
							}else if (bancoDados.getVendor().toUpperCase().equals("ORACLE")){
								dateSTR = runMySQL.getDate(bancoDados);
							}
							Logger.getLogger(getClass()).debug(
									"[ " + userInfo.getLoggedUsername()
											+ " ] Hora obtida do servidor "
											+ bancoDados.getHostname() + ": "
											+ dateSTR);
							bancoDados.setClientTime(dateSTR);
							// Calculating time difference
							bancoDados.setDifference((dt.diffrenceTime(
									bancoDados.getServerTime(), dateSTR,
									"Dont Need this, Remove!!!")));

							if (bancoDados.getDifference() < 0) {
								bancoDados.setDifference(bancoDados
										.getDifference() * -1);
							}

							if (bancoDados.getDifference() <= this.configurationDAO
									.getDiffirenceSecs()) {
								bancoDados.setTrClass("success");
								bancoDados.setStatus("OK");
							} else {
								bancoDados.setTrClass("error");
								bancoDados.setStatus("não OK");
							}
							try {
								// Critpografando a senha
								bancoDados.setPass(encodePass.encode(bancoDados
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error(
										"[ " + userInfo.getLoggedUsername()
												+ " ] Error: ", e1);
							}
							this.dbDAO.updateDataBase(bancoDados);

						} catch (JSchException e) {
							bancoDados.setStatus(e + "");
							bancoDados.setTrClass("error");
							try {
								// Critpografando a senha
								bancoDados.setPass(encodePass.encode(bancoDados
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error(
										"[ " + userInfo.getLoggedUsername()
												+ " ] Error: ", e1);
							} 
							this.dbDAO.updateDataBase(bancoDados);
						} catch (IOException e) {
							bancoDados.setStatus(e + "");
							bancoDados.setTrClass("error");
							try {

								// Critpografando a senha
								bancoDados.setPass(encodePass.encode(bancoDados
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error(
										"[ " + userInfo.getLoggedUsername()
												+ " ] Error: ", e1);
							}

						}
					}

				result.include("class","activeBanco");
				result.include("bancoDados", listdb).forwardTo(HomeController.class)
				.home("");
			}
		}
		lockDAO.removeLock(lockedResource);
	}
	
	@Get("/database/showByStatus/{status}")
	public void showByStatus(String status) {
		// inserindo html title no result
		result.include("title", "Hr Status Home");

		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /database/showByStatus/" + status);

		if (status.equals("OK")) {
			List<BancoDados> listdb = this.dbDAO.getdataBasesOK();
			result.include("class","activeBanco");
			result.include("bancoDados", listdb).forwardTo(HomeController.class)
					.home("");
			
		} else if (!status.equals("OK")) {
			List<BancoDados> listdb = this.dbDAO.getdataBasesNOK();
			result.include("class","activeBanco");
			result.include("bancoDados", listdb).forwardTo(HomeController.class)
					.home("");
			
		} else {
			validator.onErrorUsePageOf(HomeController.class).home("");
		}
	}
	
}