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

package br.com.hrstatus.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.hrstatus.action.SftpLogs;
import br.com.hrstatus.dao.InstallProcessInterface;
import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.model.Users;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.utils.GetSystemInformation;
import br.com.hrstatus.utils.PropertiesLoaderImpl;
import br.com.hrstatus.utils.UserInfo;

import com.jcraft.jsch.JSchException;

/*
 * @author spolti
 */

@Resource
public class LogsController {

	Logger log = Logger.getLogger(LogsController.class.getCanonicalName());

	@Autowired
	private Result result;
	@Autowired
	private ServersInterface iteracoesDAO;
	@Autowired
	private UsersInterface userDAO;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private InstallProcessInterface ipi;
	private GetSystemInformation getSys = new GetSystemInformation();
	private UserInfo userInfo = new UserInfo();
	private SftpLogs listLogs = new SftpLogs();
	private SftpLogs getLogFile = new SftpLogs();
	private List<Servidores> server = new ArrayList<Servidores>();

	@SuppressWarnings("static-access")
	@Get("/selectServer")
	public void selectServer() {

		//Sending information to "About" page
		PropertiesLoaderImpl load = new PropertiesLoaderImpl();
		String version = load.getValor("version");
		result.include("version", version);
		List<String> info = getSys.SystemInformation();
		result.include("jvmName", info.get(2));
		result.include("jvmVendor",info.get(1));
		result.include("jvmVersion",info.get(0));
		result.include("osInfo",info.get(3));
		result.include("installDate", ipi.getInstallationDate());
		
		// Inserting HTML title in the result
		result.include("title", "Selecione o Servidor");
		// Inserting the Logged username in the home page
		result.include("loggedUser", userInfo.getLoggedUsername()); 
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /selectServer");

		// Getting the server IDs that the logged user can access.
		Users user = this.userDAO.getUserByID(userInfo.getLoggedUsername());

		for (Servidores sv : user.getServer()) {
			log.fine("[ " + userInfo.getLoggedUsername() + " ] The user has permissions on the server: " + sv.getHostname());
			server.add(sv);
		}

		result.include("server", server);
	}

