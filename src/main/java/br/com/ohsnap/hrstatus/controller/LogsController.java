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


package br.com.ohsnap.hrstatus.controller;

/*
 * @author spolti
 */

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.FileDownload;
import br.com.ohsnap.hrstatus.action.SftpLogs;
import br.com.ohsnap.hrstatus.dao.Iteracoes;
import br.com.ohsnap.hrstatus.model.Servidores;
import br.com.ohsnap.hrstatus.security.Crypto;
import br.com.ohsnap.hrstatus.utils.UserInfo;

import com.jcraft.jsch.JSchException;

@Resource
public class LogsController {

	private Result result;
	private Iteracoes iteracoesDAO;
	HttpServletResponse response;
	UserInfo userInfo = new UserInfo();
	
	public LogsController(Result result, Iteracoes iteracoesDAO, HttpServletResponse response) {
		this.result = result;
		this.iteracoesDAO = iteracoesDAO;
		this.response = response;
	}
	
	@Get("/selectServer")
	public void selectServer(){
		
		result.include("title","Selecione o Servidor");
		result.include("loggedUser", userInfo.getLoggedUsername());
		
		Logger.getLogger(getClass()).info("URI Called: /selectServer");
		
		List<Servidores>server = this.iteracoesDAO.getHostnamesWithLogDir();
		
		result.include("server",server);
		
	}
	
	@Get("/listLogFiles/{hostname}")
	public void listLogFiles(String hostname) throws JSchException, IOException{
		SftpLogs listLogs = new SftpLogs();
		
		result.include("title","Lista de Arquivos");
		//inserindo username na home:
		result.include("loggedUser", userInfo.getLoggedUsername());
		
		Logger.getLogger(getClass()).info("URI Called: /listLogFiles");
		Logger.getLogger(getClass()).info("Listing files of " + hostname);
		
		Servidores servidor = this.iteracoesDAO.getServerByHostname(hostname);
		
		//setando logDir
		result.include("logDir",servidor.getLogDir());
		
		try {
			servidor.setPass(String.valueOf(Crypto
					.decode(servidor.getPass())));
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
		
		String files = listLogs.showGetFiles(servidor.getUser(), servidor.getPass(), servidor.getIp(), servidor.getPort(), servidor.getLogDir());
		Logger.getLogger(getClass()).debug("Files foind: " + files);
		
		String listOfFiles[] = files.split("\n");
		result.include("hostname", servidor.getHostname());
		result.include("qtdn",listOfFiles.length);
		result.include("listOfFiles",listOfFiles);
			
	}
	@Get("/downloadFile/{hostname}/{file}")
	public File downloadFile(String hostname, String file){
		
		Logger.getLogger(getClass()).debug("Apagando Arquivo Temporário");
		File fileDelete = new File("tempFile.log");
		if (fileDelete.delete()){
			Logger.getLogger(getClass()).debug("Arquivo temporário removido. (tempFile.log)");
		}else {
			Logger.getLogger(getClass()).debug("Arquivo temporário não encontrado. (tempFile.log)");
		}
		
		SftpLogs getLogFile = new SftpLogs();
		
		result.include("title", "Download");

		// inserindo username na home:
		Object LoggedObjectUser = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String LoggedUsername = ((UserDetails) LoggedObjectUser).getUsername();
		result.include("loggedUser", LoggedUsername);
		
		Logger.getLogger(getClass()).info("URI Called: /downloadFile");
		
		Servidores servidor = this.iteracoesDAO.getServerByHostname(hostname);
		
 		try {
			servidor.setPass(String.valueOf(Crypto
					.decode(servidor.getPass())));
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
				
		String getName[] = file.split(" ");
		String filename = getName[1];
		
		String rfile = servidor.getLogDir() + "/" + filename;
 		 		
 		this.response.setContentType("application/octet-stream");
		this.response.setHeader("Content-disposition", "attachment; filename="+filename+"");
		
		Logger.getLogger(getClass()).info("Fazendo download do arquivo " + filename + " do servidor " + hostname);
		
		String downloadResult = getLogFile.getFile(servidor.getUser(), servidor.getPass(), servidor.getIp(), servidor.getPort(), rfile);
		Logger.getLogger(getClass()).info("Resultado do download: " + downloadResult);
		return new File("tempFile.log");
		

	}
}
