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

package br.com.ohsnap.hrstatus.action.linux;

/*
 * @author spolti
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import br.com.ohsnap.hrstatus.utils.DateUtils;

import com.jcraft.jsch.*;

public class GetDateLinux {

	public static String exec(String user, String host, String password,
			int port) throws JSchException, IOException {

		String s = "";

		// informações de usuário/host/porta para conexão

		// definindo a não obrigação do arquivo know_hosts
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");

		// Criando a sessao e conectando no servidor
		JSch jsch = new JSch();
		Session session = jsch.getSession(user, host, port);
		session.setConfig(config);
		session.setPassword(password);
		session.connect(10000);

		// Exectando o comando
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand("date");
		((ChannelExec) channel).setErrStream(System.err);
		InputStream in = channel.getInputStream();

		channel.connect();

		byte[] tmp = new byte[1024];
		boolean test = true;
		while (test == true) {

			while (in.available() > 0) {

				int i = in.read(tmp, 0, 1024);
				if (i < 0) {
					break;
				}
				s += (new String(tmp, 0, i));
			}

			if (channel.isClosed()) {
				// System.out.println("exit-status: "+channel.getExitStatus());
				break;
			}
		}
		channel.disconnect();
		session.disconnect();

//		if (s.endsWith(" ")) {
//			return s.substring(0, -1);
//		} else {
//			return s;
//		}
		while (s.endsWith(" ")){
			s = s.substring(0, -1);
		}
		return s;
	}

	public static void main(String args[]) throws IOException, JSchException {
		GetDateLinux get = new GetDateLinux();

		String tmp = get.exec("nagios", "10.11.133.152", "23d3marc0", 22);
		DateUtils util = new DateUtils();
		Date data = util.dateConverter(tmp, "LINUX", null);
		
		System.out.println(data);
	}

}
