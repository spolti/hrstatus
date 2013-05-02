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

import java.util.List;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.ohsnap.hrstatus.dao.Configuration;
import br.com.ohsnap.hrstatus.dao.Iteracoes;
import br.com.ohsnap.hrstatus.dao.UsersInterface;
import br.com.ohsnap.hrstatus.model.Configurations;
import br.com.ohsnap.hrstatus.model.Servidores;
import br.com.ohsnap.hrstatus.model.Users;
import br.com.ohsnap.hrstatus.utils.UserInfo;

@Resource
public class ConfigController {

	private Result result;
	private Iteracoes iteracoesDAO;
	private Configuration configurationDAO;
	private Validator validator;
	private UsersInterface userDAO;
	UserInfo userInfo = new UserInfo();

	public ConfigController(Result result, Iteracoes iteracoesDAO,
			Configuration configurationDAO, Validator validator, UsersInterface userDAO) {
		this.result = result;
		this.iteracoesDAO = iteracoesDAO;
		this.configurationDAO = configurationDAO;
		this.validator = validator;
		this.userDAO = userDAO;
	}

	@Get("/configServer")
	public void configServer() {
		//inserindo html title no result
		result.include("title","Configurar Servidor");
		
		result.include("loggedUser", userInfo.getLoggedUsername());
		
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ]URI Called: /configServer");
		//List<Configurations> opts = this.configurationDAO.getConfigs();
		Configurations opts = this.configurationDAO.getConfigs();
		int diff = opts.getDifference();;
		String mailFrom = opts.getMailFrom();
		String subject = opts.getSubject();
		String dests = opts.getDests();
		String jndiMail = opts.getJndiMail();
		String ntpServer = opts.getNtpServer();
		boolean updateNtpIsActive = opts.isUpdateNtpIsActive();
		
		Logger.getLogger(getClass()).debug("Difference time: " + diff);
		Logger.getLogger(getClass()).debug("Sender: " + mailFrom);
		Logger.getLogger(getClass()).debug("Subject: " + subject);
		Logger.getLogger(getClass()).debug("Dests: " + dests);
		Logger.getLogger(getClass()).debug("JndiMail: " + jndiMail);
		Logger.getLogger(getClass()).debug("NtpServer: " + ntpServer);
		Logger.getLogger(getClass()).debug("updateNtpIsActive: " + updateNtpIsActive);
		
		result.include("difference", diff);
		result.include("mailFrom", mailFrom);
		result.include("subject", subject);
		result.include("dests", dests);
		result.include("jndiMail", jndiMail);
		result.include("ntpServer",ntpServer);
		
		if (updateNtpIsActive){
			result.include("updateNtpIsActive","ATIVO");
		}else{
			result.include("updateNtpIsActive","INATIVO");
		}
	}
	
	@Get("/updateConfig")
	public void updateConfig(String new_value, String campo){
		//inserindo html title no result
		result.include("title","Configurar Servidor");
		
		result.include("loggedUser", userInfo.getLoggedUsername());
		
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /updateConfig");
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] Valor recebido " + campo + " " + new_value);
		
		String regex_mail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
		Configurations config = this.configurationDAO.getConfigs();
				
		if (new_value.equals("")) {
			validator.add(new ValidationMessage(
					"Algum valor deve ser informado", "Erro"));
		} else if (campo.equals("difference")) {
			try {
				int secs = Integer.parseInt(new_value);
				config.setDifference(secs);

			} catch (Exception e) {
				validator.add(new ValidationMessage("" + e, "Erro"));
				validator.onErrorForwardTo(ConfigController.class).configServer();
			}

		} else if (campo.equals("mailFrom")) {
			
			if (!new_value.matches(regex_mail)) {
				validator.add(new ValidationMessage("E-mail " + new_value
						+ " não está correto", "Erro"));
			}else {
				config.setMailFrom(new_value);
			}
		}else if (campo.equals("subject")){
			config.setSubject(new_value);
		}else if (campo.equals("ntpServer")){
			config.setNtpServer(new_value);
		}else if (campo.equals("jndiMail")){
			config.setJndiMail(new_value);
		}else if(campo.equals("dests")){
			String [] mail_dests = new_value.split(",");
			
			for (int i = 0; i < mail_dests.length; i++){
				Logger.getLogger(getClass()).debug("Emails [ " + i + " ]: " + mail_dests[i]);
				if (!mail_dests[i].matches(regex_mail)) {
					validator.add(new ValidationMessage("E-mail " + mail_dests[i]
							+ " não está correto", "Erro"));
				}else{
					config.setDests(new_value);
				}
			}
			
		}else if(campo.equals("updateNtpIsActive")){
			if ("ATIVO".equals(new_value.toUpperCase())){
				config.setUpdateNtpIsActive(true);				
			}else if("INATIVO".equals(new_value.toUpperCase())){
				config.setUpdateNtpIsActive(false);
			}else{
				validator.add(new ValidationMessage("O campo Ativar Atualização via NTP só permite os valores ATIVO ou INATIVO", "Erro"));
			}
		}

		validator.onErrorForwardTo(ConfigController.class).configServer();
		this.configurationDAO.updateConfig(config);
		result.forwardTo(ConfigController.class).configServer();
		
	}
	
	@Get("/configClients")
	public void configClients() {
		//inserindo html title no result
		result.include("title","Configurar Clientes");
		
		result.include("loggedUser", userInfo.getLoggedUsername());
		
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /configClients");
		List<Servidores> list = this.iteracoesDAO.listServers();
		result.include("server", list);
	}

	@Get("/configUser")
	public void configUser() {
		//inserindo html title no result
		result.include("title","Configurar Usuário");
		
		result.include("loggedUser", userInfo.getLoggedUsername());
		
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /configUser");
		List<Users> list = this.userDAO.listUser();
		result.include("user",list);
	}

}
