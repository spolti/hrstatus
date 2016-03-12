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

package br.com.hrstatus.verification.os;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.hrstatus.controller.HomeController;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.verification.helper.VerificationHelper;
import br.com.hrstatus.verification.impl.VerificationImpl;
import com.jcraft.jsch.JSchException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/*
 * @author spolti
 */

@Resource
public class VerifySingleServer extends VerificationHelper {

	protected final Logger log = Logger.getLogger(getClass().getName());
	
	@Autowired
	private Result result;
	@Autowired
	private VerificationImpl verification;
	
	@Get("/singleServerToVerify/{id}")
	public void singleServerToVerify(int id) throws JSchException, IOException {
		
		// Inserting HTML title in the result
		result.include("title", "Home");
		result.include("loggedUser", userInfo.getLoggedUsername());
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /singleServerToVerify/" + id);

		verification.serverVerification(this.serversDAO.listServerByID(id));

		List<Servidores> list = this.serversDAO.listServerByID(id);
		result.include("class", "activeServer");
		result.include("server", list).forwardTo(HomeController.class).home("");
	}
}