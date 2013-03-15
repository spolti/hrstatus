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

//Erros comuns
//09:47:11,464 ERROR [stderr] (Connect thread 10.11.152.76 session) sudo: no tty present and no askpass program specified
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
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.ohsnap.hrstatus.action.VerifySingleServer;
import br.com.ohsnap.hrstatus.action.linux.RunNtpDate;
import br.com.ohsnap.hrstatus.dao.Configuration;
import br.com.ohsnap.hrstatus.dao.Iteracoes;
import br.com.ohsnap.hrstatus.model.Servidores;
import br.com.ohsnap.hrstatus.security.Crypto;
import br.com.ohsnap.hrstatus.utils.UserInfo;

import com.jcraft.jsch.JSchException;

@Resource
public class UpdateTimeController {

	Servidores servidor = new Servidores();
	String command = null;
	int id = 0;
	String resultCommand = null;
	UserInfo userInfo = new UserInfo();
	
	private Iteracoes iteracoesDAO;
	private Configuration configurationDAO;	
	private Result result;
	private Validator validator;
	private VerifySingleServer runVerify;
	
	public UpdateTimeController(Iteracoes iteracoesDAO, Configuration configurationDAO, Result result, Validator validator, VerifySingleServer runVerify) {
		this.iteracoesDAO = iteracoesDAO;
		this.configurationDAO = configurationDAO;
		this.result = result;
		this.validator= validator;
		this.runVerify = runVerify;
	}

	@Get("updateTimeSelectedClients/{ids}")
	public void updateTimeSelectedClients(String ids) throws JSchException, IOException {
		
		result.include("loggedUser", userInfo.getLoggedUsername());
		
		result.include("title", "Home");
		
		Logger.getLogger(getClass()).info(
				"URI Called: /updateTimeSelectedClients");

		String servidores[] = ids.split(",");
		Logger.getLogger(getClass()).debug(
				"Tentando atualizar data/hora do servidor(es): " + ids);
		
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
				
				Logger.getLogger(getClass()).info("Tentando atualizar data [command: " + command + "] no servidor " + servidor.getHostname());
				resultCommand = RunNtpDate.exec(servidor.getUser(), servidor.getIp(), servidor.getPass(), servidor.getPort(), command);

				if (resultCommand.equals("")){
					validator.add(new ValidationMessage(servidor.getHostname() + ": Não foi possível executar a atualização automática, provavelmente erro na execução do comando utilizado.", "Erro"));
				
				}else{
					//Está com erro somente para fazer append nas informações do resultado na página.
					validator.add(new ValidationMessage(servidor.getHostname() + ": " + resultCommand, "Erro"));
					//chamando a re-verificação individual de servidor passando objeto.
					runVerify.runSingleVerification(servidor);				
					Logger.getLogger(getClass()).debug(resultCommand);
				}

			}else if (servidor.getSO().equals("WINDOWS")){
				Logger.getLogger(getClass()).info("Servidores Windows não são suportados para esta opção.");
				validator.add(new ValidationMessage("Servidores Windows não são suportados para esta opção.", "Erro"));
			}
		}
		validator.onErrorUsePageOf(HomeController.class).home("");
		
	}

	@Get("/updateTimeAllClients")
	public void updateTimeAllClients() throws JSchException, IOException {
		result.include("title", "Home");
		Logger.getLogger(getClass()).info("URI Called: /updateTimeAllClients");
		Logger.getLogger(getClass())
				.debug("Tentando atualizar data/hora de todos os servidores desatualizados.");
		String ntpAddress = configurationDAO.getNtpServerAddress();
		//buscar todos os servidores não OK.
		List<Servidores> tempServer = this.iteracoesDAO.getServersNOK();
		for (Servidores servidor : tempServer){
			Logger.getLogger(getClass()).info("Servidor recebido para atualização: " + servidor.getHostname());
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
				
				Logger.getLogger(getClass()).info("Tentando atualizar data [command: " + command + "] no servidor " + servidor.getHostname());
				resultCommand = RunNtpDate.exec(servidor.getUser(), servidor.getIp(), servidor.getPass(), servidor.getPort(), command);

				if (resultCommand.equals("")){
					Logger.getLogger(getClass()).info(servidor.getHostname() + ": Não foi possível executar a atualização automática, provavelmente erro na execução do comando utilizado.");
					validator.add(new ValidationMessage(servidor.getHostname() + ": Não foi possível executar a atualização automática, provavelmente erro na execução do comando utilizado.", "Erro"));
				}else{
					//Está com erro somente para fazer append nas informações do resultado na página.
					Logger.getLogger(getClass()).info(servidor.getHostname() + ": " + resultCommand);	
					validator.add(new ValidationMessage(servidor.getHostname() + ": " + resultCommand, "Erro"));
					//chamando a re-verificação individual de servidor passando objeto.
					runVerify.runSingleVerification(servidor);				
					Logger.getLogger(getClass()).debug(resultCommand);
				}

			}else if (servidor.getSO().equals("WINDOWS")){
				Logger.getLogger(getClass()).info("Servidores Windows não são suportados para esta opção.");
				validator.add(new ValidationMessage("Servidores Windows não são suportados para esta opção.", "Erro"));
			}
		}
		validator.onErrorUsePageOf(HomeController.class).home("");
		
	}
}
