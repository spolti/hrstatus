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
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.hrstatus.action.linux.RunNtpDate;
import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.utils.UserInfo;
import br.com.hrstatus.verification.os.VerifySingleServer;

import com.jcraft.jsch.JSchException;

/*
 * @author spolti
 */

//Known issues
//ERROR [stderr] (Connect thread 10.11.152.76 session) sudo: no tty present and no askpass program specified

@Resource
public class UpdateTimeController {

	Logger log =  Logger.getLogger(UpdateTimeController.class.getCanonicalName());
	
	@Autowired
	private ServersInterface iteracoesDAO;
	@Autowired
	private Configuration configurationDAO;	
	@Autowired
	private Result result;
	@Autowired
	private Validator validator;
	@Autowired
	private VerifySingleServer runVerify;
	Servidores servidor = new Servidores();
	String command = null;
	int id = 0;
	String resultCommand = null;
	UserInfo userInfo = new UserInfo();
	

	@Get("/updateTimeSelectedClients/{ids}")
	public void updateTimeSelectedClients(String ids) throws JSchException, IOException {
		
		// Inserting HTML title in the result
		result.include("title", "Home");
		// Inserting the Logged username in the home page
		result.include("loggedUser", userInfo.getLoggedUsername());
		
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /updateTimeSelectedClients");
		String servidores[] = ids.split(",");
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Trying to update the time/date in the servers: " + ids);
		
		for (int i = 0; i < servidores.length; i++) {
			id = Integer.parseInt(servidores[i]);
			servidor = this.iteracoesDAO.getServerByID(id);

			if (servidor.getSO().equals("LINUX") || servidor.getSO().equals("UNIX") || servidor.getSO().equals("OUTRO")){
				command = (servidor.getSuCommand() + " " + this.configurationDAO.getNtpServerAddress());
				
				// Decrypting the password
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
				
				log.info("[ " + userInfo.getLoggedUsername() + " ] Trying to update the date [command: " + command + "] in the server " + servidor.getHostname());
				resultCommand = RunNtpDate.exec(servidor.getUser(), servidor.getIp(), servidor.getPass(), servidor.getPort(), command);

				if (resultCommand.equals("")){
					validator.add(new ValidationMessage(servidor.getHostname() + ": It was not possible execute the automatically update probably due to an error during the command execution.", "Erro"));
				
				}else{
					// this step have an error to append the result in the home page where the result is shown
					validator.add(new ValidationMessage(servidor.getHostname() + ": " + resultCommand, "Erro"));
					//Calling the individual verification of the server passswed as parameter
					runVerify.runSingleVerification(servidor);				
					log.fine("[ " + userInfo.getLoggedUsername() + " ] " + resultCommand);
				}

			}else if (servidor.getSO().equals("WINDOWS")){
				log.info("[ " + userInfo.getLoggedUsername() + " ] This option does not support Windows servers. If you want to use this opetion, please use Unix like.. :0 gotcha, i'm kidding :)");
				validator.add(new ValidationMessage("Servidores Windows não são suportados para esta opção.", "Erro"));
			}
		}
		validator.onErrorUsePageOf(HomeController.class).home("");
		
	}

	@Get("/updateTimeAllClients")
	public void updateTimeAllClients() throws JSchException, IOException {
		
		result.include("title", "Home");
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /updateTimeAllClients");
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Trying to update all outdated servers.");
		String ntpAddress = configurationDAO.getNtpServerAddress();
		
		//Listing all outdated servers
		List<Servidores> tempServer = this.iteracoesDAO.getServersNOK();
		for (Servidores servidor : tempServer){
			log.info("[ " + userInfo.getLoggedUsername() + " ] Server received to update: " + servidor.getHostname());
			if (servidor.getSO().equals("LINUX") || servidor.getSO().equals("UNIX") || servidor.getSO().equals("OUTRO")){
				command = (servidor.getSuCommand() + " " + ntpAddress);
				
				//Decrypting the password
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
				
				log.info("[ " + userInfo.getLoggedUsername() + " ] Trying to update the date [command: " + command + "] in the server " + servidor.getHostname());
				resultCommand = RunNtpDate.exec(servidor.getUser(), servidor.getIp(), servidor.getPass(), servidor.getPort(), command);

				if (resultCommand.equals("")){
					log.info("[ " + userInfo.getLoggedUsername() + " ] " + servidor.getHostname() + ": It was not possible execute the automatically update probably due to an error during the command execution.");
					validator.add(new ValidationMessage(servidor.getHostname() + ": Não foi possível executar a atualização automática, provavelmente erro na execução do comando utilizado.", "Erro"));
				}else{
					
					log.info("[ " + userInfo.getLoggedUsername() + " ] " + servidor.getHostname() + ": " + resultCommand);	
					validator.add(new ValidationMessage(servidor.getHostname() + ": " + resultCommand, "Erro"));
					
					// Calling the individual verification of the server passswed as parameter
					runVerify.runSingleVerification(servidor);				
					log.fine("[ " + userInfo.getLoggedUsername() + " ] " + resultCommand);
				}

			}else if (servidor.getSO().equals("WINDOWS")){
				log.info("[ " + userInfo.getLoggedUsername() + " ] This option does not support Windows servers. If you want to use this opetion, please use Unix like.. :0 gotcha, i'm kidding :)");
				validator.add(new ValidationMessage("Servidores Windows não são suportados para esta opção.", "Erro"));
			}
		}
		validator.onErrorUsePageOf(HomeController.class).home("");
		
	}
}