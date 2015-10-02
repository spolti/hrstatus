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
import br.com.hrstatus.dao.SchedulerInterface;
import br.com.hrstatus.model.VerificationScheduler;
import br.com.hrstatus.utils.UserInfo;


/*
 * @author spolti
 */

@Resource
public class SchedulerController {

	Logger log =  Logger.getLogger(SchedulerController.class.getCanonicalName());
	
	@Autowired
	private Result result;
	
	private UserInfo userInfo = new UserInfo();
	
	@Autowired
	private SchedulerInterface schedulerDAO;
	
	@Get("/scheduler")
	public void scheduler () {
		
		// Inserting HTML title in the result
		result.include("title", "Agendamentos Ativos/Novo Agendamento");
		result.include("loggedUser", userInfo.getLoggedUsername());
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /scheduler");
		
		List<VerificationScheduler> scheduler = this.schedulerDAO.schedulers();
		result.include("scheduler", scheduler);
		
		
	}
	
	
	@Get("/listScheduler")
	public void listScheduler () {
		
		// Inserting HTML title in the result
		result.include("title", "Listar Agendamentos");

		result.include("loggedUser", userInfo.getLoggedUsername());

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /listScheduler");
		
		
	}
}