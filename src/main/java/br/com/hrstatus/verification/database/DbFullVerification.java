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
import br.com.hrstatus.model.Lock;
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
	private UserInfo userInfo = new UserInfo();
	private Lock lockedResource = new Lock();


	@Get("/database/startDataBaseVerification/fullDBVerification")
	public void startFullDataBaseVerification() throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {

		// inserindo html title no result
		result.include("title", "Hr Status Home");
		String LoggedUsername = userInfo.getLoggedUsername();
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI called: /database/startDataBaseVerification/fullDBVerification");

		// Verifica se já tem alguma verificação ocorrendo...
		log.info("[ " + userInfo.getLoggedUsername() + " ] Initializing a fullDBVerification verification.");
		
		lockedResource.setRecurso("fullDBVerification");
		lockedResource.setUsername(LoggedUsername);
		List<Lock> lockList = this.lockDAO.listLockedServices("fullDBVerification");
		if (lockList.size() != 0) {
			for (Lock lock : lockList) {
				log.info("[ " + userInfo.getLoggedUsername() + " ] The resource fullDBVerification is locked by the user " + lock.getUsername());
				result.include("class", "activeBanco");
				result.include("info","O recurso fullDBVerification está locado pelo usuário " + lock.getUsername()
								+ ", aguarde o término da mesma").forwardTo(HomeController.class).home("");

			}
		} else {
			
			log.info("[ " + userInfo.getLoggedUsername() + " ] O recurso fullDBVerification não está locado, locando e proseguindo");
			// locking resource
			lockDAO.insertLock(lockedResource);


			dbVerification.performFullVerification();
			List<BancoDados> listdb =  this.dbDAO.listDataBases();
			result.include("class", "activeBanco");
			result.include("bancoDados",listdb).forwardTo(HomeController.class).home("");
	
		}
		lockDAO.removeLock(lockedResource);
	}
}