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

package br.com.hrstatus.action.windows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/*
 * @author spolti
 */

public class GetDateWindows {

	static Logger log =  Logger.getLogger(GetDateWindows.class.getCanonicalName());
	
	public static String Exec(String ip, String parameter) throws IOException {

		Process p = null;
		String out = null;
		String s = null;
		
		try {

			if (parameter.equals("I")) {
				log.fine("Trying parameter -I");
				p = Runtime.getRuntime().exec("net time -I " + ip);
				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((s = reader.readLine()) != null) {
					out += s;
				}

				// i dont know why, but the net time command returns null before
				// the date, like this: nullTer Mar ......
				if (out.startsWith("n")) {
					String temp = out.substring(4, out.length());
					out = temp;
				}

			} else if (parameter.equals("S")) {
				log.fine("Trying parameter -S");
				p = Runtime.getRuntime().exec("net time -S " + ip);
				if (p.equals(null)) {
					log.fine("Null pointer at this point...");
				}
				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((s = reader.readLine()) != null) {
					out += s;
				}

				// i dont know why, but the net time command returns null before
				// the date, like this: nullTer Mar ......
				if (out.startsWith("n")) {
					String temp = out.substring(4, out.length());
					out = temp;
				}
	
			}
			
			return out;
			
		} catch (Exception ex) {
			log.severe(ex.toString());
			return "";
		}

	}
}