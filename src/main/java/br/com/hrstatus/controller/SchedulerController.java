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
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.hrstatus.dao.InstallProcessInterface;
import br.com.hrstatus.dao.SchedulerInterface;
import br.com.hrstatus.model.VerificationScheduler;
import br.com.hrstatus.model.VerificationSchedulerHistory;
import br.com.hrstatus.utils.GetSystemInformation;
import br.com.hrstatus.utils.PropertiesLoaderImpl;
import br.com.hrstatus.utils.UserInfo;


/*
 * @author spolti
 */


@SuppressWarnings("static-access")
@Resource
public class SchedulerController {

	Logger log =  Logger.getLogger(SchedulerController.class.getCanonicalName());
	
	@Autowired
	private Result result;
	
	private UserInfo userInfo = new UserInfo();
	private VerificationScheduler scheduler = new VerificationScheduler();
	
	@Autowired
	private SchedulerInterface schedulerDAO;
	
	@Autowired
	private InstallProcessInterface ipi;
	
	private GetSystemInformation getSys = new GetSystemInformation();
	
	@Get("/scheduler")
	public void scheduler () {
		
		//Sending information to the "About" page
		PropertiesLoaderImpl load = new PropertiesLoaderImpl();
		String version = load.getValor("version");
		result.include("version", version);
		List<String> info = getSys.SystemInformation();
		result.include("jvmName", info.get(2));
		result.include("jvmVendor",info.get(1));
		result.include("jvmVersion",info.get(0));
		result.include("osInfo",info.get(3));
		result.include("installDate", ipi.getInstallationDate());
		
		// Inserting HTML title in the result
		result.include("title", "Agendamentos Ativos/Novo Agendamento");
		result.include("loggedUser", userInfo.getLoggedUsername());
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /scheduler");
		
		List<VerificationScheduler> scheduler = this.schedulerDAO.schedulers();
		result.include("scheduler", scheduler);
		
	}
	
	
	@Get("/listScheduler")
	public void listScheduler () {
		
		//Sending information to the "About" page
		PropertiesLoaderImpl load = new PropertiesLoaderImpl();
		String version = load.getValor("version");
		result.include("version", version);
		List<String> info = getSys.SystemInformation();
		result.include("jvmName", info.get(2));
		result.include("jvmVendor",info.get(1));
		result.include("jvmVersion",info.get(0));
		result.include("osInfo",info.get(3));
		result.include("installDate", ipi.getInstallationDate());
		
		// Inserting HTML title in the result
		result.include("title", "Histórico de Agendamentos");
		result.include("loggedUser", userInfo.getLoggedUsername());

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /listScheduler");
		
		List<VerificationSchedulerHistory> schedulerHistory = this.schedulerDAO.getSchedulerHistory(userInfo.getLoggedUsername());
		result.include("schedulerHistory", schedulerHistory);
		
	}
	
	@Get("/findForUpdateScheduler/{schedulerId}")
	public void findForUpdateScheduler (int schedulerId) {
		
		//Sending information to the "About" page
		PropertiesLoaderImpl load = new PropertiesLoaderImpl();
		String version = load.getValor("version");
		result.include("version", version);
		List<String> info = getSys.SystemInformation();
		result.include("jvmName", info.get(2));
		result.include("jvmVendor",info.get(1));
		result.include("jvmVersion",info.get(0));
		result.include("osInfo",info.get(3));
		result.include("installDate", ipi.getInstallationDate());
		
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /findForUpdateScheduler/" + schedulerId);
		
		// Inserting HTML title in the result
		result.include("title", "Alterar Agendamento");
		result.include("loggedUser", userInfo.getLoggedUsername());
		
		scheduler = this.schedulerDAO.getSchedulerByID(schedulerId);
		
		if (scheduler.isDefaultScheduler()) {
			result.include("isDefault", "checked");
		} else {
			result.include("isNotDefault", "checked");
		}
		
		if (scheduler.isEveryday()) {
			result.include("isEveryday", "checked");
		} else {
			result.include("isNotEveryday", "checked");
		}
		
		if (scheduler.isEnabled()) {
			result.include("isEnabled", "checked");
		} else {
			result.include("isNotEnabled", "checked");
		}
		
		result.include("scheduler", scheduler);
		
	}
	
	@Post("/updateScheduler")
	public void updateScheduler (VerificationScheduler scheduler, String isDefaultScheduler, String isEveryday, String isEnabled) {
		
		//Sending information to the "About" page
		String version = PropertiesLoaderImpl.getValor("version");
		result.include("version", version);
		List<String> info = getSys.SystemInformation();
		result.include("jvmName", info.get(2));
		result.include("jvmVendor",info.get(1));
		result.include("jvmVersion",info.get(0));
		result.include("osInfo",info.get(3));
		result.include("installDate", ipi.getInstallationDate());
		
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /updateScheduler");
		// Inserting HTML title in the result
		result.include("title", "Atualizar Agendamento");
		// Inserting the Logged username in the home page
		result.include("loggedUser", userInfo.getLoggedUsername());
		
		//for the default scheduler the two following will be always true
		scheduler.setDefaultScheduler(true);
		scheduler.setEveryday(true);

		
		if (isEnabled.equals("true")) {
			scheduler.setEnabled(true);
		} else {
			scheduler.setEnabled(false);
		}		
		
		log.info("valores do scheduler, ID: " + scheduler.getSchedulerId());
		log.info("SchedulerName: " + scheduler.getSchedulerName());
		log.info("isDefaultScheduler: " + scheduler.isDefaultScheduler());
		log.info("isEveryday: " + scheduler.isEveryday());
		log.info("isEnabled: " + scheduler.isEnabled());

		this.schedulerDAO.updateScheduler(scheduler);
		
		result.forwardTo(SchedulerController.class).scheduler();
	}
	
}