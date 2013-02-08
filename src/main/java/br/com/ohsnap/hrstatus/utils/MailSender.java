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
package br.com.ohsnap.hrstatus.utils;

/*
 * @author spolti
 */

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class MailSender{

	//public Configuration configurationDAO;

	public void Sender(String mailSender, String Subject, String dests[], String jndiMail) {
	
		try {
	
			InitialContext ic = new InitialContext(); 
			Session session = (Session) ic.lookup(jndiMail);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailSender));
			InternetAddress to[] = new InternetAddress[dests.length];
			for(int i = 0; i < dests.length; i++){
				to[i] = new InternetAddress(dests[i]);
			}
			message.setRecipients(Message.RecipientType.TO, to);
			message.setSubject(Subject);
			message.setSentDate(new java.util.Date());

			
			message.setContent ("<h1>Olá, alguns servidores ainda estão com a data/hora desatualizados.  " +
					"\nAcesse o link abaixo para maiores detalhes:</h1> " +
					"<a href=\"http://localhost:8080/hrstatus/reports/reportServersNOK\">Relatório</a>", "text/html");

			Transport.send(message);
			
			Logger.getLogger(getClass()).info("----> Email enviado.");
		
			
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e);
		}
	}
	
	public void sendNewPass(String mailSender, String dest, String jndiMail, String pass){
	
		try {
			
			InitialContext ic = new InitialContext(); 
			Session session = (Session) ic.lookup(jndiMail);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailSender));
			InternetAddress to = new InternetAddress(dest);
			message.setRecipient(Message.RecipientType.TO, to);
			message.setSubject("NO REPLY - Recuperação de Senha HR status");
			message.setSentDate(new java.util.Date());
			
			message.setContent ("Nova senha: " + pass , "text/html");

			Transport.send(message);
			
			Logger.getLogger(getClass()).info("----> Email enviado.");
			
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e);
		}
	}
}
