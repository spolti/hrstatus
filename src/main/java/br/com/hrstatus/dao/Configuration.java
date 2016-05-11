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

    void updateConfig(Configurations config);

    void saveConfigNotLogged(Configurations config);

    Configurations getConfigs();

    String getMailSender();

    String getMailSenderNotLogged();

    String getSubject();

    String getSubjectNotLogged();

    String getDests();

    String getDestsNotLogged();

    String getJndiMail();

    String getJndiMailNotLogged();

    int getDiffirenceSecs();

    int getDiffirenceSecsScheduler(String schedulerName);

    String getNtpServerAddress();

    String getNtpServerAddressNotLogged();

    boolean sendNotification();
}