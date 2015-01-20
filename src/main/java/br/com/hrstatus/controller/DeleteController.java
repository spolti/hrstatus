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

import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.dao.LockIntrface;
import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.model.Lock;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.model.Users;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Resource
public class DeleteController {

	Logger log =  Logger.getLogger(DeleteController.class.getCanonicalName());
	
	@Autowired
	private Result result;
	@Autowired
	private ServersInterface iteracoesDAO;
	@Autowired
	private UsersInterface usersDAO;
	@Autowired
	private BancoDadosInterface BancoDadosDAO;
	@Autowired
	private LockIntrface lockDAO;
	UserInfo userInfo = new UserInfo();

	
	@Delete("/deleteServerByID")
	public void deleteServerByID(String id_server) {

		// Inserting HTML title in the result
		result.include("title","Deletar Servidor");

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /deleteServerByID");
		int id = Integer.parseInt(id_server);
		Servidores server = this.iteracoesDAO.getServerByID(id);
		
		// Setting server ID to delete
		server.setId(id);
		this.iteracoesDAO.deleteServerByID(server);
		result.redirectTo(HomeController.class).home("");
	}
	
	@Delete("/deleteDataBaseByID")
	public void deleteDataBaseByID(String id_dataBase) {

		// Inserting HTML title in the result
		result.include("title","Deletar Banco de Dados");
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /deleteDataBaseByID");
		
		// Setting database ID to delete
		int id = Integer.parseInt(id_dataBase);
		BancoDados dataBase = this.BancoDadosDAO.getDataBaseByID(id);
		this.BancoDadosDAO.deleteDataBase(dataBase);
		result.redirectTo(HomeController.class).home("");
	}

	@Delete("/deleteUserByID")
	public void deleteUserByID(String username) {

		// Inserting HTML title in the result
		result.include("title","Deletar Usuário");
		
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /deleteUserByID");
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Received user  to be deleted: " + username);
		
		Users user = this.usersDAO.getUserByID(username);
		
		if (user.getUsername().equals(userInfo.getLoggedUsername())){
			result.include("Error", "Você não pode deletar você mesmo.");
			result.redirectTo(ConfigController.class).configUser();
		}else{
			// Setting the username to be deleted
			user.setUsername(username);
			this.usersDAO.deleteUserByID(user);
			result.redirectTo(HomeController.class).home("");
		}
	}
	
	@Delete("/deleteLockByID")
	public void deleteLockByID(String id_lock){
		
		// Inserting HTML title in the result
		result.include("title","Deletar Lock");
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /deleteLockByID");
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Lock id to be deleted: " + id_lock);
		Lock lock = this.lockDAO.getLockByID(Integer.parseInt(id_lock));
		this.lockDAO.removeLock(lock);
		result.redirectTo(HomeController.class).home("");
	}
}