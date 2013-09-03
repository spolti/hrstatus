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

import java.util.List;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.hrstatus.dao.LockIntrface;
import br.com.hrstatus.model.Lock;
import br.com.hrstatus.utils.UserInfo;

@Resource
public class LockController {

	private Result result;
	private LockIntrface lockDAO;
	UserInfo userInfo = new UserInfo();
	
	public LockController (Result result, LockIntrface lockDAO ){
		this.result = result;
		this.lockDAO = lockDAO;
	}
	
	@Get("/listLocks")
	public void listLocks (){
		
		// inserindo html title no result
		result.include("title", "Locked Resources");
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /listLocks");
		result.include("loggedUser", userInfo.getLoggedUsername());
		List<Lock> listLocks = this.lockDAO.ListAllLocks();
		result.include("locks", listLocks);
		
	}
}
