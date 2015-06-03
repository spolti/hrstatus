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

package br.com.hrstatus.utils.mail;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.hrstatus.utils.GetServerIPAddress;

/*
 * @author spolti
 */

public class MailSender {

	Logger log =  Logger.getLogger(MailSender.class.getCanonicalName());
	
	GetServerIPAddress getIP = new GetServerIPAddress();	

	public void Sender(String mailSender, String Subject, String dests[], String jndiMail) throws UnknownHostException {

		String url = ("" + getIP.returnServerAddres());
		try {

			InitialContext ic = new InitialContext();
			Session session = (Session) ic.lookup(jndiMail);
			session.setDebug(false);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailSender));
			InternetAddress to[] = new InternetAddress[dests.length];
			for (int i = 0; i < dests.length; i++) {
				to[i] = new InternetAddress(dests[i]);
			}
			message.setRecipients(Message.RecipientType.TO, to);
			message.setSubject(Subject);
			message.setSentDate(new java.util.Date());

			String msg = "<html>"
					+ "<body>"
					+ "<div style='text-align:center; width: 100%;'>"
					+ "<div style='width: 700px; margin: 0 auto;'>"
					+ "<a href='http://"
					+ url
					+ "/hs/home'><img src='http://"
					+ url
					+ "/hs/show/emailHeader/up' height='125' width='700'/></a><br>"
					+ "<br><br><h2>Olá, alguns servidores ainda estão com a data/hora desatualizados.  "
					+ "\nAcesse o link abaixo para maiores detalhes:"
					+ "<a href=\"http://"
					+ url
					+ "/hs/reports/reportServersNOK\"> Relatório</a>"
					+ "</h1><br><br><a href='http://"
					+ url
					+ "/hs/home'><img src='http://"
					+ url
					+ "/hs/show/emailHeader/down' height='125' width='700'/></a>"
					+ "</div>" + "</div>" + "</body>" + "</html>";

			log.fine(msg);
			message.setContent(msg, "text/html; charset=UTF-8");

			try {
				Transport.send(message);
				log.info("----> Email sent.");

			} catch (Exception e) {
				log.severe("----> Email not sent.");
				log.severe(e.toString());
			}
		} catch (Exception e) {
			log.severe(e.toString());
		}
	}

	public String sendNewPass(String mailSender, String dest, String jndiMail, String pass, String username) throws UnknownHostException {

		String url = ("" + getIP.returnServerAddres());

		try {

			InitialContext ic = new InitialContext();
			Session session = (Session) ic.lookup(jndiMail);
			session.setDebug(false);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailSender));
			InternetAddress to = new InternetAddress(dest);
			message.setRecipient(Message.RecipientType.TO, to);
			message.setSubject("NO REPLY - Recuperação de Senha HR status");
			message.setSentDate(new java.util.Date());

			String msg = "<html>"
					+ "<body>"
					+ "<div style='text-align:center; width: 100%;'>"
					+ "<div style='width: 700px; margin: 0 auto;'>"
					+ "<a href='http://"
					+ url
					+ "/hs/home'><img src='http://"
					+ url
					+ "/hs/show/emailHeader/up' height='125' width='700'/></a><br>"
					+ "<h2><br><p align='left' style='width:630px; margin: 0 auto;'>Olá <a style='color: blue;'>"
					+ username
					+ "</a><br> Sua nova é senha: <a style='color: blue;'>"
					+ pass
					+ "</a><br>"
					+ " Para acesso utilize a url: <br><a href='http://"
					+ url
					+ "/hs/login'> HrStatus </p></h2>"
					+ "<br><a href='http://"
					+ url
					+ "/hs/home'><img src='http://"
					+ url
					+ "/hs/show/emailHeader/down' height='125' width='700'/></a>"
					+ "</div>" + "</div>" + "</body>" + "</html>";

			log.fine(msg);
			message.setContent(msg, "text/html; charset=UTF-8");

			try {
				Transport.send(message);
				log.info("----> Email sent.");
				return "----> Email sent";

			} catch (Exception e) {
				log.severe("----> Email not sent.");
				log.severe(e.toString());
				return "----> Email not sent";
			}

		} catch (Exception e) {
			log.severe(e.toString());
			return "----> Email not sent";
		}
	}

	public void sendCreatUserInfo(String mailSender, String dest, String jndiMail, String name, String username, String pass)
			throws UnsupportedEncodingException, UnknownHostException {

		String url = ("" + getIP.returnServerAddres());

		try {

			InitialContext ic = new InitialContext();
			Session session = (Session) ic.lookup(jndiMail);
			session.setDebug(false);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailSender));
			InternetAddress to = new InternetAddress(dest);
			message.setRecipient(Message.RecipientType.TO, to);
			message.setSubject("NO REPLY - Criação de usuário HR status");
			message.setSentDate(new java.util.Date());

			String msg = "<html>" + "<body>"
					+ "<div style='text-align:center; width: 100%;'>"
					+ "<div style='width: 700px; margin: 0 auto;'>"
					+ "<a href='http://"
					+ url
					+ "/hs/home'><img src='http://"
					+ url
					+ "/hs/show/emailHeader/up' height='125' width='700'/></a><br>"
					+ "<h2><br>Olá <a style='color: blue;'>"
					+ name
					+ "</a>, seu usuário foi criado com sucesso no sistema HrStatus"
					+ " Seus dados para acesso são:<br><br> "
					+ "<p align='left' style='width:630px; margin: 0 auto;'>Usuário: "
					+ username
					+ "<br>"
					+ "Senha: "
					+ pass
					+ "</p><br>"
					+ " Para acesso utilize a url: <br><a href='http://"
					+ url
					+ "/hs/login'> HrStatus </a></h2>"
					+ "<br><a href='http://"
					+ url
					+ "/hs/home'><img src='http://"
					+ url
					+ "/hs/show/emailHeader/down' height='125' width='700'/></a>"
					+ "</div>" + "</div>" + "</body>" + "</html>";

			log.fine(msg);
			message.setContent(msg, "text/html; charset=UTF-8");

			try {
				Transport.send(message);
				log.info("----> Email sent");

			} catch (Exception e) {
				log.severe("----> Email not sent");
				log.severe(e.toString());
			}

		} catch (Exception e) {
			log.severe(e.toString());

		}
	}
		
	public String sendTestMail(String mailSender,  String dest, String jndiMail) throws UnknownHostException, NamingException, AddressException, MessagingException {

		InitialContext ic = new InitialContext();
		Session session = (Session) ic.lookup(jndiMail);
		session.setDebug(false);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(mailSender));
		InternetAddress to = new InternetAddress(dest);
		message.setRecipient(Message.RecipientType.TO, to);
		message.setSubject("NO REPLY - E-mail de teste");
		message.setSentDate(new java.util.Date());
		String msg = " Mail Test Message -> Teste realizado com sucesso.";
		log.fine(msg);
		message.setContent(msg, "text/html; charset=UTF-8");

		try {
			Transport.send(message);
			log.info("----> Test email sent");
			return "----> Test email sent.";
		} catch (Exception e) {
			log.severe("----> Test email not sent");
			log.severe(e.toString());
			return "----> Test email no sent -----> " + e;
		}

	}
}