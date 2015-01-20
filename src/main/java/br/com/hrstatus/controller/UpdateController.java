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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.view.Results;
import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.model.Users;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.security.SpringEncoder;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Resource
public class UpdateController {

	Logger log =  Logger.getLogger(UpdateController.class.getCanonicalName());
	
	@Autowired
	private Result result;
	@Autowired
	private ServersInterface iteracoesDAO;
	@Autowired
	private Validator validator;
	@Autowired
	private UsersInterface usersDAO;
	@Autowired
	private BancoDadosInterface BancoDadosDAO;
	@Autowired
	private HttpServletRequest request;
	UserInfo userInfo = new UserInfo();
	Crypto passUtil = new Crypto();
	
	
	@SuppressWarnings("static-access")
	@Get("/findForUpdateServer/{serverID}")
	public void findForUpdateServer(Servidores s, String serverID) {
		
		// Inserting HTML title in the result
		result.include("title", "Atualizar Servidor");
		// Inserting the Logged username in the home page
		result.include("loggedUser", userInfo.getLoggedUsername());
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /findForUpdateServer");
		
		//The server ID is integer.
		int id = Integer.parseInt(serverID);
		
		log.info("[ " + userInfo.getLoggedUsername() + " ] Server id selected for update: " + id);
		Servidores server = this.iteracoesDAO.getServerByID(id);

		// setting the server ID
		server.setId(id);

		// Decrypting  the password and putting it in the form
		try {

			String textPass = String.valueOf(passUtil.decode(server.getPass()));
			server.setPass(textPass);

		} catch (Exception e) {
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Error to decrypting the password:  " + e);
		}

		// populating OS combobox
		List<Servidores> so = this.iteracoesDAO.getListOfSO();
		int size = so.size();
		if ((size < 1) || (size < 4)) {
			so.removeAll(so);

			Servidores linux = new Servidores();
			Servidores windows = new Servidores();
			Servidores unix = new Servidores();
			Servidores outro = new Servidores();
			linux.setSO("LINUX");
			windows.setSO("WINDOWS");
			unix.setSO("UNIX");
			outro.setSO("OUTRO");
			so.add(linux);
			so.add(windows);
			so.add(unix);
			so.add(outro);
			result.include("SO", so);
		} else {
			result.include("SO", so);
		}

		List<Users> userCorrect = this.usersDAO.listUser();
		List<Users> userFinal = new ArrayList<Users>();
		for(Users user : userCorrect){
			for (Users us : server.getUsers()){
				if (user.getUsername().equals(us.getUsername())){
					log.fine("*************************************************************************");
					log.fine("Server " + server.getHostname() + " is mapped for the user " + user.getUsername());
					log.fine("*************************************************************************");
				}
			}
			userFinal.add(user);
		}

		result.include("userFinal", userFinal);
		result.include("server", server);

		if (s != null) {
			log.info("[ " + userInfo.getLoggedUsername() + " ] Server Object is not empty, atributing its values.");
			server = s;
		}
	}

	@SuppressWarnings("static-access")
	@Post("/updateServer")
	public void updateServer(Servidores server, String[] idUser, String OSserver) {

		// Inserting HTML title in the result
		result.include("title", "Atualizar Servidor");
		// Inserting the Logged username in the home page
		result.include("loggedUser", userInfo.getLoggedUsername());

		
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /updateServer");
		Pattern pattern = Pattern.compile("\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z");
		Matcher matcher = pattern.matcher(server.getIp());

		if (server.getIp().isEmpty()) {
			validator.add(new ValidationMessage("O campo Ip deve ser informado", "Erro"));
		} else if (!matcher.matches()) {
			validator.add(new ValidationMessage("O ip " + server.getIp() + " não é válido.", "Erro"));
		} else if (server.getHostname().isEmpty()) {
			validator.add(new ValidationMessage("O campo Hostname deve ser informado", "Erro"));
		} else if (server.getUser().isEmpty()) {
			validator.add(new ValidationMessage("O campo Usuário deve ser informado", "Erro"));
		} else if (server.getPass().isEmpty()) {
			validator.add(new ValidationMessage("O campo Senha deve ser informado", "Erro"));
		} else if (server.getPort() <= 0 || server.getPort() >= 65536) {
			validator.add(new ValidationMessage("O campo porta está incorreto ou vazio", "Erro"));
		} else if (OSserver.isEmpty()) {
			validator.add(new ValidationMessage("O campo SO deve ser informado", "Erro"));
		}
		validator.onErrorUsePageOf(UpdateController.class).findForUpdateServer(server, "");		
		
		server.setSO(OSserver.toUpperCase());
		
		try {
			// Encrypting the password
			server.setPass(passUtil.encode(server.getPass()));

		} catch (Exception e) {
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e);
		}
		

