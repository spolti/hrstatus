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

package br.com.ohsnap.hrstatus.controller;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.ohsnap.hrstatus.action.linux.GetDateLinux;
import br.com.ohsnap.hrstatus.action.other.GetDateOther;
import br.com.ohsnap.hrstatus.action.unix.GetDateUnix;
import br.com.ohsnap.hrstatus.action.windows.GetDateWindows;
import br.com.ohsnap.hrstatus.dao.Configuration;
import br.com.ohsnap.hrstatus.dao.Iteracoes;
import br.com.ohsnap.hrstatus.dao.LockIntrface;
import br.com.ohsnap.hrstatus.model.Lock;
import br.com.ohsnap.hrstatus.model.Servidores;
import br.com.ohsnap.hrstatus.security.Crypto;
import br.com.ohsnap.hrstatus.utils.DateUtils;

import com.jcraft.jsch.JSchException;

@Resource
public class HomeController {

	private Result result;
	private Iteracoes iteracoesDAO;
	private Configuration configurationDAO;
	private Validator validator;
	private LockIntrface lockDAO;

	public HomeController(Result result, Iteracoes iteracoesDAO,
			Configuration configurationDAO, Validator validator,LockIntrface lockDAO) {
		this.result = result;
		this.iteracoesDAO = iteracoesDAO;
		this.configurationDAO = configurationDAO;
		this.validator = validator;
		this.lockDAO = lockDAO;
	}

	@Get("/home")
	public void home(String verification) {
		//inserindo html title no result
		result.include("title","Hr Status Home");
		
		Logger.getLogger(getClass()).info("URI Called: /home");

		//inserindo username noa home:
		Object  LoggedObjectUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String LoggedUsername = ((UserDetails) LoggedObjectUser).getUsername();
		result.include("loggedUser", LoggedUsername);
		// ///////////////////////////////////////
		// Sending SERVERS % to plot SO's graph
		int linux = this.iteracoesDAO.countLinux();
		int windows = this.iteracoesDAO.countWindows();
		int unix = this.iteracoesDAO.countUnix();
		int other = this.iteracoesDAO.countOther();
		int total = this.iteracoesDAO.countAllServers();
		if (linux > 0 && windows > 0 && unix > 0) {
			result.include("linux", (linux * 100) / total);
			result.include("windows", (windows * 100) / total);
			result.include("unix", (unix * 100) / total);
			result.include("other", (other * 100) / total);
			Logger.getLogger(getClass()).info("Total: " + total);
			Logger.getLogger(getClass()).info("linux: " + linux);
			Logger.getLogger(getClass()).info("Windows: " + windows);
			Logger.getLogger(getClass()).info("Unix: " + unix);
			Logger.getLogger(getClass()).info("Other: " + other);

		} else if (linux > 0) {
			result.include("linux", (linux * 100) / total);
			result.include("windows", 0);
			result.include("unix", 0);
			result.include("other", 0);
			Logger.getLogger(getClass()).info("Total: " + total);
			Logger.getLogger(getClass()).info("linux: " + linux);
			Logger.getLogger(getClass()).info("Windows: " + windows);
			Logger.getLogger(getClass()).info("Unix: " + unix);
			Logger.getLogger(getClass()).info("Other: " + other);

		} else if (windows > 0) {
			result.include("linux", 0);
			result.include("windows", (windows * 100) / total);
			result.include("unix", 0);
			result.include("other", 0);
			Logger.getLogger(getClass()).info("Total: " + total);
			Logger.getLogger(getClass()).info("linux: " + linux);
			Logger.getLogger(getClass()).info("Windows: " + windows);
			Logger.getLogger(getClass()).info("Unix: " + unix);
			Logger.getLogger(getClass()).info("Other: " + other);

		} else if (unix > 0) {
			result.include("linux", 0);
			result.include("windows", 0);
			result.include("unix", (unix * 100) / total);
			result.include("other", 0);
			Logger.getLogger(getClass()).info("Total: " + total);
			Logger.getLogger(getClass()).info("linux: " + linux);
			Logger.getLogger(getClass()).info("Windows: " + windows);
			Logger.getLogger(getClass()).info("Unix: " + unix);
			Logger.getLogger(getClass()).info("Other: " + other);

		} else if (other > 0) {
			result.include("linux", 0);
			result.include("windows", 0);
			result.include("unix", 0);
			result.include("other", (other * 100) / total);
			Logger.getLogger(getClass()).info("Total: " + total);
			Logger.getLogger(getClass()).info("linux: " + linux);
			Logger.getLogger(getClass()).info("Windows: " + windows);
			Logger.getLogger(getClass()).info("Unix: " + unix);
			Logger.getLogger(getClass()).info("Other: " + other);
		}

		// Populating 2° graph (servers ok and not ok)
		result.include("serversOK", this.iteracoesDAO.countServersOK());
		result.include("serversNOK", this.iteracoesDAO.countServersNOK());

		// Ploting 3° graph.
		result.include("serversLinuxOK", this.iteracoesDAO.countLinuxOK());
		result.include("serversLinuxNOK", this.iteracoesDAO.countLinuxNOK());

		result.include("serversUnixOK", this.iteracoesDAO.countUnixOK());
		result.include("serversUnixNOK", this.iteracoesDAO.countUnixNOK());

		result.include("serversWindowsOK", this.iteracoesDAO.countWindowsOK());
		result.include("serversWindowsNOK", this.iteracoesDAO.countWindowsNOK());

		result.include("otherOK", this.iteracoesDAO.countOtherOK());
		result.include("otherNOK", this.iteracoesDAO.countOtherNOK());

		// ///////////////////////////////////////////////////////
	}

