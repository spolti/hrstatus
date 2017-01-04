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

package br.com.hrstatus.utils.notification.channel;

import br.com.hrstatus.model.Setup;
import br.com.hrstatus.repository.impl.DataBaseRepository;
import br.com.hrstatus.utils.notification.Channel;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class Email implements Channel {

    private Logger log = Logger.getLogger(Email.class.getName());

    @Inject
    private DataBaseRepository repository;
    @Inject
    private Setup setup;

    private InitialContext INITIAL_CONTEXT;
    private Session MAIL_SESSION;
    private MimeMessage MAIL_MESSAGE;
    private String MAIL_SESSION_JNDI;
    private String MAIL_FROM;

    @Override
    public String send(String message, String receiver, String subject, String jndi) {

        MAIL_SESSION_JNDI = jndi != null ? jndi : MAIL_SESSION_JNDI;

        //setup the session
        setupEmailSession(receiver);
        log.fine("Enviando email: " + message + " para o usuário " + receiver + " com o subject: " + subject);
        try {
            MAIL_MESSAGE.setSubject(subject);
            MAIL_MESSAGE.setContent(message, "text/html; charset=UTF-8");
            Transport.send(MAIL_MESSAGE);
            return "Email Enviado";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Falha ao enviar email: " + e.getCause();
        }
    }

    /*
    * Configure the email Session
    */
    private void setupEmailSession(String receiver) {
        configureMailBasics();
        try {
            INITIAL_CONTEXT = new InitialContext();
            MAIL_SESSION = (Session) INITIAL_CONTEXT.lookup(MAIL_SESSION_JNDI);
            MAIL_SESSION.setDebug(false);
            MAIL_MESSAGE = new MimeMessage(MAIL_SESSION);
            MAIL_MESSAGE.setFrom(MAIL_FROM);
            MAIL_MESSAGE.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            MAIL_MESSAGE.setSentDate(new java.util.Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * Retrieve the mail configuration fromn database
    */
    private void configureMailBasics() {
        setup = repository.loadConfiguration();
        MAIL_SESSION_JNDI = setup.getMailJndi();
        MAIL_FROM = setup.getMailFrom();
    }
}