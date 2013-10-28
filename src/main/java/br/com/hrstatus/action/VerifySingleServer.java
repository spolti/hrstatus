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

package br.com.hrstatus.action;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.caelum.vraptor.Resource;
import br.com.hrstatus.action.linux.GetDateLinux;
import br.com.hrstatus.action.windows.GetDateWindows;
import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.dao.Iteracoes;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.utils.DateUtils;
import br.com.hrstatus.utils.UserInfo;

import com.jcraft.jsch.JSchException;

/*
 * @author spolti
 */

@Resource
public class VerifySingleServer {
	
	@Autowired
	private Configuration configurationDAO;
	@Autowired
	private Iteracoes iteracoesDAO;
	
	UserInfo userInfo = new UserInfo();
	
	public VerifySingleServer() {
	}
	
	@SuppressWarnings("static-access")
	public void runSingleVerification(Servidores servidores) throws JSchException, IOException{
		
		DateUtils dt = new DateUtils();
		Crypto encodePass = new Crypto();
		
		if (servidores.getSO().equals("LINUX")) {
			servidores.setServerTime(dt.getTime());
			servidores.setLastCheck(servidores.getServerTime());
			// Decripting password
			
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
					Logger.getLogger(getClass()).error("Error: ",
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
					Logger.getLogger(getClass()).error("Error: ",
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
					Logger.getLogger(getClass()).error("Error: ",
							e1);
				}
				this.iteracoesDAO.updateServer(servidores);
			}
		}
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
	}	
}