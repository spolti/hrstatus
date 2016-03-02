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
import java.sql.SQLException;
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
import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.utils.UserInfo;
import br.com.hrstatus.utils.date.DateUtils;
import br.com.hrstatus.verification.impl.VerifySingleDBImpl;

/*
 * @author spolti
 */

@Resource
public class VerifySingleDB {

	Logger log = Logger.getLogger(VerifySingleDB.class.getCanonicalName());

	@Autowired
	private Result result;
	@Autowired
	private BancoDadosInterface dbDAO;
	@Autowired
	private Configuration configurationDAO;
	@Autowired
	private Validator validator;
	@Autowired
	private VerifySingleDBImpl singleVerification;
	UserInfo userInfo = new UserInfo();
	DateUtils dt = new DateUtils();


	@Get("/database/verifySingleDB/{id}")
	public void verifySingleDB(int id) throws ClassNotFoundException, SQLException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, IllegalVendorException {


		// Inserting HTML title in the result
		result.include("title", "Hr Status Home");
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI called: /database/verifySingleDB");
		log.info("[ " + userInfo.getLoggedUsername() + " ] Initializing a verifySingleDB verification.");

		singleVerification.performSingleDbVerification(id);
		List<BancoDados> bancoDados = this.dbDAO.listDataBaseByID(id);
		result.include("class", "activeBanco");
		result.include("bancoDados", bancoDados).forwardTo(HomeController.class).home("");
	}
}