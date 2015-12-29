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
import br.com.hrstatus.dao.InstallProcessInterface;
import br.com.hrstatus.dao.LockIntrface;
import br.com.hrstatus.model.Lock;
import br.com.hrstatus.utils.GetSystemInformation;
import br.com.hrstatus.utils.PropertiesLoaderImpl;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Resource
public class LockController {

	Logger log =  Logger.getLogger(LockController.class.getCanonicalName());
	
	@Autowired
	private Result result;
	@Autowired
	private LockIntrface lockDAO;
	@Autowired
	private InstallProcessInterface ipi;
	private UserInfo userInfo = new UserInfo();
	private GetSystemInformation getSys = new GetSystemInformation();
	List<Lock> listLocks;
	
	@SuppressWarnings("static-access")
	@Get("/listLocks")
	public void listLocks (){
		
		//Sending information to "About" page
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
		result.include("title", "Locked Resources");
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /listLocks");
		result.include("loggedUser", userInfo.getLoggedUsername());
		listLocks = this.lockDAO.ListAllLocks();
		result.include("locks", listLocks);	
	}
}