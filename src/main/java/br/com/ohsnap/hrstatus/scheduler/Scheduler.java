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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.ohsnap.hrstatus.dao.Configuration;
import br.com.ohsnap.hrstatus.dao.Iteracoes;
import br.com.ohsnap.hrstatus.model.Configurations;
import br.com.ohsnap.hrstatus.utils.MailSender;

@Service
public class Scheduler {

	@Autowired
	private Iteracoes iteracoesDAO;
	@Autowired
	private Configuration configurationDAO;

	public Scheduler() {
	}

	@Scheduled(cron = "${br.com.ohsnap.hrstatus.scheduler.MailScheduler.cron}")
	public void sendMail() {
		Logger.getLogger(getClass())
				.debug("Invoking sendMail at " + new Date());

		int count = this.iteracoesDAO.countServersNOK();
		if (count > 0) {
			Logger.getLogger(getClass())
					.debug("Foram encontrados "
							+ count
							+ " servidor(es) desatualizado(s), enviando e-mail de alerta.");
			// Envio de e-mail
			String mail1 = this.configurationDAO.getMailSender();
			String Subject = this.configurationDAO.getSubject();
			String Dests[] = this.configurationDAO.getDests().split(",");
			String jndiMail = this.configurationDAO.getJndiMail();
			MailSender mail = new MailSender();
			mail.Sender(mail1, Subject, Dests, jndiMail);
		} else {
			Logger.getLogger(getClass()).info(
					"Nenhum servidor desatualizado foi encontrado.");
		}

	}

	@Scheduled(cron = "${br.com.ohsnap.hrstatus.scheduler.NtpScheduler.cron}")
	public void NtpUpdater() throws IOException {

		Configurations config = this.configurationDAO.getConfigs();
		Boolean isUpdateNtpIsActive = config.isUpdateNtpIsActive();
		
		Process p = (Runtime.getRuntime().exec("which ntpdate"));
		String stdIn = "", s = "";
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		while ((s = stdInput.readLine()) != null) {
			stdIn += s + "\n";
		}

		if (stdIn.equals("")) {
			Logger.getLogger(getClass())
					.error("Comando ntpdate não encontrado, abortando atualização automática");
		} else {
			Logger.getLogger(getClass()).info("ntp ativo: " + isUpdateNtpIsActive);
			if (isUpdateNtpIsActive){
				Logger.getLogger(getClass()).debug("Iniciando checagem NTP");
				String ntpServer = this.configurationDAO.getNtpServerAddress();
				Logger.getLogger(getClass()).debug(
						"Servidor NTP configurado: " + ntpServer);

				// fazer a atualização com sudo....
				String stdInAtualiza = "";
				Process pAtualiza = (Runtime.getRuntime().exec("sudo " + stdIn
						+ " -u " + ntpServer));
				BufferedReader stdInputAtualiza = new BufferedReader(
						new InputStreamReader(pAtualiza.getInputStream()));
				while ((s = stdInputAtualiza.readLine()) != null) {
					stdInAtualiza += s + "\n";
				}
				Logger.getLogger(getClass()).debug(
						"Resultado atualização ntp [ sudo " + stdIn + " -u "
								+ ntpServer + "]: " + stdInAtualiza);
			} else {
				Logger.getLogger(getClass()).info(
						"Atualização NTP automática não está ativa.");
			}
		}
	}

}
