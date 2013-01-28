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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

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

	public long diffrenceTime(String serverTime, String clientTime, String SO) {

		DateUtils date = new DateUtils();
		long diff = 0;

		// Converting String dates to java.util.Date

		Date stime = date.dateConverter(serverTime, SO);
		Date ctime = date.dateConverter(clientTime, SO);

		diff = stime.getTime() - ctime.getTime();

		DecimalFormat df = new DecimalFormat();
		df.applyPattern("00.00;(00.00)");
		long result = diff / (1000);

		return result;
	}

	public Date dateConverter(String data, String SO) {
		Date date = null;
		SimpleDateFormat formatador;

		if (SO.equals("LINUX")) {
			if (data.startsWith("Dom") || data.startsWith("Seg") || data.startsWith("Ter") || data.startsWith("Qua") || data.startsWith("Qui") || data.startsWith("Sex") || data.startsWith("Sab")) {
				formatador = new SimpleDateFormat(
						"EEE MMM dd HH:mm:ss zzzz yyyy", new Locale("pt", "BR"));
			} else {
				formatador = new SimpleDateFormat(
						"EEE MMM dd HH:mm:ss zzzz yyyy");
			}

			try {
				date = formatador.parse(data);
			} catch (ParseException ex) {
				throw new RuntimeException(ex);
			}

		}

		if (SO.equals("WINDOWS")) {
			formatador = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss yyyy");
			try {
				date = formatador.parse(data);
			} catch (ParseException ex) {
				throw new RuntimeException(ex);
			}

		}

		if (SO.equals("UNIX")) {
			formatador = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss zzzz yyyy");
			try {
				date = formatador.parse(data);
			} catch (ParseException ex) {
				throw new RuntimeException(ex);
			}
		}

		if (SO.equals("OUTRO")) {
			formatador = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss zzzz yyyy");
			try {
				date = formatador.parse(data);
			} catch (ParseException ex) {
				throw new RuntimeException(ex);
			}

		}

		return date;
	}
}
