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

package br.com.ohsnap.hrstatus.controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.ohsnap.hrstatus.security.CriptoJbossDSPassword;
import br.com.ohsnap.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Resource
public class UtilsController {

	private Result result;
	UserInfo userInfo = new UserInfo();

	public UtilsController(Result result) {
		this.result = result;
	}
	
	@Get("/utils/criptPassJboss/{password}")
	public void criptPassJboss (String password) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException{
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /utils/criptPassJboss/");
		
		CriptoJbossDSPassword cript = new CriptoJbossDSPassword();
		result.include("EncriptedPassword",cript.encode(password));
		
	}
	
}
