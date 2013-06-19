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

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.hrstatus.action.VerifySingleServer;
import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.dao.Iteracoes;
import br.com.hrstatus.dao.LockIntrface;
import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Resource
public class ChartsController {
	
	private Result result;
	private Iteracoes iteracoesDAO;
	private Configuration configurationDAO;
	private Validator validator;
	private LockIntrface lockDAO;
	private UsersInterface userDAO;
	private VerifySingleServer runVerify;
	UserInfo userInfo = new UserInfo();

	public ChartsController(Result result, Iteracoes iteracoesDAO,
			Configuration configurationDAO, Validator validator,
			LockIntrface lockDAO, UsersInterface userDAO, VerifySingleServer runVerify) {
		this.result = result;
		this.iteracoesDAO = iteracoesDAO;
		this.configurationDAO = configurationDAO;
		this.validator = validator;
		this.lockDAO = lockDAO;
		this.userDAO = userDAO;
		this.runVerify = runVerify;
	}

	@Get("/charts/servers/consolidated")
	public void chartServidor(){
	// inserindo html title no result
			result.include("title", "Gráficos - Servidores");

			Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /charts/servers/consolidated");

			result.include("loggedUser", userInfo.getLoggedUsername());
			// ///////////////////////////////////////
			// Sending SERVERS % to plot SO's graph
			int linux = this.iteracoesDAO.countLinux();
			int windows = this.iteracoesDAO.countWindows();
			int unix = this.iteracoesDAO.countUnix();
			int other = this.iteracoesDAO.countOther();
			int total = this.iteracoesDAO.countAllServers();
			if (linux > 0 && windows > 0 && unix > 0) {
				result.include("linux", (linux * 100) / total);
				result.include("windows", (windows * 100) / total);
				result.include("unix", (unix * 100) / total);
				result.include("other", (other * 100) / total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] linux: " + linux);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Windows: " + windows);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Unix: " + unix);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Other: " + other);

			} else if (linux > 0) {
				result.include("linux", (linux * 100) / total);
				result.include("windows", 0);
				result.include("unix", 0);
				result.include("other", 0);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] linux: " + linux);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Windows: " + windows);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Unix: " + unix);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Other: " + other);

			} else if (windows > 0) {
				result.include("linux", 0);
				result.include("windows", (windows * 100) / total);
				result.include("unix", 0);
				result.include("other", 0);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] linux: " + linux);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Windows: " + windows);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Unix: " + unix);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Other: " + other);

			} else if (unix > 0) {
				result.include("linux", 0);
				result.include("windows", 0);
				result.include("unix", (unix * 100) / total);
				result.include("other", 0);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] linux: " + linux);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Windows: " + windows);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Unix: " + unix);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Other: " + other);

			} else if (other > 0) {
				result.include("linux", 0);
				result.include("windows", 0);
				result.include("unix", 0);
				result.include("other", (other * 100) / total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Total: " + total);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] linux: " + linux);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Windows: " + windows);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Unix: " + unix);
				Logger.getLogger(getClass()).debug("[ " + userInfo.getLoggedUsername() + " ] Other: " + other);
			}

			// Populating 2° graph (servers ok and not ok)
			result.include("serversOK", this.iteracoesDAO.countServersOK());
			result.include("serversNOK", this.iteracoesDAO.countServersNOK());

			// Ploting 3° graph.
			result.include("serversLinuxOK", this.iteracoesDAO.countLinuxOK());
			result.include("serversLinuxNOK", this.iteracoesDAO.countLinuxNOK());

			result.include("serversUnixOK", this.iteracoesDAO.countUnixOK());
			result.include("serversUnixNOK", this.iteracoesDAO.countUnixNOK());

			result.include("serversWindowsOK", this.iteracoesDAO.countWindowsOK());
			result.include("serversWindowsNOK", this.iteracoesDAO.countWindowsNOK());

			result.include("otherOK", this.iteracoesDAO.countOtherOK());
			result.include("otherNOK", this.iteracoesDAO.countOtherNOK());

			result.include("totalServer",total);
	}
}
