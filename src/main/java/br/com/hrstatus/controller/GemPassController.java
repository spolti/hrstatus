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

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.model.PassExpire;
import br.com.hrstatus.model.Users;
import br.com.hrstatus.security.SpringEncoder;
import br.com.hrstatus.utils.DateParser;
import br.com.hrstatus.utils.DateUtils;
import br.com.hrstatus.utils.MailSender;
import br.com.hrstatus.utils.PassGenerator;
import br.com.hrstatus.utils.UserInfo;

@Resource
public class GemPassController {

	private UsersInterface userDAO;
	private Configuration configurationDAO;
	private Result result;
	UserInfo userInfo = new UserInfo();

	public GemPassController(Result result, Configuration configurationDAO, UsersInterface userDAO) {
		this.result = result;
		this.userDAO = userDAO;
		this.configurationDAO = configurationDAO;
	}

	@Get("/atualizarCadastro/{username}")
	public void atualizarCadastro(String username) {
		//inserindo html title no result
		result.include("title","Atualizar Usuário");
		
		result.include("loggedUser", userInfo.getLoggedUsername());

		Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Usuário logado: " + userInfo.getLoggedUsername());

		if (!username.equals(userInfo.getLoggedUsername().toString())) {
			result.use(Results.http()).sendError(403);
		} else {
			Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] validação de usuário OK");
			result.redirectTo(UpdateController.class).findForUpdateUser(null, userInfo.getLoggedUsername(),"changePass");
		}

	}

	@Post("/requestNewPass")
	public void requestNewPass(String username) {
		
		Logger.getLogger(getClass()).info("[ Not Logged ] URI Called: /requestNewPass");
		Logger.getLogger(getClass()).info("[ Not Logged ] Username recebido: " + username);
		SpringEncoder encode = new SpringEncoder();
		String password = "";
		PassExpire passExpire = new PassExpire();
		try {
			// Verificando se o usuário existe
			if (this.userDAO.searchUser(username) == 1) {
				Users user = this.userDAO.getUserByID(username);
				Logger.getLogger(getClass()).info("usuário encontrado");
				PassGenerator gemPass = new PassGenerator();
				String mailSender = this.configurationDAO.getMailSender();
				String jndiMail = this.configurationDAO.getJndiMail();
				String dest = this.userDAO.getMail(username);
				MailSender send = new MailSender();
				password = gemPass.gemPass();
				
				// Definindo a expiração da nova senha e armazenando a senha
				// antiga na base temporária
				// Setando senha antiga e usuário
				passExpire.setUsername(username);

				String oldPwd = user.getPassword();
				passExpire.setOldPwd(oldPwd);

				// Obtendo a hora e calculando a tempo de expiração
				DateUtils dateUtils = new DateUtils();
				DateParser parse = new DateParser();
//				Date changeTime = dateUtils.dateConverter(
//						dateUtils.getTime(), "LINUX",null); // hora completa
//																// atual
				Date changeTime = parse.parser(dateUtils.getTime());
				
				passExpire.setChangeTime(changeTime.toString());

				// calculando a soma
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(changeTime);
				calendar.add(Calendar.MINUTE, 30);

				// Atribuindo a soma para a variável.
				Date expireTime = calendar.getTime();
				passExpire.setExpireTime(expireTime.toString());

				// Salvando nova senha no banco
				String newPwd = encode.encodePassUser(password);
				user.setPassword(newPwd);

				passExpire.setNewPwd(newPwd);

				if (this.userDAO.searchUserChangePass(username) != 1) {
					user.setFirstLogin(true);
					this.userDAO.updateUser(user);
					this.userDAO.setExpirePasswordTime(passExpire);
					send.sendNewPass(mailSender, dest, jndiMail, password);

					result.redirectTo(LoginController.class)
							.login("Se o usuário for válido uma nova senha será enviada para seu e-mail.");
				} else {
					result.redirectTo(LoginController.class)
							.login("Já foi solicitado uma troca de senha para este usuário, por favor cheque seu e-mail.");
				}

			} else {
				Logger.getLogger(getClass()).info("Usuário não encontrado");
				result.redirectTo(LoginController.class)
				.login("Se o usuário for válido uma nova senha será enviada para seu e-mail.");
			}

		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e);
		}
	}
}
