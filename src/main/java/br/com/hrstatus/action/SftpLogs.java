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

package br.com.hrstatus.action;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/*
 * @author spolti
 * Any codes from jcraft.com
 */


public class SftpLogs {

	static Logger log =  Logger.getLogger(SftpLogs.class.getCanonicalName());
	
	@SuppressWarnings("unchecked")
	public static class MyLogger implements com.jcraft.jsch.Logger {
		@SuppressWarnings("rawtypes")
		static java.util.Hashtable name = new java.util.Hashtable();
		static {
			name.put(new Integer(DEBUG), "DEBUG: ");
			name.put(new Integer(INFO), "INFO: ");
			name.put(new Integer(WARN), "WARN: ");
			name.put(new Integer(ERROR), "ERROR: ");
			name.put(new Integer(FATAL), "FATAL: ");
		}

		public boolean isEnabled(int level) {
			return true;
		}

		public void log(int level, String message) {
			System.out.print(name.get(new Integer(level)));
			log.fine(message);
		}
	}

	public String showGetFiles(String user, String pass, String host, int port, String remoteDir) throws JSchException, IOException {
		
		String s = "";
		JSch.setLogger(new MyLogger());

		// Disabling host key check
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");

		// Creating the server session and connecting
		JSch jsch = new JSch();
		Session session = jsch.getSession(user, host, port);
		session.setConfig(config);
		session.setPassword(pass);
		session.connect(10000);

		// Executing the command
		String command = "ls -lh "+ remoteDir +" | awk '{print $1,$5,$9}' | grep -v total |  cut -c  1,12- | sed 's/^-//;s/ 4.0K//;s/4.0K//;s/^l//;s/^ //g'";
		System.out.println(command);
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand(command);
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

				break;
			}
		}
		channel.disconnect();
		session.disconnect();

		while (s.endsWith(" ")) {
			s = s.substring(0, -1);
		}
		return s;
	}

	public String tailFile(String user, String pass, String host, int port,	String remoteDir, String file, Integer numeroLinhas) throws JSchException, IOException {

		String s = "";

		// Disabling host key check
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");

		// Creating the server session and connecting
		JSch jsch = new JSch();
		Session session = jsch.getSession(user, host, port);
		session.setConfig(config);
		session.setPassword(pass);
		session.connect(10000);

		// Executing the command
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand("tail -n " + numeroLinhas + " " + remoteDir + "/" + file);
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

				break;
			}
		}
		channel.disconnect();
		session.disconnect();

		while (s.endsWith(" ")) {
			s = s.substring(0, -1);
		}
		return s;

	}

	public String findInFile(String user, String pass, String host, int port, String remoteDir, String file, String palavraBusca) throws JSchException, IOException {

		String s = "";

		log.fine("Debugging grep command: \"grep -i \"" + palavraBusca + "' " + remoteDir + "/" + file);
		
		// Disabling host key check
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");

		// Creating the server session and connecting
		JSch jsch = new JSch();
		Session session = jsch.getSession(user, host, port);
		session.setConfig(config);
		session.setPassword(pass);
		session.connect(10000);

		// Executing the command
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand("grep -i '" + palavraBusca + "' " + remoteDir + "/" + file);
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

				break;
			}
		}
		channel.disconnect();
		session.disconnect();

		while (s.endsWith(" ")) {
			s = s.substring(0, -1);
		}
		return s;

	}

	public String getFile(String user, String pass, String host, int port, String rfile) {
		
		FileOutputStream fos = null;
		try {
			// Disabling host key check
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
			session.setConfig(config);
			session.setPassword(pass);
			session.connect(10000);

			// exec 'scp -f rfile' remotely
			String command = "scp -f " + rfile;
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setErrStream(System.err);
			((ChannelExec) channel).setCommand(command);

			// get I/O streams for remote scp
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();

			channel.connect();

			byte[] buf = new byte[1024];

			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();

			while (true) {
				int c = checkAck(in);
				if (c != 'C') {
					break;
				}

				// read '0644 '
				in.read(buf, 0, 5);

				long filesize = 0L;
				while (true) {
					if (in.read(buf, 0, 1) < 0) {
						// error
						break;
					}
					if (buf[0] == ' ')
						break;
					filesize = filesize * 10L + (long) (buf[0] - '0');
				}

				@SuppressWarnings("unused")
				String file = null;
				for (int i = 0;; i++) {
					in.read(buf, i, 1);
					if (buf[i] == (byte) 0x0a) {
						file = new String(buf, 0, i);
						break;
					}
				}

				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();

				// read a content of lfile
				fos = new FileOutputStream("tempFile.log");
				int foo;
				while (true) {
					if (buf.length < filesize)
						foo = buf.length;
					else
						foo = (int) filesize;
					foo = in.read(buf, 0, foo);
					if (foo < 0) {
						// error
						break;
					}
					fos.write(buf, 0, foo);
					filesize -= foo;
					if (filesize == 0L)
						break;
				}
				fos.close();
				fos = null;

				if (checkAck(in) != 0) {
					System.exit(0);
				}

				// send '\0'
				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();
			}

			session.disconnect();

		} catch (Exception e) {
			System.err.println(e);
			return "Download falied";
		}
		return "File Downloaded.";
	}

	static int checkAck(InputStream in) throws IOException {
		int b = in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
		if (b == 0)
			return b;
		if (b == -1)
			return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
			if (b == 1) { // error
				log.severe(sb.toString());
			}
			if (b == 2) { // fatal error
				log.severe(sb.toString());
			}
		}
		return b;
	}
}