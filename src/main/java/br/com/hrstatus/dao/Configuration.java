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

package br.com.hrstatus.dao;

import br.com.hrstatus.model.Configurations;

/*
 * @author spolti
 */

public interface Configuration {

	public void updateConfig(Configurations config);
	
	public void saveConfigNotLogged(Configurations config);
	
	public Configurations getConfigs();
	
	public String getMailSender();
	
	public String getMailSenderNotLogged();
	
	public String getSubject();
	
	public String getSubjectNotLogged();
	
	public String getDests();
	
	public String getDestsNotLogged();
	
	public String getJndiMail();
	
	public String getJndiMailNotLogged();
	
	public int getDiffirenceSecs();
	
	public int getDiffirenceSecsScheduler(String schedulerName);
	
	public String getNtpServerAddress();
	
	public String getNtpServerAddressNotLogged();
	
	public boolean sendNotification();
}