	@SuppressWarnings("static-access")
	@Get("/listLogFiles/{hostname}")
	public void listLogFiles(String hostname) throws JSchException, IOException {

		//Sending information to "About" page
		PropertiesLoaderImpl load = new PropertiesLoaderImpl();
		String version = load.getValor("version");
		result.include("version", version);
		List<String> info = getSys.SystemInformation();
		result.include("jvmName", info.get(2));
		result.include("jvmVendor",info.get(1));
		result.include("jvmVersion",info.get(0));
		result.include("osInfo",info.get(3));
		result.include("installDate", ipi.getInstallationDate());
		
		// Inserting HTML title in the result
		result.include("title", "Lista de Arquivos");
		// Inserting the Logged username in the home page
		result.include("loggedUser", userInfo.getLoggedUsername());

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /listLogFiles/" + hostname);
		log.info("[ " + userInfo.getLoggedUsername() + " ] Listing files of " + hostname);

		boolean isAdmin = request.isUserInRole("ROLE_ADMIN");
		Servidores servidor = this.iteracoesDAO.getServerByHostname(hostname);
		Users user = this.userDAO.getUserByID(userInfo.getLoggedUsername());

		if (isAdmin) {
			try {
				// setting logDir
				result.include("logDir", servidor.getLogDir());
				servidor.setPass(String.valueOf(Crypto.decode(servidor.getPass())));
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			String files = listLogs.showGetFiles(servidor.getUser(),
					servidor.getPass(), servidor.getIp(), servidor.getPort(),
					servidor.getLogDir());
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Files found: " + files);

			String listOfFiles[] = files.split("\n");
			result.include("hostname", servidor.getHostname());
			result.include("qtdn", listOfFiles.length - 1);
			result.include("listOfFiles", listOfFiles);

		} else {

			if (userInfo.listLogFiles(user, servidor.getId())) {
				log.info("[ " + userInfo.getLoggedUsername() + " ] The user has permissions on the server: " + hostname);

				try {
					// setting logDir
					result.include("logDir", servidor.getLogDir());
					servidor.setPass(String.valueOf(Crypto.decode(servidor.getPass())));
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (BadPaddingException e) {
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

				String files = listLogs.showGetFiles(servidor.getUser(), servidor.getPass(), servidor.getIp(),
						servidor.getPort(), servidor.getLogDir());
				log.fine("[ " + userInfo.getLoggedUsername() + " ] Files found: " + files);

				String listOfFiles[] = files.split("\n");
				result.include("hostname", servidor.getHostname());
				result.include("qtdn", listOfFiles.length);
				result.include("listOfFiles", listOfFiles);

			} else {
				log.info("[ " + userInfo.getLoggedUsername() + " ] The user has permissions on the server: " + hostname);
				result.use(Results.http()).sendError(403);
			}
		}
	}

	@SuppressWarnings("static-access")
	@Get("/listLogFiles/{hostname}/subdir/{subdir*}")
	public void listLogFilesSubdir(String hostname, String subdir)
			throws JSchException, IOException {

		//Sending information to "About" page
		PropertiesLoaderImpl load = new PropertiesLoaderImpl();
		String version = load.getValor("version");
		result.include("version", version);
		List<String> info = getSys.SystemInformation();
		result.include("jvmName", info.get(2));
		result.include("jvmVendor",info.get(1));
		result.include("jvmVersion",info.get(0));
		result.include("osInfo",info.get(3));
		result.include("installDate", ipi.getInstallationDate());
		
		// Inserting HTML title in the result
		result.include("title", "Lista de Arquivos SubiDir " + subdir);
		// Inserting the Logged username in the home page
		result.include("loggedUser", userInfo.getLoggedUsername());

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /listLogFiles/" + hostname + "/subdir/"
				+ subdir);

		// the "d" represents directory, so is needed remove it to access the
		// directories recursively.
		subdir = subdir.replace("d ", "");

		boolean isAdmin = request.isUserInRole("ROLE_ADMIN");
		Servidores servidor = this.iteracoesDAO.getServerByHostname(hostname);
		Users user = this.userDAO.getUserByID(userInfo.getLoggedUsername());

		if (isAdmin) {
			try {
				// setting logDir
				if (servidor.getLogDir().equals(subdir)) {
					result.include("logDir", subdir);
				} else {
					if (subdir.contains(servidor.getLogDir())) {
						subdir = subdir.replace(servidor.getLogDir() + "/", "");
					}
					result.include("logDir", servidor.getLogDir() + "/"	+ subdir);
				}
				servidor.setPass(String.valueOf(Crypto.decode(servidor.getPass())));
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			String files = listLogs.showGetFiles(servidor.getUser(),
					servidor.getPass(), servidor.getIp(), servidor.getPort(),
					servidor.getLogDir() + "/" + subdir);
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Files found: " + files);

			String listOfFiles[] = files.split("\n");
			result.include("hostname", servidor.getHostname());
			result.include("qtdn", listOfFiles.length);
			result.include("listOfFiles", listOfFiles);

		} else {

			if (userInfo.listLogFiles(user, servidor.getId())) {
				log.info("[ " + userInfo.getLoggedUsername() + " ] The user has permissions on the server: " + hostname);

				try {
					// setting logDir in the session
					result.include("logDir", servidor.getLogDir() + "/"	+ subdir);
					servidor.setPass(String.valueOf(Crypto.decode(servidor.getPass())));
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (BadPaddingException e) {
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

				String files = listLogs.showGetFiles(servidor.getUser(),
						servidor.getPass(), servidor.getIp(),
						servidor.getPort(), servidor.getLogDir());
				log.fine("[ " + userInfo.getLoggedUsername() + " ] Files found: " + files);

				String listOfFiles[] = files.split("\n");
				result.include("hostname", servidor.getHostname());
				result.include("qtdn", listOfFiles.length);
				result.include("listOfFiles", listOfFiles);

			} else {
				log.info("[ " + userInfo.getLoggedUsername() + " ] The user has permissions on the server: " + hostname);
				result.use(Results.http()).sendError(403);
			}
		}
	}

	@SuppressWarnings("static-access")
	@Get("/tailFile/{hostname}/{numeroLinhas}/{file*}")
	public void tailFile(String hostname, String file, Integer numeroLinhas)
			throws JSchException, IOException, Exception {

		//Sending information to "About" page
		PropertiesLoaderImpl load = new PropertiesLoaderImpl();
		String version = load.getValor("version");
		result.include("version", version);
		List<String> info = getSys.SystemInformation();
		result.include("jvmName", info.get(2));
		result.include("jvmVendor",info.get(1));
		result.include("jvmVersion",info.get(0));
		result.include("osInfo",info.get(3));
		result.include("installDate", ipi.getInstallationDate());
		
		log.info("[ " + userInfo.getLoggedUsername() + " ] Tail of file " + file + " of " + hostname + " - " + numeroLinhas + " linhas. ");

		// Inserting HTML title in the result
		result.include("title", "Tail do arquivo " + file + ". Trazendo as " + numeroLinhas + " últimas linhas.");
		// Inserting the Logged username in the home page
		result.include("loggedUser", userInfo.getLoggedUsername());

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /tailFile");
		log.info("[ " + userInfo.getLoggedUsername() + " ] Tail of file " + file + " of " + hostname);

		if (numeroLinhas == null || numeroLinhas < 0 || numeroLinhas > 1000) {
			throw new Exception("Número de linhas inválido.");
		} else if (file == null || file.isEmpty()) {
			throw new Exception("Arquivo inválido.");
		} else if (hostname == null || hostname.isEmpty()) {
			throw new Exception("Hostname inválido.");
		}

		Servidores servidor = this.iteracoesDAO.getServerByHostname(hostname);

		// setting logDir
		result.include("logDir", servidor.getLogDir());
		// setting fileName
		result.include("fileName", file);

		try {
			servidor.setPass(String.valueOf(Crypto.decode(servidor.getPass())));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}

		if (file.contains("/")) {
			log.fine("Transforming directory path.");
			String[] temp = file.split("/");
			String dir = "";
			for (int i = 1; i < temp.length; i++) {
				// if (!temp[i].isEmpty())
				dir += "/" + temp[i];
			}
			// getting the file name
			file = temp[0];
			result.include("fileName", file);
			servidor.setLogDir(dir + "/");
			log.fine("Complete directory: " + dir + "/");
		}
		
		
		String files = "\n";
		files += listLogs.tailFile(servidor.getUser(), servidor.getPass(), servidor.getIp(), servidor.getPort(), servidor.getLogDir(), file, numeroLinhas);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Files found: \n" + files);

		String linhasArquivo[] = files.split("\n");
		result.include("hostname", servidor.getHostname());
		result.include("qtdn", linhasArquivo.length -1);
		result.include("linhasArquivo", linhasArquivo);

	}

	@SuppressWarnings("static-access")
	@Get("/findInFile/{hostname}/{palavraBusca}/{file*}")
	public void findInFile(String hostname, String file, String palavraBusca)
			throws JSchException, IOException, Exception {

		//Sending information to "About" page
		PropertiesLoaderImpl load = new PropertiesLoaderImpl();
		String version = load.getValor("version");
		result.include("version", version);
		List<String> info = getSys.SystemInformation();
		result.include("jvmName", info.get(2));
		result.include("jvmVendor",info.get(1));
		result.include("jvmVersion",info.get(0));
		result.include("osInfo",info.get(3));
		result.include("installDate", ipi.getInstallationDate());
		
		log.fine("hostname: " + hostname + " palavraBusca: " + palavraBusca + " file: " + file);

		// Inserting HTML title in the result
		result.include("title", "Busca no arquivo " + file + ". Buscando: "	+ palavraBusca);
		// Inserting the Logged username in the home page
		result.include("loggedUser", userInfo.getLoggedUsername());

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /findInFile");
		log.info("[ " + userInfo.getLoggedUsername() + " ] Finding " + palavraBusca + " in file " + file + " of " + hostname);

		if (palavraBusca == null || palavraBusca.isEmpty()) {
			throw new Exception("Número de linhas inválido.");
		} else if (file == null || file.isEmpty()) {
			throw new Exception("Arquivo inválido.");
		} else if (hostname == null || hostname.isEmpty()) {
			throw new Exception("Hostname inválido.");
		}

		Servidores servidor = this.iteracoesDAO.getServerByHostname(hostname);

		try {
			servidor.setPass(String.valueOf(Crypto.decode(servidor.getPass())));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}

		// setting logDir
		result.include("logDir", servidor.getLogDir());
		// setting fileName
		result.include("fileName", file);

		if (file.contains("/")) {
			log.fine("Transforming directory path.");
			String[] temp = file.split("/");
			String dir = "";
			for (int i = 1; i < temp.length; i++) {
				// if (!temp[i].isEmpty())
				dir += "/" + temp[i];
			}
			// getting the file name
			file = temp[0];
			result.include("fileName", file);
			servidor.setLogDir(dir + "/");
			log.fine("Complete directory: " + dir + "/");
		}

		String files = "\n";
		files += listLogs.findInFile(servidor.getUser(), servidor.getPass(), servidor.getIp(), servidor.getPort(), servidor.getLogDir(), file, palavraBusca);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Content found: \n"+ files);

		String findInFile[] = files.split("\n");
		result.include("hostname", servidor.getHostname());
		result.include("qtdn", findInFile.length - 1);
		result.include("findInFile", findInFile);

	}

	@SuppressWarnings("static-access")
	@Get("/downloadFile/{hostname}/{file}")
	public File downloadFile(String hostname, String file) {

		//Sending information to "About" page
		PropertiesLoaderImpl load = new PropertiesLoaderImpl();
		String version = load.getValor("version");
		result.include("version", version);
		List<String> info = getSys.SystemInformation();
		result.include("jvmName", info.get(2));
		result.include("jvmVendor",info.get(1));
		result.include("jvmVersion",info.get(0));
		result.include("osInfo",info.get(3));
		result.include("installDate", ipi.getInstallationDate());
		
		// Inserting HTML title in the result
		result.include("title", "Download");

		// Inserting the Logged username in the home page
		result.include("loggedUser", userInfo.getLoggedUsername());

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /downloadFile/" + hostname + "/" + file);

		log.fine("[ " + userInfo.getLoggedUsername() + " ] Removing Temporary File.");
		File fileDelete = new File("tempFile.log");
		if (fileDelete.delete()) {
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Temporary file removed. (tempFile.log)");
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Location of the file to be deleted: " + fileDelete.getAbsolutePath());
		} else {
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Temporary file not found. (tempFile.log)");
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Location of the file to be deleted: " + fileDelete.getAbsolutePath());
		}

		Servidores servidor = this.iteracoesDAO.getServerByHostname(hostname);

		try {
			servidor.setPass(String.valueOf(Crypto.decode(servidor.getPass())));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}

		String filename = null;
		// Verifying if the filename starts with space
		if (file.startsWith(" ")) {
			log.fine("[ " + userInfo.getLoggedUsername() + " ] The file "
					+ file + " starts with space, removing it.");
			String temp[] = file.split(" ");
			filename = temp[temp.length - 1];
		} else {
			String getName[] = file.split(" ");
			filename = getName[1];
		}

		String rfile = servidor.getLogDir() + "/" + filename;

		this.response.setContentType("application/octet-stream");
		this.response.setHeader("Content-disposition", "attachment; filename=" + filename + "");

		log.info("[ " + userInfo.getLoggedUsername() + " ] Downloading the file " + filename + " from server "
				+ hostname);

		String downloadResult = getLogFile
				.getFile(servidor.getUser(), servidor.getPass(), servidor.getIp(), servidor.getPort(), rfile);
		log.info("[ " + userInfo.getLoggedUsername() + " ] Download Result: " + downloadResult);
		return new File("tempFile.log");

	}

	@SuppressWarnings("static-access")
	@Get("/downloadFileSubdir/{hostname}/{file}/{logDir*}")
	public File downloadFileSubdir(String hostname, String file, String logDir) throws IOException {

		//validating if the Dir passed in the request is the same registered in the database
		if (checkDir(logDir,hostname)) {
			log.fine("[ " + userInfo.getLoggedUsername() + " ] SECURITY - the logged user is trying to dowload different files from fileSystem");
			this.result.use(Results.http()).sendError(HttpServletResponse.SC_FORBIDDEN);
			
			String content = "Access denied";
			File tempFile = new File("tempFile.log");
			// if file doesnt exists, then create it
			if (!tempFile.exists()) {
				tempFile.createNewFile();
			}

			FileWriter fw = new FileWriter(tempFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			
			return tempFile;
		}
		
		
		//Sending information to "About" page
		PropertiesLoaderImpl load = new PropertiesLoaderImpl();
		String version = load.getValor("version");
		result.include("version", version);
		List<String> info = getSys.SystemInformation();
		result.include("jvmName", info.get(2));
		result.include("jvmVendor",info.get(1));
		result.include("jvmVersion",info.get(0));
		result.include("osInfo",info.get(3));
		result.include("installDate", ipi.getInstallationDate());
		
		// Inserting HTML title in the result
		result.include("title", "Download");
		// Inserting the Logged username in the home page
		result.include("loggedUser", userInfo.getLoggedUsername());

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /downloadFileSubdir/" + hostname + "/" + logDir + "/"
				+ file);

		log.fine("[ " + userInfo.getLoggedUsername() + " ] Removing Temporary File.");
		File fileDelete = new File("tempFile.log");
		if (fileDelete.delete()) {
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Temporary file removed. (tempFile.log)");
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Location of the file to be deleted: " + fileDelete.getAbsolutePath());
		} else {
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Temporary file not found. (tempFile.log)");
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Location of the file to be deleted: "
					+ fileDelete.getAbsolutePath());
		}

		Servidores servidor = this.iteracoesDAO.getServerByHostname(hostname);

		// Verifying if the path received to download is different from the
		// database information.
		if (!servidor.getLogDir().equals(file)) {
			servidor.setLogDir("/" + logDir);
			log.fine("[ " + userInfo.getLoggedUsername() + " ] LogDir from server " + hostname + ": " + servidor.getLogDir());
		}

		try {
			servidor.setPass(String.valueOf(Crypto.decode(servidor.getPass())));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}

		String filename = null;

		// Verifying if the filename starts with space
		if (file.startsWith(" ")) {
			log.fine("[ " + userInfo.getLoggedUsername() + " ] File " + file + " starts with space, removing it.");
			String temp[] = file.split(" ");
			filename = temp[temp.length - 1];
		} else {
			String getName[] = file.split(" ");
			filename = getName[1];
		}

		String rfile = servidor.getLogDir() + "/" + filename;

		this.response.setContentType("application/octet-stream");
		this.response.setHeader("Content-disposition", "attachment; filename=" + filename);

		log.info("[ " + userInfo.getLoggedUsername() + " ] Downloading the file " + filename + " from server " + hostname);

		String downloadResult = getLogFile.getFile(servidor.getUser(), servidor.getPass(),
						servidor.getIp(), servidor.getPort(), rfile);
		log.info("[ " + userInfo.getLoggedUsername() + " ] Download Result: " + downloadResult);
		return new File("tempFile.log");

	}

	/*
	 * return true if the LogDir passed in the request if different from the value stored in the database
	 * it is to avoid the user to specify another file in the filesystem, for example, the passwd file
	 */
	private boolean checkDir (String dirToCheck, String hostname) {
		
		Servidores server = this.iteracoesDAO.getServerLogDir(hostname);
		dirToCheck = "/"+dirToCheck;
		log.fine("from database: " + server.getLogDir() + "------- from request: " + dirToCheck);
		String fromDatabase = server.getLogDir().replace("/", "");
		dirToCheck = dirToCheck.replace("/", "");
		
		if (dirToCheck.contains(fromDatabase)) {
			return false;
		} else {
			return true;
		}
		
	}
	
}