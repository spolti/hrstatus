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

/*
 * @author spolti
 */

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.hrstatus.action.VerifySingleServer;
import br.com.hrstatus.action.linux.GetDateLinux;
import br.com.hrstatus.action.other.GetDateOther;
import br.com.hrstatus.action.unix.GetDateUnix;
import br.com.hrstatus.action.windows.GetDateWindows;
import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.dao.Iteracoes;
import br.com.hrstatus.dao.LockIntrface;
import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.model.Lock;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.model.Users;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.utils.DateUtils;
import br.com.hrstatus.utils.PropertiesLoaderImpl;
import br.com.hrstatus.utils.UserInfo;

import com.jcraft.jsch.JSchException;

@Resource
public class HomeController {

	private Result result;
	private Iteracoes iteracoesDAO;
	private Configuration configurationDAO;
	private Validator validator;
	private LockIntrface lockDAO;
	private UsersInterface userDAO;
	private VerifySingleServer runVerify;
	UserInfo userInfo = new UserInfo();

	public HomeController(Result result, Iteracoes iteracoesDAO,
			Configuration configurationDAO, Validator validator,
			LockIntrface lockDAO, UsersInterface userDAO, VerifySingleServer runVerify) {
		this.result = result;
		this.iteracoesDAO = iteracoesDAO;
		this.configurationDAO = configurationDAO;
		this.validator = validator;
		this.lockDAO = lockDAO;
		this.userDAO = userDAO;
		this.runVerify = runVerify;
	}

	@Get("/home")
	public void home(String verification) {

		// inserindo html title no result
		result.include("title", "Hr Status Home");

		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /home");

		result.include("loggedUser", userInfo.getLoggedUsername());
		//result.include("class","activeServer");
		// ///////////////////////////////////////
		
		//inserindo no banco timepstamp do login.
		DateUtils dt = new DateUtils();
		Users user = this.userDAO.getUserByID(userInfo.getLoggedUsername());
		String lastLoginTime = dt.getTime();
		user.setLastLogin(lastLoginTime);
		this.userDAO.updateUser(user);
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] Successful login at " + lastLoginTime);
		
