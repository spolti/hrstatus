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

package br.com.ohsnap.hrstatus.scheduler;

/*
 * @author spolti
 */

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.JSchException;

import br.com.ohsnap.hrstatus.dao.UsersInterface;
import br.com.ohsnap.hrstatus.model.PassExpire;
import br.com.ohsnap.hrstatus.model.Users;
import br.com.ohsnap.hrstatus.utils.DateUtils;

@Service
public class PassExpireScheduler {

	@Autowired
	private UsersInterface userDAO;

	public PassExpireScheduler() {
	}

	@Scheduled(cron = "0 0/5 * * * *" ) //de 5 em 5 minutos
	public void passExpire() throws ParseException, JSchException, IOException {
		Logger.getLogger(getClass()).info("Invoking passExpire at " + new Date());
		
		List<PassExpire> list = this.userDAO.getExpireTime();
		
		DateUtils time = new DateUtils();
		Date timeNow = time.dateConverter(time.getTime("LINUX"), "LINUX",null);
		
		
		for (PassExpire passExpire : list){
			if (timeNow.compareTo(time.dateConverter(passExpire.getExpireTime(),"LINUX",null)) > 0){
				Logger.getLogger(getClass()).info("Password gerado para o usuário " + passExpire.getUsername() + "expirou, aplicando senha antiga novamente.");
				Users user = this.userDAO.getUserByID(passExpire.getUsername());
				
				user.setPassword(passExpire.getOldPwd());
				user.setFirstLogin(false);
				//Salvando novo usuário.
				this.userDAO.updateUser(user);
				
				//Removendo usuário da tabela temporária
				this.userDAO.delUserExpireTime(passExpire);
				
			}else{
				Logger.getLogger(getClass()).info("Password gerado para o usuário " + passExpire.getUsername() + " ainda não expirou.");
			}

		}

	}

}