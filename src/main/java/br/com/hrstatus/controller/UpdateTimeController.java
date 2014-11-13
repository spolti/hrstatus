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

//Erros comuns
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
	

	@Get("updateTimeSelectedClients/{ids}")
	public void updateTimeSelectedClients(String ids) throws JSchException, IOException {
		
		result.include("loggedUser", userInfo.getLoggedUsername());
		
		result.include("title", "Home");
		
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /updateTimeSelectedClients");

		String servidores[] = ids.split(",");
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Tentando atualizar data/hora do servidor(es): " + ids);
		
		for (int i = 0; i < servidores.length; i++) {
			id = Integer.parseInt(servidores[i]);
			servidor = this.iteracoesDAO.getServerByID(id);

			if (servidor.getSO().equals("LINUX") || servidor.getSO().equals("UNIX") || servidor.getSO().equals("OUTRO")){
				command = (servidor.getSuCommand() + " " + this.configurationDAO.getNtpServerAddress());
				//descriptografando a senha:
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
				
				log.info("[ " + userInfo.getLoggedUsername() + " ] Tentando atualizar data [command: " + command + "] no servidor " + servidor.getHostname());
				resultCommand = RunNtpDate.exec(servidor.getUser(), servidor.getIp(), servidor.getPass(), servidor.getPort(), command);

				if (resultCommand.equals("")){
					validator.add(new ValidationMessage(servidor.getHostname() + ": Não foi possível executar a atualização automática, provavelmente erro na execução do comando utilizado.", "Erro"));
				
				}else{
					//Está com erro somente para fazer append nas informações do resultado na página.
					validator.add(new ValidationMessage(servidor.getHostname() + ": " + resultCommand, "Erro"));
					//chamando a re-verificação individual de servidor passando objeto.
					runVerify.runSingleVerification(servidor);				
					log.fine("[ " + userInfo.getLoggedUsername() + " ] " + resultCommand);
				}

			}else if (servidor.getSO().equals("WINDOWS")){
				log.info("[ " + userInfo.getLoggedUsername() + " ] Servidores Windows não são suportados para esta opção.");
				validator.add(new ValidationMessage("Servidores Windows não são suportados para esta opção.", "Erro"));
			}
		}
		validator.onErrorUsePageOf(HomeController.class).home("");
		
	}

	@Get("/updateTimeAllClients")
	public void updateTimeAllClients() throws JSchException, IOException {
		result.include("title", "Home");
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /updateTimeAllClients");
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Tentando atualizar data/hora de todos os servidores desatualizados.");
		String ntpAddress = configurationDAO.getNtpServerAddress();
		//buscar todos os servidores não OK.
		List<Servidores> tempServer = this.iteracoesDAO.getServersNOK();
		for (Servidores servidor : tempServer){
			log.info("[ " + userInfo.getLoggedUsername() + " ] Servidor recebido para atualização: " + servidor.getHostname());
			if (servidor.getSO().equals("LINUX") || servidor.getSO().equals("UNIX") || servidor.getSO().equals("OUTRO")){
				command = (servidor.getSuCommand() + " " + ntpAddress);
				//descriptografando a senha:
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
				
				log.info("[ " + userInfo.getLoggedUsername() + " ] Tentando atualizar data [command: " + command + "] no servidor " + servidor.getHostname());
				resultCommand = RunNtpDate.exec(servidor.getUser(), servidor.getIp(), servidor.getPass(), servidor.getPort(), command);

				if (resultCommand.equals("")){
					log.info("[ " + userInfo.getLoggedUsername() + " ] " + servidor.getHostname() + ": Não foi possível executar a atualização automática, provavelmente erro na execução do comando utilizado.");
					validator.add(new ValidationMessage(servidor.getHostname() + ": Não foi possível executar a atualização automática, provavelmente erro na execução do comando utilizado.", "Erro"));
				}else{
					//Está com erro somente para fazer append nas informações do resultado na página.
					log.info("[ " + userInfo.getLoggedUsername() + " ] " + servidor.getHostname() + ": " + resultCommand);	
					validator.add(new ValidationMessage(servidor.getHostname() + ": " + resultCommand, "Erro"));
					//chamando a re-verificação individual de servidor passando objeto.
					runVerify.runSingleVerification(servidor);				
					log.fine("[ " + userInfo.getLoggedUsername() + " ] " + resultCommand);
				}

			}else if (servidor.getSO().equals("WINDOWS")){
				log.info("[ " + userInfo.getLoggedUsername() + " ] Servidores Windows não são suportados para esta opção.");
				validator.add(new ValidationMessage("Servidores Windows não são suportados para esta opção.", "Erro"));
			}
		}
		validator.onErrorUsePageOf(HomeController.class).home("");
		
	}
}