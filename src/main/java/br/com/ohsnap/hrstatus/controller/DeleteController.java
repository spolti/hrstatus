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

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.ohsnap.hrstatus.dao.Iteracoes;
import br.com.ohsnap.hrstatus.dao.UsersInterface;
import br.com.ohsnap.hrstatus.model.Servidores;
import br.com.ohsnap.hrstatus.model.Users;

@Resource
public class DeleteController {

	private Result result;
	private Iteracoes iteracoesDAO;
	private UsersInterface usersDAO;

	public DeleteController(Result result, Iteracoes iteracoesDAO, UsersInterface usersDAO) {
		this.result = result;
		this.iteracoesDAO = iteracoesDAO;
		this.usersDAO = usersDAO;
	}

	@Delete("/deleteServerByID")
	public void deleteServerByID(String id_server) {
		//inserindo html title no result
		result.include("title","Deletar Servidor");

		Logger.getLogger(getClass()).info("URI Called: /deleteServerByID");
		int id = Integer.parseInt(id_server);
		Servidores server = this.iteracoesDAO.getServerByID(id);

		// Setando ID
		server.setId(id);

		this.iteracoesDAO.deleteServerByID(server);

		result.redirectTo(HomeController.class).home("");
	}

	@Delete("/deleteUserByID")
	public void deleteUserByID(String username) {
		//inserindo html title no result
		result.include("title","Deletar Usuário");
		
		Logger.getLogger(getClass()).info("URI Called: /deleteUserByID");
		Logger.getLogger(getClass()).debug("Usuário recebido para deleção: " + username);
		
		Users user = this.usersDAO.getUserByID(username);
		
		//Setando username
		user.setUsername(username);
		
		this.usersDAO.deleteUserByID(user);
		
		result.redirectTo(HomeController.class).home("");
		
	}
	
}
