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

import java.io.File;
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
import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.model.Users;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.utils.UserInfo;

import com.jcraft.jsch.JSchException;

/*
 * @author spolti
 */

@Resource
public class LogsController {

	Logger log =  Logger.getLogger(LogsController.class.getCanonicalName());
	
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
	UserInfo userInfo = new UserInfo();


	@Get("/selectServer")
	public void selectServer() {

		result.include("title", "Selecione o Servidor");
		result.include("loggedUser", userInfo.getLoggedUsername());

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /selectServer");

		List<Servidores> server = new ArrayList<Servidores>();
		// pegar os Ids dos servidores que o usuário pode acessar
		Users user = this.userDAO.getUserByID(userInfo.getLoggedUsername());

		for (Servidores sv : user.getServer()) {
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Servidores com permissão: " + sv.getHostname());
			server.add(sv);
		}

		result.include("server", server);
	}

	@Get("/listLogFiles/{hostname}")
	public void listLogFiles(String hostname) throws JSchException, IOException {
		SftpLogs listLogs = new SftpLogs();

		result.include("title", "Lista de Arquivos");
		// inserindo username na home:
		result.include("loggedUser", userInfo.getLoggedUsername());

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /listLogFiles");
		log.info("[ " + userInfo.getLoggedUsername() + " ] Listing files of " + hostname);

		boolean isAdmin = request.isUserInRole("ROLE_ADMIN");
		Servidores servidor = this.iteracoesDAO.getServerByHostname(hostname);
		Users user = this.userDAO.getUserByID(userInfo.getLoggedUsername());

		if (isAdmin) {
			try {
				// setando logDir
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

			String files = listLogs.showGetFiles(servidor.getUser(),servidor.getPass(), servidor.getIp(), servidor.getPort(), servidor.getLogDir());
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Files found: "	+ files);

			String listOfFiles[] = files.split("\n");
			result.include("hostname", servidor.getHostname());
			result.include("qtdn", listOfFiles.length -1);
			result.include("listOfFiles", listOfFiles);

		} else {

			// for (Servidores sv : user.getServer()) {

			if (userInfo.listLogFiles(user, servidor.getId())) {
				log.info("[ " + userInfo.getLoggedUsername() + " ] Usuário possui permissão " + hostname);

				try {
					// setando logDir
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

				String files = listLogs.showGetFiles(servidor.getUser(),servidor.getPass(), servidor.getIp(),servidor.getPort(), servidor.getLogDir());
				log.fine("[ " + userInfo.getLoggedUsername() + " ] Files found: " + files);

				String listOfFiles[] = files.split("\n");
				result.include("hostname", servidor.getHostname());
				result.include("qtdn", listOfFiles.length);
				result.include("listOfFiles", listOfFiles);

			} else {
				log.info("[ " + userInfo.getLoggedUsername() + " ] Usuário possui não permissão " + hostname);
				result.use(Results.http()).sendError(403);
			}
		}
	}

	@Get("/tailFile/{hostname}/{file}/{numeroLinhas}")
	public void tailFile(String hostname, String file, Integer numeroLinhas) throws JSchException, IOException, Exception {
		
		SftpLogs listLogs = new SftpLogs();
		log.info("[ " + userInfo.getLoggedUsername() + " ] Tail of file" + file + " of " + hostname + " - " + numeroLinhas	+ " linhas. ");

		result.include("title", "Tail do arquivo " + file + ". Trazendo as " + numeroLinhas + " últimas linhas.");
		// inserindo username na home:
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

		// setando logDir
		result.include("logDir", servidor.getLogDir());
		// setando fileName
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

		// TODO criar método no SFTPLogs
		String files = listLogs.tailFile(servidor.getUser(),servidor.getPass(), servidor.getIp(), servidor.getPort(),servidor.getLogDir(), file, numeroLinhas);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Files found: "	+ files);

		String linhasArquivo[] = files.split("\n");
		result.include("hostname", servidor.getHostname());
		result.include("qtdn", linhasArquivo.length);
		result.include("linhasArquivo", linhasArquivo);

	}

	@Get("/findInFile/{hostname}/{file}/{palavraBusca}")
	public void findInFile(String hostname, String file, String palavraBusca) throws JSchException, IOException, Exception {
		SftpLogs listLogs = new SftpLogs();

		result.include("title", "Busca no arquivo " + file + ". Buscando: " + palavraBusca);
		// inserindo username na home:
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

		// setando logDir
		result.include("logDir", servidor.getLogDir());
		// setando fileName
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

		// TODO criar método no SFTPLog
		String files = listLogs.findInFile(servidor.getUser(),servidor.getPass(), servidor.getIp(), servidor.getPort(),	servidor.getLogDir(), file, palavraBusca);
		log.fine("[ " + userInfo.getLoggedUsername() + " ] Files found: "	+ files);

		String findInFile[] = files.split("\n");
		result.include("hostname", servidor.getHostname());
		result.include("qtdn", findInFile.length);
		result.include("findInFile", findInFile);

	}

	@Get("/downloadFile/{hostname}/{file}")
	public File downloadFile(String hostname, String file) {

		log.fine("[ " + userInfo.getLoggedUsername() + " ] Apagando Arquivo Temporário");
		File fileDelete = new File("tempFile.log");
		if (fileDelete.delete()) {
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Arquivo temporário removido. (tempFile.log)");
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Local Arquivo: " + fileDelete.getAbsolutePath());
		} else {
			log.fine("[ "	+ userInfo.getLoggedUsername() + " ] Arquivo temporário não encontrado. (tempFile.log)");
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Local Arquivo: " + fileDelete.getAbsolutePath());
		}

		SftpLogs getLogFile = new SftpLogs();

		result.include("title", "Download");

		// inserindo username na home:
		result.include("loggedUser", userInfo.getLoggedUsername());

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /downloadFile");

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
		// verificando se o filename começa com espaço
		if (file.startsWith(" ")) {
			log.fine("[ " + userInfo.getLoggedUsername() + " ] Arquivo " + file + " começa com espaço, removemdo.");
			String temp[] = file.split(" ");
			filename = temp[temp.length - 1];
		} else {
			String getName[] = file.split(" ");
			filename = getName[1];
		}

		String rfile = servidor.getLogDir() + "/" + filename;

		this.response.setContentType("application/octet-stream");
		this.response.setHeader("Content-disposition", "attachment; filename=" + filename + "");

		log.info("[ " + userInfo.getLoggedUsername() + " ] Fazendo download do arquivo " + filename + " do servidor " + hostname);

		String downloadResult = getLogFile.getFile(servidor.getUser(), servidor.getPass(),servidor.getIp(), servidor.getPort(), rfile);
		log.info("[ " + userInfo.getLoggedUsername() + " ] Resultado do download: " + downloadResult);
		return new File("tempFile.log");

	}
}