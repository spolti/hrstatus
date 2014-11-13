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

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.utils.PropertiesLoaderImpl;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Resource
public class HomeController {

	Logger log =  Logger.getLogger(HomeController.class.getCanonicalName());
	
	@Autowired
	private Result result;
	@Autowired
	private ServersInterface iteracoesDAO;
	@Autowired
	private Validator validator;
	@Autowired
	private UsersInterface userDAO;
	private UserInfo userInfo = new UserInfo();
	
	@Get("/home")
	public void home(String verification) {
		// inserindo html title no result
		result.include("title", "Hr Status Home");

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /home");

		result.include("loggedUser", userInfo.getLoggedUsername());

		// ///////////////////////////////////////////////////////
		// Verificando se é o primeiro login do usuário após troca de senha ou
		// do cadastro
		// se for false não faz nade se for true redireciona para atualizar
		// cadastro.
		boolean isFirstLogin = this.userDAO.getFirstLogin(userInfo.getLoggedUsername());
		if (isFirstLogin) {
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Primeiro login do usuário "
							+ userInfo.getLoggedUsername() + ": " + isFirstLogin);
			
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Redirecionando o usuário para troca de senha.");
			result.forwardTo(UpdateController.class).findForUpdateUser(null,userInfo.getLoggedUsername(), "changePass");
		} else {
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Primeiro login do usuário " + userInfo.getLoggedUsername() 
							+ ": " + isFirstLogin);
		}
	}

	@SuppressWarnings("static-access")
	@Get("/navbar")
	public void navbar() {
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /navbar");
		PropertiesLoaderImpl load = new PropertiesLoaderImpl();
		String version = load.getValor("version");
		result.include("version", version);
	}

	@Get("/home/showByStatus/{status}")
	public void showByStatus(String status) {
		// inserindo html title no result
		result.include("title", "Hr Status Home");

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /home/showByStatus/" + status);

		if (status.equals("OK")) {
			List<Servidores> list = this.iteracoesDAO.getServersOK();
			result.include("class", "activeServer");
			result.include("server", list).forwardTo(HomeController.class).home("");
			result.include("class", "activeServer");

		} else if (!status.equals("OK")) {
			List<Servidores> list = this.iteracoesDAO.getServersNOK();
			result.include("class", "activeServer");
			result.include("server", list).forwardTo(HomeController.class).home("");

		} else {
			validator.onErrorUsePageOf(HomeController.class).home("");
		}
	}
}