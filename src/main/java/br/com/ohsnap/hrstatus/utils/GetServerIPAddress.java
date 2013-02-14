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

public class GetServerIPAddress {

	public String returnIP() {

		String ip = "";
		try {
			java.net.InetAddress i = java.net.InetAddress.getLocalHost();
			ip = i.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ip;
	}
}