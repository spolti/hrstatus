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

package br.com.hrstatus.scheduler;

import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.model.Configurations;
import br.com.hrstatus.utils.mail.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Logger;

/*
 * @author spolti
 */

@Service
public class MainScheduler {

    private Logger log = Logger.getLogger(MainScheduler.class.getName());

    @Autowired
    private ServersInterface iteracoesDAO;

    @Autowired
    private Configuration configurationDAO;

    public MainScheduler() {
    }

    @Scheduled(cron = "${br.com.hrstatus.scheduler.MailScheduler.cron}")
    public void sendMail() throws UnknownHostException {

        log.fine("[ System ] Invoking sendMail() at " + new Date());

        final int count = this.iteracoesDAO.countServersNOK();
        final boolean sendNotification = this.configurationDAO.sendNotification();

        log.fine("[ System ] Notification through email is active? -> " + sendNotification);

        if (sendNotification) {

            if (count > 0) {

                log.fine("[ System ] Was found " + count + " servers outdated, sending notification email");

                final String mail1 = this.configurationDAO.getMailSenderNotLogged();
                final String Subject = this.configurationDAO.getSubjectNotLogged();
                final String []Dests = this.configurationDAO.getDestsNotLogged().split(",");
                final String jndiMail = this.configurationDAO.getJndiMailNotLogged();
                final MailSender mail = new MailSender();
                mail.Sender(mail1, Subject, Dests, jndiMail);
            } else {
                log.fine("[ System ] No server outdated was found");
            }
        } else {
            log.fine("Sending notification e-mail is disabled");
        }
    }

    @Scheduled(cron = "${br.com.hrstatus.scheduler.NtpScheduler.cron}")
    public void NtpUpdater() throws IOException {

        final Configurations config = this.configurationDAO.getConfigs();
        final Boolean isUpdateNtpActive = config.isUpdateNtpIsActive();

        final Process p = (Runtime.getRuntime().exec("which ntpdate"));
        String stdIn = "";
        String s;
        final BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((s = stdInput.readLine()) != null) {
            stdIn += s + "\n";
        }

        if ("".equals(stdIn)) {
            log.severe("ntpdate command not found, aborting the automated update");

        } else {
            log.finest("ntp ativo: " + isUpdateNtpActive);
            if (isUpdateNtpActive) {
                log.fine("Iniciando checagem NTP");
                final String ntpServer = this.configurationDAO.getNtpServerAddressNotLogged();
                log.fine("Servidor NTP configurado: " + ntpServer);

                // Doing the update with sudo
                String stdInAtualiza = "";
                final Process pAtualiza = (Runtime.getRuntime().exec("sudo " + stdIn + " -u " + ntpServer));
                final BufferedReader stdInputAtualiza = new BufferedReader(new InputStreamReader(pAtualiza.getInputStream()));
                while ((s = stdInputAtualiza.readLine()) != null) {
                    stdInAtualiza += s + "\n";
                }
                if (stdInAtualiza.contains("offset")) {
                    log.fine("Result of ntp update [ sudo " + stdIn + " -u " + ntpServer + "]: " + stdInAtualiza);
                } else {
                    log.warning("command not performed, result: " + stdInAtualiza);
                    log.warning("Make sure that the user used to execute the WildFly/JBoss have the correct sudo permissions to execute the ntpdate -u command");
                }

            } else {
                log.fine("NTP Update automatic is not active");
            }
        }
    }
}