		/////////////////////////////////////////////////////////
		//Verificando se é o primeiro login do usuário após troca de senha ou do cadastro
		//se for false não faz nade se for true redireciona para atualizar cadastro.
		if (this.userDAO.getFirstLogin(userInfo.getLoggedUsername())){
			Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] Primeiro login do usuário " + userInfo.getLoggedUsername() + ": "  + this.userDAO.getFirstLogin(userInfo.getLoggedUsername()));
			Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] Redirecionando o usuário para troca de senha.");
			result.forwardTo(UpdateController.class).findForUpdateUser(null, userInfo.getLoggedUsername(), "changePass");
		}else{
			Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] Primeiro login do usuário " + userInfo.getLoggedUsername() + ": "  + this.userDAO.getFirstLogin(userInfo.getLoggedUsername()));
		}
	}

	@SuppressWarnings("static-access")
	@Get("/navbar")
	public void navbar() {
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /navbar");
		PropertiesLoaderImpl load = new PropertiesLoaderImpl();
	    String version = load.getValor("version"); 
	    result.include("version",version);
	}

	@Get("/home/showByStatus/{status}")
	public void showByStatus(String status) {
		// inserindo html title no result
		result.include("title", "Hr Status Home");

		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /home/showByStatus/" + status);

		if (status.equals("OK")) {
			List<Servidores> list = this.iteracoesDAO.getServersOK();
			result.include("class","activeServer");
			result.include("server", list).forwardTo(HomeController.class)
					.home("");
			result.include("class","activeServer");

		} else if (!status.equals("OK")) {
			List<Servidores> list = this.iteracoesDAO.getServersNOK();
			result.include("class","activeServer");
			result.include("server", list).forwardTo(HomeController.class)
					.home("");
			
		} else {
			validator.onErrorUsePageOf(HomeController.class).home("");
		}
	}

	@SuppressWarnings("static-access")
	@Get("/home/startVerification/{value}")
	public void startVerification(String value) throws InterruptedException,
			JSchException {
		// inserindo html title no result
		result.include("title", "Hr Status Home");

		String LoggedUsername = userInfo.getLoggedUsername();

		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI called: /home/startVerification/" + value);

		Lock lockedResource = new Lock();

		// Verifica se já tem alguma verificação ocorrendo...

		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] Initializing a " + value + " verification.");

		DateUtils dt = new DateUtils();

		Crypto encodePass = new Crypto();

		if (value.equals("full")) {
			lockedResource.setRecurso("verificationFull");
			lockedResource.setUsername(LoggedUsername);
			List<Lock> lockList = this.lockDAO
					.listLockedServices("verificationFull");
			if (lockList.size() != 0) {
				for (Lock lock : lockList) {
					Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] O recurso verificationFull está locado pelo usuário "
									+ lock.getUsername()
									+ ", aguarde o término da mesma");
					result.include("class","activeServer");
					result.include("info",
							"O recurso verificationFull está locado pelo usuário "
									+ lock.getUsername()
									+ ", aguarde o término da mesma").forwardTo(HomeController.class).home("");

				}
			} else {
				Logger.getLogger(getClass())
						.info("[ " + userInfo.getLoggedUsername() + " ] O recurso verificationFull não está locado, locando e proseguindo");
				// locar recurso.
				lockDAO.insertLock(lockedResource);

				List<Servidores> list = this.iteracoesDAO.listServers();
				for (Servidores servidores : list) {
					// if Linux
					if (servidores.getSO().equals("LINUX")) {
						servidores.setServerTime(dt.getTime());
						servidores.setLastCheck(servidores.getServerTime());
						// Decripting password
						try {
							servidores.setPass(String.valueOf(Crypto
									.decode(servidores.getPass())));
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
							String dateSTR = GetDateLinux.exec(
									servidores.getUser(), servidores.getIp(),
									servidores.getPass(), servidores.getPort());
							Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Hora obtida do servidor " + servidores.getHostname() + ": " + dateSTR);
							servidores.setClientTime(dateSTR);
							// Calculating time difference
							servidores.setDifference(dt.diffrenceTime(
									servidores.getServerTime(), dateSTR,
									"LINUX"));
							
							if (servidores.getDifference() < 0){
								servidores.setDifference(servidores.getDifference() * -1);
							}
								
							if (servidores.getDifference() <= this.configurationDAO
									.getDiffirenceSecs()) {
								servidores.setTrClass("success");
								servidores.setStatus("OK");
							} else {
								servidores.setTrClass("error");
								servidores.setStatus("não OK");
							}
							try {
								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error("[ " + userInfo.getLoggedUsername() + " ] Error: ",
										e1);
							}
							this.iteracoesDAO.updateServer(servidores);

						} catch (JSchException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {
								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error("[ " + userInfo.getLoggedUsername() + " ] Error: ",
										e1);
							}
							this.iteracoesDAO.updateServer(servidores);
						} catch (IOException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error("[ " + userInfo.getLoggedUsername() + " ] Error: ",
										e1);
							}
							this.iteracoesDAO.updateServer(servidores);
						}

					}

					if (servidores.getSO().equals("UNIX")) {
						servidores.setServerTime(dt.getTime());
						servidores.setLastCheck(servidores.getServerTime());
						// Decripting password
						try {
							servidores.setPass(String.valueOf(Crypto
									.decode(servidores.getPass())));
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
							String dateSTR = GetDateUnix.exec(
									servidores.getUser(), servidores.getIp(),
									servidores.getPass(), servidores.getPort());
							Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Hora obtida do servidor " + servidores.getHostname() + ": " + dateSTR);
							servidores.setClientTime(dateSTR);
							// Calculating time difference
							servidores.setDifference(dt.diffrenceTime(
									servidores.getServerTime(), dateSTR,
									"UNIX"));
							
							if (servidores.getDifference() < 0){
								servidores.setDifference(servidores.getDifference() * -1);
							}
							if (servidores.getDifference() <= this.configurationDAO
									.getDiffirenceSecs()) {
								servidores.setTrClass("success");
								servidores.setStatus("OK");
							} else {
								servidores.setTrClass("error");
								servidores.setStatus("não OK");
							}
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e) {
								Logger.getLogger(getClass())
										.error("[ " + userInfo.getLoggedUsername() + " ] Error: ", e);
							}
							this.iteracoesDAO.updateServer(servidores);
						} catch (JSchException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error("[ " + userInfo.getLoggedUsername() + " ] Error: ",
										e1);
							}
							this.iteracoesDAO.updateServer(servidores);
						} catch (IOException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error("[ " + userInfo.getLoggedUsername() + " ] Error: ",
										e1);
							}
							this.iteracoesDAO.updateServer(servidores);
						}

					}

					// if Windows
					if (servidores.getSO().equals("WINDOWS")) {
						servidores.setServerTime(dt.getTime());
						servidores.setLastCheck(servidores.getServerTime());
						try {
							String dateSTR = GetDateWindows.Exec(servidores
									.getIp());
							Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Hora obtida do servidor " + servidores.getHostname() + ": " + dateSTR);
							servidores.setClientTime(dateSTR);
							// Calculating time difference
							servidores.setDifference(dt.diffrenceTime(
									servidores.getServerTime(), dateSTR,
									"WINDOWS"));
							
							if (servidores.getDifference() < 0){
								servidores.setDifference(servidores.getDifference() * -1);
							}
							
							if (servidores.getDifference() <= this.configurationDAO
									.getDiffirenceSecs()) {
								servidores.setTrClass("success");
								servidores.setStatus("OK");
							} else {
								servidores.setTrClass("error");
								servidores.setStatus("não OK");
							}
							this.iteracoesDAO.updateServer(servidores);
						} catch (IOException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							this.iteracoesDAO.updateServer(servidores);
						}

					}

					// if Others
					if (servidores.getSO().equals("OUTRO")) {
						servidores.setServerTime(dt.getTime());
						servidores.setLastCheck(servidores.getServerTime());
						// Decripting password
						try {
							servidores.setPass(String.valueOf(Crypto
									.decode(servidores.getPass())));
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
							String dateSTR = GetDateOther.exec(
									servidores.getUser(), servidores.getIp(),
									servidores.getPass(), servidores.getPort());
							Logger.getLogger(getClass()).debug("Hora obtida do servidor " + servidores.getHostname() + ": " + dateSTR);
							servidores.setClientTime(dateSTR);
							// Calculating time difference
							servidores.setDifference(dt.diffrenceTime(
									servidores.getServerTime(), dateSTR,
									"OUTRO"));
							
							PropertiesLoaderImpl load = new PropertiesLoaderImpl();
						    String version = load.getValor("version"); 
						    result.include("version",version);	if (servidores.getDifference() < 0){
								servidores.setDifference(servidores.getDifference() * -1);
							}
							if (servidores.getDifference() <= this.configurationDAO
									.getDiffirenceSecs()) {
								servidores.setTrClass("success");
								servidores.setStatus("OK");
							} else {
								servidores.setTrClass("error");
								servidores.setStatus("não OK");
							}
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e) {
								Logger.getLogger(getClass())
										.error("[ " + userInfo.getLoggedUsername() + " ] Error: ", e);
							}
							this.iteracoesDAO.updateServer(servidores);
						} catch (JSchException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error("[ " + userInfo.getLoggedUsername() + " ] Error: ",
										e1);
							}
							this.iteracoesDAO.updateServer(servidores);
						} catch (IOException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error("[ " + userInfo.getLoggedUsername() + " ] Error: ",
										e1);
							}
							this.iteracoesDAO.updateServer(servidores);
						}

					}

				}

				result.include("server", list).forwardTo(HomeController.class)
						.home("");
				result.include("class","activeServer");
			}
		}

		if (value.equals("notFull")) {
			lockedResource.setRecurso("notOkverification");
			lockedResource.setUsername(LoggedUsername);
			List<Lock> lockList = this.lockDAO
					.listLockedServices("notOkverification");
			if (lockList.size() != 0) {
				for (Lock lock : lockList) {
					Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] O recurso notOkverification está locado pelo usuário "
									+ lock.getUsername()
									+ ", aguarde o término da mesma");
					result.include("class","activeServer");
					result.include(
							"info",
							"O recurso notOkverification está locado pelo usuário "
									+ lock.getUsername()
									+ ", aguarde o término da mesma")
							.forwardTo(HomeController.class).home("");

				}
			} else {
				Logger.getLogger(getClass())
						.info("[ " + userInfo.getLoggedUsername() + " ] O recurso notOkverification não está locado, locando e proseguindo");
				// locar recurso.
				lockDAO.insertLock(lockedResource);

				// inserindo html title no result
				result.include("title", "Hr Status Home");
				// arrumar o select NOK
				List<Servidores> list = this.iteracoesDAO.getServersNOK();
				for (Servidores servidores : list) {
					// if Linux
					if (servidores.getSO().equals("LINUX")) {
						servidores.setServerTime(dt.getTime());
						servidores.setLastCheck(servidores.getServerTime());
						// Decripting password
						try {
							servidores.setPass(String.valueOf(Crypto
									.decode(servidores.getPass())));
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
							String dateSTR = GetDateLinux.exec(
									servidores.getUser(), servidores.getIp(),
									servidores.getPass(), servidores.getPort());
							Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Hora obtida do servidor " + servidores.getHostname() + ": " + dateSTR);
							servidores.setClientTime(dateSTR);
							// Calculating time difference
							servidores.setDifference(dt.diffrenceTime(
									servidores.getServerTime(), dateSTR,
									"LINUX"));
							if (servidores.getDifference() < 0){
								servidores.setDifference(servidores.getDifference() * -1);
							}
							if (servidores.getDifference() <= this.configurationDAO
									.getDiffirenceSecs()) {
								servidores.setTrClass("success");
								servidores.setStatus("OK");
							} else {
								servidores.setTrClass("error");
								servidores.setStatus("não OK");
							}
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e) {
								Logger.getLogger(getClass())
										.error("[ " + userInfo.getLoggedUsername() + " ] Error: ", e);
							}
							this.iteracoesDAO.updateServer(servidores);

						} catch (JSchException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error("[ " + userInfo.getLoggedUsername() + " ] Error: ",
										e1);
							}
							this.iteracoesDAO.updateServer(servidores);
						} catch (IOException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error("[ " + userInfo.getLoggedUsername() + " ] Error: ",
										e1);
							}
							this.iteracoesDAO.updateServer(servidores);
						}

					}

					if (servidores.getSO().equals("UNIX")) {
						servidores.setServerTime(dt.getTime());
						servidores.setLastCheck(servidores.getServerTime());
						// Decripting password
						try {
							servidores.setPass(String.valueOf(Crypto
									.decode(servidores.getPass())));
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
							String dateSTR = GetDateUnix.exec(
									servidores.getUser(), servidores.getIp(),
									servidores.getPass(), servidores.getPort());
							Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Hora obtida do servidor " + servidores.getHostname() + ": " + dateSTR);
							servidores.setClientTime(dateSTR);
							// Calculating time difference
							servidores.setDifference(dt.diffrenceTime(
									servidores.getServerTime(), dateSTR,
									"UNIX"));
							if (servidores.getDifference() < 0){
								servidores.setDifference(servidores.getDifference() * -1);
							}
							if (servidores.getDifference() <= this.configurationDAO
									.getDiffirenceSecs()) {
								servidores.setTrClass("success");
								servidores.setStatus("OK");
							} else {
								servidores.setTrClass("error");
								servidores.setStatus("não OK");
							}
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e) {
								Logger.getLogger(getClass())
										.error("[ " + userInfo.getLoggedUsername() + " ] Error: ", e);
							}
							this.iteracoesDAO.updateServer(servidores);
						} catch (JSchException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error("[ " + userInfo.getLoggedUsername() + " ] Error: ",
										e1);
							}
							this.iteracoesDAO.updateServer(servidores);
						} catch (IOException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error("[ " + userInfo.getLoggedUsername() + " ] Error: ",
										e1);
							}
							this.iteracoesDAO.updateServer(servidores);
						}
					}

					// if Windows
					if (servidores.getSO().equals("WINDOWS")) {
						servidores.setServerTime(dt.getTime());
						servidores.setLastCheck(servidores.getServerTime());
						try {
							String dateSTR = GetDateWindows.Exec(servidores
									.getIp());
							Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Hora obtida do servidor " + servidores.getHostname() + ": " + dateSTR);
							servidores.setClientTime(dateSTR);
							// Calculating time difference
							servidores.setDifference(dt.diffrenceTime(
									servidores.getServerTime(), dateSTR,
									"WINDOWS"));
							if (servidores.getDifference() < 0){
								servidores.setDifference(servidores.getDifference() * -1);
							}
							if (servidores.getDifference() <= this.configurationDAO
									.getDiffirenceSecs()) {
								servidores.setTrClass("success");
								servidores.setStatus("OK");
							} else {
								servidores.setTrClass("error");
								servidores.setStatus("não OK");
							}

							this.iteracoesDAO.updateServer(servidores);
						} catch (IOException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							this.iteracoesDAO.updateServer(servidores);
						}
					}

					// if Others
					if (servidores.getSO().equals("OUTRO")) {
						servidores.setServerTime(dt.getTime());
						servidores.setLastCheck(servidores.getServerTime());
						// Decripting password
						try {
							servidores.setPass(String.valueOf(Crypto
									.decode(servidores.getPass())));
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
							String dateSTR = GetDateOther.exec(
									servidores.getUser(), servidores.getIp(),
									servidores.getPass(), servidores.getPort());
							Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Hora obtida do servidor " + servidores.getHostname() + ": " + dateSTR);
							servidores.setClientTime(dateSTR);
							// Calculating time difference
							servidores.setDifference(dt.diffrenceTime(
									servidores.getServerTime(), dateSTR,
									"OUTRO"));
							if (servidores.getDifference() < 0){
								servidores.setDifference(servidores.getDifference() * -1);
							}
							if (servidores.getDifference() <= this.configurationDAO
									.getDiffirenceSecs()) {
								servidores.setTrClass("success");
								servidores.setStatus("OK");
							} else {
								servidores.setTrClass("error");
								servidores.setStatus("não OK");
							}
							try {
								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e) {
								Logger.getLogger(getClass())
										.error("[ " + userInfo.getLoggedUsername() + " ] Error: ", e);
							}
							this.iteracoesDAO.updateServer(servidores);
						} catch (JSchException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {
								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error("[ " + userInfo.getLoggedUsername() + " ] Error: ",
										e1);
							}
							this.iteracoesDAO.updateServer(servidores);
						} catch (IOException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {
								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores
										.getPass()));

							} catch (Exception e1) {
								Logger.getLogger(getClass()).error("[ " + userInfo.getLoggedUsername() + " ] Error: ",
										e1);
							}
							this.iteracoesDAO.updateServer(servidores);
						}
					}
				}
				result.include("class","activeServer");
				result.include("server", list).forwardTo(HomeController.class)
						.home("");
				
			}
		}
		// desloca a tabela quando a verficação terminar.
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] Verificação finalizada, liberando recurso "
						+ lockedResource.getRecurso());
		lockDAO.removeLock(lockedResource);
	}
	
	@Get("/singleServerToVerify/{id}")
	public void singleServerToVerify(int id) throws JSchException, IOException{
		result.include("title", "Home");
		result.include("loggedUser", userInfo.getLoggedUsername());
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /singleServerToVerify/" + id);		
		Servidores servidor = this.iteracoesDAO.getServerByID(id);
		
		try {
			servidor.setPass(String.valueOf(Crypto.decode(servidor.getPass())));
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
		
		runVerify.runSingleVerification(servidor);
		
		List<Servidores> list = this.iteracoesDAO.listServerByID(id);
		result.include("class","activeServer");
		result.include("server", list).forwardTo(HomeController.class).home("");
		
	}
	
}