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

package br.com.hrstatus.verification.os;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.jcraft.jsch.JSchException;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.hrstatus.action.linux.GetDateLinux;
import br.com.hrstatus.action.other.GetDateOther;
import br.com.hrstatus.action.unix.GetDateUnix;
import br.com.hrstatus.action.windows.GetDateWindows;
import br.com.hrstatus.controller.HomeController;
import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.dao.LockIntrface;
import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.model.Lock;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.utils.PropertiesLoaderImpl;
import br.com.hrstatus.utils.UserInfo;
import br.com.hrstatus.utils.date.DateUtils;

/*
 * @author spolti
 */

@Resource
public class FullVerification {

	Logger log =  Logger.getLogger(FullVerification.class.getCanonicalName());
	
	@Autowired
	private Result result;
	@Autowired
	private ServersInterface serversDAO;
	@Autowired
	private Configuration configurationDAO;
	@Autowired
	private LockIntrface lockDAO;
	UserInfo userInfo = new UserInfo();
	DateUtils getTime = new DateUtils();
	private Crypto encodePass = new Crypto();
	private Lock lockedResource = new Lock();
	private DateUtils dt = new DateUtils();

	@SuppressWarnings("static-access")
	@Get("/home/startVerification/full")
	public void startFullVerification() throws InterruptedException, JSchException {
		// inserindo html title no result
		result.include("title", "Hr Status Home");
		String LoggedUsername = userInfo.getLoggedUsername();

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI called: /home/startVerification/full");

		// Verifica se já tem alguma verificação ocorrendo...
		log.info("[ " + userInfo.getLoggedUsername() + " ] Initializing a full verification.");

		lockedResource.setRecurso("verificationFull");
		lockedResource.setUsername(LoggedUsername);
		List<Lock> lockList = this.lockDAO.listLockedServices("verificationFull");
		if (lockList.size() != 0) {
			for (Lock lock : lockList) {
				log.info("[ " + userInfo.getLoggedUsername() + " ] O recurso verificationFull está locado pelo usuário "
								+ lock.getUsername() + ", aguarde o término da mesma");
				result.include("class", "activeServer");
				result.include("info", "O recurso verificationFull está locado pelo usuário " + lock.getUsername()
								+ ", aguarde o término da mesma").forwardTo(HomeController.class).home("");

			}
		} else {
			log.info("[ " + userInfo.getLoggedUsername() + " ] O recurso verificationFull não está locado, locando e proseguindo");

			List<Servidores> list = this.serversDAO.listServersVerActive();
			if (list.size() <= 0) {
				log.info("[ " + userInfo.getLoggedUsername()
								+ " ] Nenhum servidor encontrado ou não há servidores com verficação ativa");
				result.include("info","Nenhum servidor encontrado ou não há servidores com verficação ativa").forwardTo(HomeController.class).home("");

			} else {
				// locar recurso.
				lockDAO.insertLock(lockedResource);
				for (Servidores servidores : list) {
					// if Linux
					if (servidores.getSO().equals("LINUX") && servidores.getVerify().equals("SIM")) {
						servidores.setServerTime(dt.getTime());
						servidores.setLastCheck(servidores.getServerTime());
						// Decripting password
						try {
							servidores.setPass(String.valueOf(Crypto.decode(servidores.getPass())));
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
							String dateSTR = GetDateLinux.exec(servidores.getUser(), servidores.getIp(), servidores.getPass(), servidores.getPort());
							log.severe("[ " + userInfo.getLoggedUsername() + " ] Hora obtida do servidor "
											+ servidores.getHostname() + ": " + dateSTR);
							servidores.setClientTime(dateSTR);
							// Calculating time difference
							servidores.setDifference(dt.diffrenceTime(servidores.getServerTime(), dateSTR, "LINUX"));

							if (servidores.getDifference() < 0) {
								servidores.setDifference(servidores.getDifference() * -1);
							}

							if (servidores.getDifference() <= this.configurationDAO.getDiffirenceSecs()) {
								servidores.setTrClass("success");
								servidores.setStatus("OK");
							} else {
								servidores.setTrClass("error");
								servidores.setStatus("não OK");
							}
							try {
								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores.getPass()));

							} catch (Exception e1) {
								log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e1);
							}
							this.serversDAO.updateServer(servidores);

						} catch (JSchException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {
								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores.getPass()));

							} catch (Exception e1) {
								log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e1);
							}
							this.serversDAO.updateServer(servidores);
						} catch (IOException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores.getPass()));

							} catch (Exception e1) {
								log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e1);
							}
							this.serversDAO.updateServer(servidores);
						}

					} else if (servidores.getVerify().equals("NAO")) {
						log.info("[ " + userInfo.getLoggedUsername() + " ] - O servidor "
										+ servidores.getHostname() + " não será verificado pois o mesmo não está com verificação ativa.");
					}

					if (servidores.getSO().equals("UNIX") && servidores.getVerify().equals("SIM")) {
						servidores.setServerTime(dt.getTime());
						servidores.setLastCheck(servidores.getServerTime());
						// Decripting password
						try {
							servidores.setPass(String.valueOf(Crypto.decode(servidores.getPass())));
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
							String dateSTR = GetDateUnix.exec(servidores.getUser(), servidores.getIp(),	servidores.getPass(), servidores.getPort());
							log.severe("[ " + userInfo.getLoggedUsername() + " ] Hora obtida do servidor "
											+ servidores.getHostname() + ": " + dateSTR);
							servidores.setClientTime(dateSTR);
							// Calculating time difference
							servidores.setDifference(dt.diffrenceTime(servidores.getServerTime(), dateSTR, "UNIX"));

							if (servidores.getDifference() < 0) {
								servidores.setDifference(servidores.getDifference() * -1);
							}
							if (servidores.getDifference() <= this.configurationDAO.getDiffirenceSecs()) {
								servidores.setTrClass("success");
								servidores.setStatus("OK");
							} else {
								servidores.setTrClass("error");
								servidores.setStatus("não OK");
							}
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores.getPass()));

							} catch (Exception e) {
								log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e);
							}
							this.serversDAO.updateServer(servidores);
						} catch (JSchException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores.getPass()));

							} catch (Exception e1) {
								log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e1);
							}
							this.serversDAO.updateServer(servidores);
						} catch (IOException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores.getPass()));

							} catch (Exception e1) {
								log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e1);
							}
							this.serversDAO.updateServer(servidores);
						}

					} else if (servidores.getVerify().equals("NAO")) {
						log.info("[ " + userInfo.getLoggedUsername() + " ] - O servidor "
										+ servidores.getHostname() + " não será verificado pois o mesmo não está com verificação ativa.");
					}

					// if Windows
					if (servidores.getSO().equals("WINDOWS") && servidores.getVerify().equals("SIM")) {
						servidores.setServerTime(dt.getTime());
						servidores.setLastCheck(servidores.getServerTime());
						try {

							String dateSTR = GetDateWindows.Exec(servidores.getIp(), "I");
							if (dateSTR == null || dateSTR == "") {
								log.severe("Parametro net time -I retornou nulo, tentando o parametro S");
								dateSTR = GetDateWindows.Exec(servidores.getIp(), "S");
							}
							log.severe("[ " + userInfo.getLoggedUsername() + " ] Hora obtida do servidor "
											+ servidores.getHostname() + ": " + dateSTR);
							servidores.setClientTime(dateSTR);
							// Calculating time difference
							servidores.setDifference(dt.diffrenceTime(servidores.getServerTime(), dateSTR, "WINDOWS"));

							if (servidores.getDifference() < 0) {
								servidores.setDifference(servidores.getDifference() * -1);
							}

							if (servidores.getDifference() <= this.configurationDAO.getDiffirenceSecs()) {
								servidores.setTrClass("success");
								servidores.setStatus("OK");
							} else if (dateSTR == null || dateSTR == "") {
								servidores.setTrClass("error");
								servidores.setStatus("Não foi possível obter data/hora deste servidor, verique conectividade.");
								servidores.setDifference(00);
							} else {
								servidores.setTrClass("error");
								servidores.setStatus("não OK");
							}
							this.serversDAO.updateServer(servidores);
						} catch (IOException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							this.serversDAO.updateServer(servidores);
						}

					} else if (servidores.getVerify().equals("NAO")) {
						log.info("[ " + userInfo.getLoggedUsername() + " ] - O servidor "
										+ servidores.getHostname() + " não será verificado pois o mesmo não está com verificação ativa.");
					}

					// if Others
					if (servidores.getSO().equals("OUTRO") && servidores.getVerify().equals("SIM")) {
						servidores.setServerTime(dt.getTime());
						servidores.setLastCheck(servidores.getServerTime());
						// Decripting password
						try {
							servidores.setPass(String.valueOf(Crypto.decode(servidores.getPass())));
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
							String dateSTR = GetDateOther.exec(servidores.getUser(), servidores.getIp(), servidores.getPass(), servidores.getPort());
							log.severe("Hora obtida do servidor " + servidores.getHostname() + ": " + dateSTR);
							servidores.setClientTime(dateSTR);
							// Calculating time difference
							servidores.setDifference(dt.diffrenceTime(servidores.getServerTime(), dateSTR,"OUTRO"));

							PropertiesLoaderImpl load = new PropertiesLoaderImpl();
							String version = load.getValor("version");
							result.include("version", version);
							if (servidores.getDifference() < 0) {
								servidores.setDifference(servidores.getDifference() * -1);
							}
							if (servidores.getDifference() <= this.configurationDAO.getDiffirenceSecs()) {
								servidores.setTrClass("success");
								servidores.setStatus("OK");
							} else {
								servidores.setTrClass("error");
								servidores.setStatus("não OK");
							}
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores.getPass()));

							} catch (Exception e) {
								log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e);
							}
							this.serversDAO.updateServer(servidores);
						} catch (JSchException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores.getPass()));

							} catch (Exception e1) {
								log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e1);
							}
							this.serversDAO.updateServer(servidores);
						} catch (IOException e) {
							servidores.setStatus(e + "");
							servidores.setTrClass("error");
							try {

								// Critpografando a senha
								servidores.setPass(encodePass.encode(servidores.getPass()));

							} catch (Exception e1) {
								log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e1);
							}
							this.serversDAO.updateServer(servidores);
						}

					} else if (servidores.getVerify().equals("NAO")) {
						log.info("[ " + userInfo.getLoggedUsername() + " ] - O servidor "
										+ servidores.getHostname() + " não será verificado pois o mesmo não está com verificação ativa.");
					}

				}

				result.include("server", list).forwardTo(HomeController.class).home("");
				result.include("class", "activeServer");
			}
		}
		// desloca a tabela quando a verficação terminar.
		log.info("[ " + userInfo.getLoggedUsername() + " ] Verificação finalizada, liberando recurso "
						+ lockedResource.getRecurso());
		lockDAO.removeLock(lockedResource);
	}
}