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

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;

import br.com.ohsnap.hrstatus.action.linux.GetDateLinux;
import br.com.ohsnap.hrstatus.model.Servidores;
import br.com.ohsnap.hrstatus.security.Crypto;

import com.jcraft.jsch.JSchException;

public class DateUtils {
	
	public String getTime(String SO) {

		Calendar cal = Calendar.getInstance();
		String serverTime = null;

		if (SO.equals("LINUX")) {
			DateFormat dateFormatLinux = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss zzzz yyyy");
			serverTime = (dateFormatLinux.format(cal.getTime()));
			Logger.getLogger(getClass()).debug(
					"The server time is: " + serverTime);
		}

		if (SO.equals("WINDOWS")) {
			DateFormat dateFormatWindows = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss yyyy");
			serverTime = (dateFormatWindows.format(cal.getTime()));
			Logger.getLogger(getClass()).debug(
					"The server time is: " + serverTime);

		}

		if (SO.equals("UNIX")) {
			DateFormat dateFormatUnix = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss zzzz yyyy");
			serverTime = (dateFormatUnix.format(cal.getTime()));
			Logger.getLogger(getClass()).debug(
					"The server time is: " + serverTime);
		}

		if (SO.equals("OUTRO")) {
			DateFormat dateFormatOther = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss zzzz yyyy");
			serverTime = (dateFormatOther.format(cal.getTime()));
			Logger.getLogger(getClass()).debug(
					"The server time is: " + serverTime);
		}

		if (SO.equals("lastCheck")) {
			DateFormat dateFormatOther = new SimpleDateFormat(
					"HH:mm:ss dd/MM/yyyy");
			serverTime = (dateFormatOther.format(cal.getTime()));
			Logger.getLogger(getClass()).debug(
					"lastChek getDate is: " + serverTime);
		}

		return serverTime;

	}

	public long diffrenceTime(String serverTime, String clientTime, String SO, Servidores servidores) throws JSchException, IOException {

		DateUtils date = new DateUtils();
		long diff = 0;

		// Converting String dates to java.util.Date

		Date stime = date.dateConverter(serverTime, SO, servidores);
		Date ctime = date.dateConverter(clientTime, SO, servidores);

		diff = stime.getTime() - ctime.getTime();

		DecimalFormat df = new DecimalFormat();
		df.applyPattern("00.00;(00.00)");
		long result = diff / (1000);

		return result;
	}
	
	public Date dateConverter(String data, String SO, Servidores server) throws JSchException, IOException {
		Date date = null;
		SimpleDateFormat formatador;

		if (SO.equals("LINUX")) {
			if (data.startsWith("Dom") || data.startsWith("Seg")
					|| data.startsWith("Ter") || data.startsWith("Qua")
					|| data.startsWith("Qui") || data.startsWith("Sex")
					|| data.startsWith("Sab")) {
				formatador = new SimpleDateFormat(
						"EEE MMM dd HH:mm:ss zzzz yyyy", new Locale("pt", "BR"));
			} else {
				formatador = new SimpleDateFormat(
						"EEE MMM dd HH:mm:ss zzzz yyyy");
			}
			
			while (data.equals("")){
				Logger.getLogger(getClass()).error("Erro ao tentar buscar data do servidor id " + server.getId());
				Logger.getLogger(getClass()).info("Buscando dados do servidor id " + server.getId() + "... tentando obter data novamente...");
	
				String data1 = GetDateLinux.exec(server.getUser(), server.getIp(), server.getPass(), server.getPort());
				Logger.getLogger(getClass()).info("Data obtida do servidor " + server.getId() + ": " + data1);
				if (data1.startsWith("Dom") || data1.startsWith("Seg")
						|| data1.startsWith("Ter") || data1.startsWith("Qua")
						|| data1.startsWith("Qui") || data1.startsWith("Sex")
						|| data1.startsWith("Sab")) {
					formatador = new SimpleDateFormat(
							"EEE MMM dd HH:mm:ss zzzz yyyy", new Locale("pt", "BR"));
				} else {
					formatador = new SimpleDateFormat(
							"EEE MMM dd HH:mm:ss zzzz yyyy");
				}
				data = data1;
			} 
				
			try {
				date = formatador.parse(data);
			} catch (ParseException ex) {
				throw new RuntimeException(ex);
			}

		}

		if (SO.equals("WINDOWS")) {
			formatador = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
			try {
				date = formatador.parse(data);
			} catch (ParseException ex) {
				throw new RuntimeException(ex);
			}

		}

		if (SO.equals("UNIX")) {
			formatador = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
			try {
				date = formatador.parse(data);
			} catch (ParseException ex) {
				throw new RuntimeException(ex);
			}
		}

		if (SO.equals("OUTRO")) {
			formatador = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
			try {
				date = formatador.parse(data);
			} catch (ParseException ex) {
				throw new RuntimeException(ex);
			}

		}

		return date;
	}
}
