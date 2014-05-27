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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.model.Configurations;
import br.com.hrstatus.security.CriptoJbossDSPassword;
import br.com.hrstatus.utils.MailSender;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Resource
public class UtilsController {

	private Result result;
	private Configuration configurationDAO;
	UserInfo userInfo = new UserInfo();
	MailSender send = new MailSender();

	public UtilsController(Result result, Configuration configurationDAO) {
		this.result = result;
		this.configurationDAO = configurationDAO;
	}
	
	@SuppressWarnings("static-access")
	@Get("/utils/criptPassJboss/{password}")
	public void criptPassJboss (String password) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException{
		Logger.getLogger(getClass()).info("[ Not Logged ] URI Called: /utils/criptPassJboss/");
		
		CriptoJbossDSPassword cript = new CriptoJbossDSPassword();
		result.include("EncriptedPassword",cript.encode(password));
		
	}
	
	@Get("/sendMailtest")
	public void sendMailtest(String rcpt){
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /sendMailtest/" + rcpt);
		
		Configurations configs = this.configurationDAO.getConfigs();
		try {
			String resultMail = send.sendTestMail(configs.getMailFrom(), rcpt, configs.getJndiMail());
			result.include("info",resultMail).forwardTo(ConfigController.class).configServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.getLogger(getClass()).error("[ " + userInfo.getLoggedUsername() + " ] Erro ao enviar email de teste: " + e);
			result.include("errors","Erro ao enviar mensagem de teste: " + e).forwardTo(ConfigController.class).configServer();
			
		} 
	}
}