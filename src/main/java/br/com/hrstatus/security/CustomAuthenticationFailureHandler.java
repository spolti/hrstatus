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

package br.com.hrstatus.security;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.dao.InstallProcessInterface;
import br.com.hrstatus.dao.SchedulerInterface;
import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.model.Configurations;
import br.com.hrstatus.model.InstallationProcess;
import br.com.hrstatus.model.Users;
import br.com.hrstatus.model.VerificationScheduler;
import br.com.hrstatus.utils.date.DateUtils;

/*
 *Spring Framework 
 *Customization, rewrite LoginFailureHandler
 */

@Service
public class CustomAuthenticationFailureHandler extends
		SimpleUrlAuthenticationFailureHandler {

	Logger log = Logger.getLogger(CustomAuthenticationFailureHandler.class.getCanonicalName());

	@Autowired
	private InstallProcessInterface ipiDAO;
	
	@Autowired
	private Configuration confDAO;
	
	@Autowired
	private UsersInterface userDAO;
	
	@Autowired
	private SchedulerInterface schedulerDAO;

	InstallationProcess ipi = new InstallationProcess();
	Configurations conf = new Configurations();
	VerificationScheduler scheduler = new VerificationScheduler();
	Users user = new Users();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,	HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {

		if (request.getParameter("j_username").equals("admin") && ipiDAO.freshInstall()) {

			log.fine("[ System ] Fresh Installation, configuring HrStatus settings.");
			log.fine("[ System ] Setting Installation Process parameters");
			ipi.setFreshInstall(false);
			ipi.setInstallDate(new DateUtils().getTime().toString());

			conf.setDests("example@hrstatus.com.br");
			conf.setDifference(50);
			conf.setJndiMail("java:jboss/mail/HrStatus");
			conf.setMailFrom("hrstatus@hrstatus.com.br");
			conf.setNtpServer("a.st1.ntp.br");
			conf.setSendNotification(true);
			conf.setSubject("NO REPLY - Status Horario de Verao");
			conf.setUpdateNtpIsActive(false);

			user.setAuthority("ROLE_ADMIN");
			user.setEnabled(true);
			user.setFirstLogin(true);
			user.setMail("admin@example.com");
			user.setNome("Administrador do Sistema");
			user.setPassword("89794b621a313bb59eed0d9f0f4e8205");
			user.setUsername("admin");

			//setting the default scheduler
			scheduler.setSchedulerName("defaultScheduler");
			scheduler.setEveryday(true);
			scheduler.setDefaultScheduler(true);
			scheduler.setEnabled(true);
			
			userDAO.saveORupdateUserNotLogged(user);
			confDAO.saveConfigNotLogged(conf);
			ipiDAO.saveInstallationProcess(ipi);
			schedulerDAO.saveSchedulerNotLogged(scheduler);
			
			setDefaultFailureUrl("/login?login_error=1&message=O Hrstatus foi instalado com sucesso, para login utilize as credenciais admin/123mudar");
			super.onAuthenticationFailure(request, response, exception);
			
		} else {

			setDefaultFailureUrl("/login?login_error=1");
			log.fine("[ System ] Login Failed for user: " + request.getParameter("j_username"));
			super.onAuthenticationFailure(request, response, exception);
		}
	}
}