		if (!idUser[0].equals("notNull")) {
			List<Users> idUserAccessServer = new ArrayList<Users>();
			for (int i = 0; i < idUser.length; i++) {
				log.fine("****************************************************");
				log.fine("Received Username: " + idUser[i]);
				if (!idUser[i].equals("notNull")) {
					idUserAccessServer.add(this.usersDAO.getUserByID(idUser[i]));
					log.fine("Received Username: " + idUser[i]);
					log.fine("****************************************************");
				}
			}
			server.setUsers(idUserAccessServer); 
		}
				
		this.iteracoesDAO.updateServer(server);
			
		// redirecto to /configClients
		result.redirectTo(ConfigController.class).configClients();
	}

	@SuppressWarnings("static-access")
	@Get("/findForUpdateDataBase/{dataBaseID}")
	public void findForUpdateDataBase(BancoDados db, String dataBaseID) {
		
		// Inserting HTML title in the result
		result.include("title", "Atualizar Banco de Dados");
		// Inserting the Logged username in the home page
		result.include("loggedUser", userInfo.getLoggedUsername());

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /findForUpdateDataBase");
		
		int id = Integer.parseInt(dataBaseID);
		log.info("[ " + userInfo.getLoggedUsername() + " ] DataBase id selected for update: " + id);

		BancoDados dataBase = this.BancoDadosDAO.getDataBaseByID(id);

		// Setting the database ID
		dataBase.setId(id);

		//  Decrypting  the password and putting it in the form
		try {

			String textPass = String.valueOf(passUtil.decode(dataBase.getPass()));
			dataBase.setPass(textPass);

		} catch (Exception e) {
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Error to decrypting the password: " + e);
		}

		// populating SO combobox
		ArrayList<String> VENDOR = new ArrayList<String>();
		VENDOR.add("MySQL");
		VENDOR.add("ORACLE");
		VENDOR.add("PostgreSQL");
		VENDOR.add("SqlServer");
		VENDOR.add("DB2");
		result.include("VENDOR", VENDOR);
		result.include("dataBase", dataBase);

		if (db != null) {
			log.info("[ " + userInfo.getLoggedUsername() + " ] Database Object is not empty, atributing its values.");
			dataBase = db;
		}
	}

	@SuppressWarnings("static-access")
	@Post("/updateDataBase")
	public void updateDataBase(BancoDados dataBase) {

		// Inserting HTML title in the result
		result.include("title", "Atualizar Banco de Dados");
		// Inserting the Logged username in the home page
		result.include("loggedUser", userInfo.getLoggedUsername());

		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /updateServer");
		Pattern pattern = Pattern.compile("\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z");
		Matcher matcher = pattern.matcher(dataBase.getIp());

		if (dataBase.getIp().isEmpty()) {
			validator.add(new ValidationMessage("O campo Ip deve ser informado", "Erro"));
		} else if (!matcher.matches()) {
			validator.add(new ValidationMessage("O ip " + dataBase.getIp() + " não é válido.", "Erro"));
		} else if (dataBase.getHostname().isEmpty()) {
			validator.add(new ValidationMessage("O campo Hostname deve ser informado", "Erro"));
		} else if (dataBase.getUser().isEmpty()) {
			validator.add(new ValidationMessage("O campo Usuário deve ser informado", "Erro"));
		} else if (dataBase.getPass().isEmpty()) {
			validator.add(new ValidationMessage("O campo Senha deve ser informado", "Erro"));
		} else if (dataBase.getPort() <= 0 || dataBase.getPort() >= 65536) {
			validator.add(new ValidationMessage("O campo porta está incorreto ou vazio", "Erro"));
		} else if (dataBase.getVendor().isEmpty()) {
			validator.add(new ValidationMessage("O campo SO deve ser informado", "Erro"));
		}
		
		if (dataBase.getQueryDate().isEmpty()) {
			if (dataBase.getVendor().toUpperCase().equals("MYSQL")) {
				dataBase.setQueryDate("SELECT NOW() AS date;");
			}
			if (dataBase.getVendor().toUpperCase().equals("ORACLE")) {
				dataBase.setQueryDate("select sysdate from dual");
			}
			if (dataBase.getVendor().toUpperCase().equals("SQLSERVER")) {
				dataBase.setQueryDate("SELECT GETDATE();");
			}
			if (dataBase.getVendor().toUpperCase().equals("POSTGRESQL")) {
				dataBase.setQueryDate("SELECT now();");
			}
			if (dataBase.getVendor().toUpperCase().equals("DB2")) {
				dataBase.setQueryDate("SELECT current date FROM sysibm.sysdummy1;");
			}
		}
		
		validator.onErrorUsePageOf(UpdateController.class).findForUpdateDataBase(dataBase, "");

		try {

			// Encrypting the password
			dataBase.setPass(passUtil.encode(dataBase.getPass()));

		} catch (Exception e) {
			log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e);
		}

		this.BancoDadosDAO.updateDataBase(dataBase);

		result.redirectTo(ConfigController.class).configDataBases();
	}

	@Get("/findForUpdateUser/{username}/{action}")
	public void findForUpdateUser(Users u, String username, String action) {
		
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /findForUpdateUser");
		// Inserting HTML title in the result
		result.include("title", "Atualizar Usuário");

		String LoggedUsername = userInfo.getLoggedUsername();
		List<Servidores> FullLogServer = this.iteracoesDAO.getHostnamesWithLogDir();
		List<Servidores> server = new ArrayList<Servidores>();

		// obtendo roles do usuário:
		boolean isAdmin = request.isUserInRole("ROLE_ADMIN");
		boolean isUser = request.isUserInRole("ROLE_USER");

		if (username.toUpperCase().equals("ADMIN")) {
			if ((isAdmin) && (LoggedUsername.toUpperCase().equals("ADMIN"))) {
				log.fine("[ " + userInfo.getLoggedUsername() + " ] The user admin id editing himself.");
			} else {
				log.info("[ " + userInfo.getLoggedUsername() + " ] The user " + userInfo.getLoggedUsername() + " does not have permissions to change the Administrator account. it will be reported to the Admins.");
				// implementar a funcionalidade de se alguém que não seja o próprio admin tentar alterar os dados do admin uma notificação é enviada para os administradores.
				result.use(Results.http()).sendError(403);
			}
		}

		if (!username.equals(LoggedUsername.toString()) && (isUser)) {
			result.use(Results.http()).sendError(403);
		} else if (action.equals("changePass")) {
			log.info("[ " + userInfo.getLoggedUsername() + " ] User validation OK");
			
			result.include("loggedUser", LoggedUsername);
			log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /changePass");

			Users user = this.usersDAO.getUserByID(username);
			// setting the username
			user.setUsername(username);
			user.setFirstLogin(false);
			result.include("isDisabled", "disabled");
			result.include("user", user);

			for (Servidores u1 : FullLogServer) {
				for (Servidores sv : user.getServer()) {
					if (u1.getId() == sv.getId()) {
						log.info("The user " + user + " have permission the read the logs of the server "	+ sv.getHostname());
						u1.setSelected("selected");
					}
				}
				server.add(u1);
			}

			result.include("server", server);

			if (user != null) {
				log.info("[ " + userInfo.getLoggedUsername() + " ] User Object is not empty, atributing its values.");
				u = user;
			}
		} else {
			if (isAdmin) {
				result.include("loggedUser", LoggedUsername);

				log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /findForUpdateUser");

				Users user = this.usersDAO.getUserByID(username);
				// setting the username
				user.setUsername(username);

				result.include("user", user);

				for (Servidores u1 : FullLogServer) {
					for (Servidores sv : user.getServer()) {
						if (u1.getId() == sv.getId()) {
							log.info("The user " + user + " have permission the read the logs of the server "	+ sv.getHostname());
							u1.setSelected("selected");
						}
					}
					server.add(u1);
				}

				result.include("server", server);

				if (user != null) {
					log.info("[ " + userInfo.getLoggedUsername() + " ] User Object is not empty, atributing its values.");
					u = user;
				}
			} else {
				result.use(Results.http()).sendError(403);
			}
		}
	}

	@SuppressWarnings("static-access")
	@Post("/updateUser")
	public void updateUser(Users user, String[] idServer, boolean checkall) {
	
		// Inserting HTML title in the result
		List<Servidores> FullLogServer = this.iteracoesDAO.getHostnamesWithLogDir();
		List<Servidores> server = new ArrayList<Servidores>();
		result.include("title", "Atualizar Usuário");

		// Obtaining the user roles
		boolean isAdmin = request.isUserInRole("ROLE_ADMIN");
		boolean isUser = request.isUserInRole("ROLE_USER");

		if (!user.getUsername().equals(userInfo.getLoggedUsername().toString()) && (isUser)) {
			result.use(Results.http()).sendError(403);
		} else {
			result.include("loggedUser", userInfo.getLoggedUsername());
			result.include("isDisabled", "disabled");

			log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /updateUser");
			SpringEncoder encode = new SpringEncoder();

			if (user.getNome().isEmpty()) {
				validator.add(new ValidationMessage("O campo Nome deve ser informado", "Erro"));
			} else if (user.getUsername().isEmpty()) {
				validator.add(new ValidationMessage("O campo Username deve ser informado", "Erro"));
			} else if (checkall) {
				log.fine("[ " + userInfo.getLoggedUsername() + " ] The checkbox select all server is checked.");
				List<Servidores> idAccessServers = new ArrayList<Servidores>();
				idAccessServers = this.iteracoesDAO.getHostnamesWithLogDir();
				user.setServer(idAccessServers);

			} else if (!checkall && idServer != null) {
				List<Servidores> idAccessServers = new ArrayList<Servidores>();
				for (int i = 0; i < idServer.length; i++) {
					if (!idServer[i].equals("notNull")) {
						idAccessServers.add(this.iteracoesDAO.getServerByID(Integer.parseInt(idServer[i])));
						log.fine("Server ID received: " + idServer[i]);
					}
				}
				user.setServer(idAccessServers); 

			}
			if (!user.getPassword().isEmpty() || !user.getConfirmPass().isEmpty()) {
				if (!user.getPassword().equals(user.getConfirmPass()) || !user.getConfirmPass().equals(user.getPassword())) {
					validator.add(new ValidationMessage("As senhas informadas não são iguais.", "Erro"));
				}
			}
			
			if (user.getAuthority() == null) {
				// Obtaining the role of t he logged user from database if it come blank from the form
				String role = this.usersDAO.getRole(userInfo.getLoggedUsername());
				user.setAuthority(role);
				if (!user.getAuthority().equals(role)){
					log.info("[ " + userInfo.getLoggedUsername() + " ] Unauthorized way to change the user ROLE. Rolling back the original value.");
					user.setAuthority(role);
				}
			}
			if (!user.getPassword().isEmpty() || !user.getConfirmPass().isEmpty()) {
				if (user.getPassword().equals(user.getConfirmPass())) {

					log.fine("[ " + userInfo.getLoggedUsername() + " ] Verifying the user password complexity.");
					List<String> passVal = new ArrayList<String>();
					Map<String, String> map = new HashMap<String, String>();
					map = br.com.hrstatus.security.PasswordPolicy.verifyPassComplexity(user.getPassword());
					Object[] valueMap = map.keySet().toArray();
					for (int i = 0; i < valueMap.length; i++) {
						if (map.get(valueMap[i]).equals("false")) {
							passVal.add(map.get(valueMap[i + 1]));
						}
					}
					for (int j = 0; j < passVal.size(); j++) {
						validator.add(new ValidationMessage(passVal.get(j),"Erro"));
					}

				}
			}
			
			if (user.getMail().isEmpty()) {
				validator.add(new ValidationMessage("O campo E-mail deve ser informado", "Erro"));
			} 
			
			if (idServer[0].indexOf("notNull") >= 0) {
				log.info("[ " + userInfo.getLoggedUsername() + " ] The server list for the user is empty.");
			}
			
			if (user.getPassword().isEmpty()) {
				user.setPassword(this.usersDAO.getPass(user.getUsername()));
			} else {
				user.setPassword(encode.encodePassUser(user.getPassword()));
			}
			
			Users returnOnValidtion = this.usersDAO.getUserByID(user.getUsername());
			for (Servidores u1 : FullLogServer) {
				for (Servidores sv : returnOnValidtion.getServer()) {
					if (u1.getId() == sv.getId()) {
						log.fine("[ " + userInfo.getLoggedUsername() + " ] The user have permissions to read the logs from " + sv.getHostname());
						u1.setSelected("selected");
					}
				}
				server.add(u1);
			}

			result.include("server", server);
			validator.onErrorUsePageOf(UpdateController.class).findForUpdateUser(user, "", "");

			if (!user.getUsername().equals(userInfo.getLoggedUsername().toString()) && !(isAdmin || isUser)) {
				result.use(Results.http()).sendError(403);
			} else {
				this.usersDAO.updateUser(user);
				if (this.usersDAO.searchUserChangePass(userInfo.getLoggedUsername()) == 1) {
					log.info("The user " + userInfo.getLoggedUsername() + " requested the change of the password does little time. Please wait.");
					this.usersDAO.delUserHasChangedPass(userInfo.getLoggedUsername());
				}
			}

			result.redirectTo(HomeController.class).home("");
		}
	}
}