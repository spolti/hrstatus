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

package br.com.hrstatus.verification.database;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import br.com.hrstatus.action.databases.helper.IllegalVendorException;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.hrstatus.controller.HomeController;
import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.dao.LockIntrface;
import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.resrources.ResourcesManagement;
import br.com.hrstatus.utils.UserInfo;
import br.com.hrstatus.verification.impl.DbFullVerificationImpl;

/*
 * @author spolti
 */

@Resource
public class DbFullVerification {

	Logger log =  Logger.getLogger(DbFullVerification.class.getCanonicalName());
	
	@Autowired
	private Result result;
	@Autowired
	private LockIntrface lockDAO;
	@Autowired
	private BancoDadosInterface dbDAO;
	@Autowired
	private Configuration configurationDAO;
	@Autowired
	private Validator validator;
	@Autowired
	DbFullVerificationImpl dbVerification;
	@Autowired
	ResourcesManagement resource;
	private UserInfo userInfo = new UserInfo();


	@Get("/database/startDataBaseVerification/fullDBVerification")
	public void startFullDataBaseVerification() throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, IllegalVendorException {

		// inserindo html title no result
		result.include("title", "Hr Status Home");
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI called: /database/startDataBaseVerification/fullDBVerification");

		log.info("[ " + userInfo.getLoggedUsername() + " ] Initializing a fullDBVerification verification.");
		
		// Verifica se já tem alguma verificação ocorrendo...
		
		if (!resource.islocked("fullDbVerification")) {
			resource.lockRecurso("fullDbVerification");
			dbVerification.performFullVerification();
			List<BancoDados> listdb =  this.dbDAO.listDataBases();
			result.include("class", "activeBanco");
			result.include("bancoDados",listdb).forwardTo(HomeController.class).home("");
			resource.releaseLock("fullDbVerification");
			
		} else {
			result.include("class", "activeBanco");
			result.include("info","O recurso fullDBVerification está locado, aguarde o término da mesma").forwardTo(HomeController.class).home("");
		}

	}
}