	@Get("/navbar")
	public void navbar() {
		Logger.getLogger(getClass()).info("URI Called: /navbar");
		
		
	}

	@Get("/home/showByStatus/{status}")
	public void showByStatus(String status) {
		//inserindo html title no result
		result.include("title","Hr Status Home");
		
		Logger.getLogger(getClass()).info(
				"URI Called: /home/showByStatus/" + status);

		if (status.equals("OK")) {
			List<Servidores> list = this.iteracoesDAO.getServersOK();
			result.include("server", list).forwardTo(HomeController.class)
					.home("");

		} else if (!status.equals("OK")) {
			List<Servidores> list = this.iteracoesDAO.getServersNOK();
			result.include("server", list).forwardTo(HomeController.class)
					.home("");

		} else {
			validator.onErrorUsePageOf(HomeController.class).home("");
		}

	}

	@Get("/home/startVerification/{value}")
	public void startVerification(String value) throws InterruptedException, JSchException {
		//inserindo html title no result
		result.include("title","Hr Status Home");
		
		Object  LoggedObjectUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String LoggedUsername = ((UserDetails) LoggedObjectUser).getUsername();
		
		Logger.getLogger(getClass()).info(
				"URI called: /home/startVerification/" + value);
		
		Lock lockedResource = new Lock();
		lockedResource.setRecurso("verificationFull");
		lockedResource.setUsername(LoggedUsername);
		List<Lock> lockList = this.lockDAO.listLockedServices("verificationFull");
		if (lockList.size() != 0){
			for (Lock lock : lockList){
				Logger.getLogger(getClass()).info("O recurso verificationFull está locado pelo usuário " + lock.getUsername() + ", aguarde o término da mesma");
				validator.add(new ValidationMessage("O recurso verificationFull está locado pelo usuário " + lock.getUsername() + ", aguarde o término da mesma", "Erro"));
				validator.onErrorUsePageOf(HomeController.class).home();
			}
		}else {
			Logger.getLogger(getClass()).info("O recurso verificationFull não está locado, locando e proseguindo");
			//locar recurso.
			lockDAO.insertLock(lockedResource);
		}
		//Verifica se já tem alguma verificação ocorrendo...
	
		Logger.getLogger(getClass()).info(
				"Initializing a " + value + " verification.");

		DateUtils dt = new DateUtils();

		Crypto encodePass = new Crypto();

		if (value.equals("full")) {
			List<Servidores> list = this.iteracoesDAO.listServers();
			for (Servidores servidores : list) {
				// if Linux
				if (servidores.getSO().equals("LINUX")) {
					servidores.setServerTime(dt.getTime("LINUX"));
					servidores.setLastCheck(dt.getTime("lastCheck"));
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
						String dateSTR = GetDateLinux.exec(servidores.getUser(),
								servidores.getIp(), servidores.getPass(),
								servidores.getPort());
						servidores.setClientTime(dateSTR);
						// Calculating time difference
						servidores.setDifference(dt.diffrenceTime(
								servidores.getServerTime(), dateSTR, "LINUX",servidores));
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
							Logger.getLogger(getClass()).error("Error: ", e1);
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
							Logger.getLogger(getClass()).error("Error: ", e1);
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
							Logger.getLogger(getClass()).error("Error: ", e1);
						}
						this.iteracoesDAO.updateServer(servidores);
					}

				}

				if (servidores.getSO().equals("UNIX")) {
					servidores.setServerTime(dt.getTime("UNIX"));
					servidores.setLastCheck(dt.getTime("lastCheck"));
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
						String dateSTR = GetDateUnix.exec(servidores.getUser(),
								servidores.getIp(), servidores.getPass(),
								servidores.getPort());
						servidores.setClientTime(dateSTR);
						// Calculating time difference
						servidores.setDifference(dt.diffrenceTime(
								servidores.getServerTime(), dateSTR, "UNIX", servidores));
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
							Logger.getLogger(getClass()).error("Error: ", e);
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
							Logger.getLogger(getClass()).error("Error: ", e1);
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
							Logger.getLogger(getClass()).error("Error: ", e1);
						}
						this.iteracoesDAO.updateServer(servidores);
					}

				}

				// if Windows
				if (servidores.getSO().equals("WINDOWS")) {
					servidores.setServerTime(dt.getTime("WINDOWS"));
					servidores.setLastCheck(dt.getTime("lastCheck"));
					try {
						String dateSTR = GetDateWindows.Exec(servidores.getIp());
						servidores.setClientTime(dateSTR);
						// Calculating time difference
						servidores
								.setDifference(dt.diffrenceTime(
										servidores.getServerTime(), dateSTR,
										"WINDOWS",servidores));
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
				
				//if Others
				if (servidores.getSO().equals("OUTRO")) {
					servidores.setServerTime(dt.getTime("OUTRO"));
					servidores.setLastCheck(dt.getTime("lastCheck"));
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
						String dateSTR = GetDateOther.exec(servidores.getUser(),
								servidores.getIp(), servidores.getPass(),
								servidores.getPort());
						servidores.setClientTime(dateSTR);
						// Calculating time difference
						servidores.setDifference(dt.diffrenceTime(
								servidores.getServerTime(), dateSTR, "OUTRO",servidores));
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
							Logger.getLogger(getClass()).error("Error: ", e);
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
							Logger.getLogger(getClass()).error("Error: ", e1);
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
							Logger.getLogger(getClass()).error("Error: ", e1);
						}
						this.iteracoesDAO.updateServer(servidores);
					}

				}
				
			}

			
			
			result.include("server", list).forwardTo(HomeController.class)
					.home("");
		}

		if (value.equals("notFull")) {
			//inserindo html title no result
			result.include("title","Hr Status Home");
			// arrumar o select NOK
			List<Servidores> list = this.iteracoesDAO.getServersNOK();
			for (Servidores servidores : list) {
				// if Linux
				if (servidores.getSO().equals("LINUX")) {
					servidores.setServerTime(dt.getTime("LINUX"));
					servidores.setLastCheck(dt.getTime("lastCheck"));
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
						String dateSTR = GetDateLinux.exec(servidores.getUser(),
								servidores.getIp(), servidores.getPass(),
								servidores.getPort());
						servidores.setClientTime(dateSTR);
						// Calculating time difference
						servidores.setDifference(dt.diffrenceTime(
								servidores.getServerTime(), dateSTR, "LINUX",servidores));
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
							Logger.getLogger(getClass()).error("Error: ", e);
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
							Logger.getLogger(getClass()).error("Error: ", e1);
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
							Logger.getLogger(getClass()).error("Error: ", e1);
						}
						this.iteracoesDAO.updateServer(servidores);
					}

				}

				if (servidores.getSO().equals("UNIX")) {
					servidores.setServerTime(dt.getTime("UNIX"));
					servidores.setLastCheck(dt.getTime("lastCheck"));
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
						String dateSTR = GetDateUnix.exec(servidores.getUser(),
								servidores.getIp(), servidores.getPass(),
								servidores.getPort());
						servidores.setClientTime(dateSTR);
						// Calculating time difference
						servidores.setDifference(dt.diffrenceTime(
								servidores.getServerTime(), dateSTR, "UNIX",servidores));
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
							Logger.getLogger(getClass()).error("Error: ", e);
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
							Logger.getLogger(getClass()).error("Error: ", e1);
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
							Logger.getLogger(getClass()).error("Error: ", e1);
						}
						this.iteracoesDAO.updateServer(servidores);
					}

				}

				// if Windows
				if (servidores.getSO().equals("WINDOWS")) {
					servidores.setServerTime(dt.getTime("WINDOWS"));
					servidores.setLastCheck(dt.getTime("lastCheck"));
					try {
						String dateSTR = GetDateWindows.Exec(servidores.getIp());
						servidores.setClientTime(dateSTR);
						// Calculating time difference
						servidores
								.setDifference(dt.diffrenceTime(
										servidores.getServerTime(), dateSTR,
										"WINDOWS",servidores));
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
				
				//if Others
				if (servidores.getSO().equals("OUTRO")) {
					servidores.setServerTime(dt.getTime("OUTRO"));
					servidores.setLastCheck(dt.getTime("lastCheck"));
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
						String dateSTR = GetDateOther.exec(servidores.getUser(),
								servidores.getIp(), servidores.getPass(),
								servidores.getPort());
						servidores.setClientTime(dateSTR);
						// Calculating time difference
						servidores.setDifference(dt.diffrenceTime(
								servidores.getServerTime(), dateSTR, "OUTRO",servidores));
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
							Logger.getLogger(getClass()).error("Error: ", e);
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
							Logger.getLogger(getClass()).error("Error: ", e1);
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
							Logger.getLogger(getClass()).error("Error: ", e1);
						}
						this.iteracoesDAO.updateServer(servidores);
					}

				}
			}			
			result.include("server", list).forwardTo(HomeController.class)
					.home("");
		}
		//desloca a tabela quando a verficação terminar.
		Logger.getLogger(getClass()).info("Verificação finalizada, liberando recurso " + lockedResource.getRecurso()); 
		lockDAO.removeLock(lockedResource);

	}
}