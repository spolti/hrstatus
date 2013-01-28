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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.view.Results;
import br.com.ohsnap.hrstatus.dao.Iteracoes;
import br.com.ohsnap.hrstatus.dao.UsersInterface;
import br.com.ohsnap.hrstatus.model.Servidores;
import br.com.ohsnap.hrstatus.model.Users;
import br.com.ohsnap.hrstatus.security.Crypto;
import br.com.ohsnap.hrstatus.security.SpringEncoder;

@Resource
public class UpdateController {
	private Result result;
	private Iteracoes iteracoesDAO;
	private Validator validator;
	private UsersInterface usersDAO;

	public UpdateController(Result result, Iteracoes iteracoesDAO,
			Validator validator, UsersInterface usersDAO) {
		this.result = result;
		this.iteracoesDAO = iteracoesDAO;
		this.validator = validator;
		this.usersDAO = usersDAO;
	}

	@Get("/findForUpdateServer/{serverID}")
	@Post("/findForUpdateServer/{serverID}")
	public void findForUpdateServer(Servidores s, String serverID) {
		//inserindo html title no result
		result.include("title","Atualizar Servidor");
		
		// inserindo username noa home:
		Object LoggedObjectUser = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String LoggedUsername = ((UserDetails) LoggedObjectUser).getUsername();
		result.include("loggedUser", LoggedUsername);

		Logger.getLogger(getClass()).info("URI Called: /findForUpdateServer");
		Crypto decodePass = new Crypto();
		// String id_string = request.getParameter("serverID");
		int id = Integer.parseInt(serverID);
		Logger.getLogger(getClass()).info("id: " + id);

		Servidores server = this.iteracoesDAO.getServerByID(id);

		// Setando ID
		server.setId(id);

		// Descriptografando senha e a jogando no formulário
		try {

			String textPass = String
					.valueOf(decodePass.decode(server.getPass()));
			server.setPass(textPass);

		} catch (Exception e) {
			Logger.getLogger(getClass()).error(
					"Erro ao descriptografar senha: ", e);
		}

		// populating SO combobox
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

		result.include("server", server);

		if (s != null) {
			Logger.getLogger(getClass())
					.info("Objeto do tipo Servidores não está vazio, atribuindo valores.");
			server = s;
			// result.include("server", server);
		}
	}

	@Get("/updateServer")
	@Post("/updateServer")
	public void updateServer(Servidores server) {
		//inserindo html title no result
		result.include("title","Atualizar Servidor");
		
		// inserindo username noa home:
		Object LoggedObjectUser = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String LoggedUsername = ((UserDetails) LoggedObjectUser).getUsername();
		result.include("loggedUser", LoggedUsername);

		Crypto encodePass = new Crypto();
		Logger.getLogger(getClass()).info("URI Called: /updateServer");
		Pattern pattern = Pattern
				.compile("\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z");
		Matcher matcher = pattern.matcher(server.getIp());

		if (server.getIp().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo Ip deve ser informado", "Erro"));
		} else if (!matcher.matches()) {
			validator.add(new ValidationMessage("O ip " + server.getIp()
					+ " não é válido.", "Erro"));
		} else if (server.getHostname().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo Hostname deve ser informado", "Erro"));
		} else if (server.getUser().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo Usuário deve ser informado", "Erro"));
		} else if (server.getPass().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo Senha deve ser informado", "Erro"));
		} else if (server.getPort() <= 0 || server.getPort() >= 65536) {
			validator.add(new ValidationMessage(
					"O campo porta está incorreto ou vazio", "Erro"));
		} else if (server.getSO().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo SO deve ser informado", "Erro"));
		}
		validator.onErrorUsePageOf(UpdateController.class).findForUpdateServer(
				server, "");

		server.setSO(server.getSO().toUpperCase());

		// verificar se os campos estão vazios, se não estiverem não faz nada se
		// estiverem seta os campos com valores default

		try {

			// Critpografando a senha
			server.setPass(encodePass.encode(server.getPass()));

		} catch (Exception e) {
			Logger.getLogger(getClass()).error("Error: ", e);
		}

		this.iteracoesDAO.updateServer(server);

		// redirecto to /configClients
		result.redirectTo(ConfigController.class).configClients();
	}

	@Get("/findForUpdateUser/{username}")
	public void findForUpdateUser(Users u, String username) {
		Logger.getLogger(getClass()).info("URI Called: /findForUpdateUser");
		//inserindo html title no result
		result.include("title","Atualizar Usuário");
		
		// inserindo username noa home:
		Object LoggedObjectUser = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String LoggedUsername = ((UserDetails) LoggedObjectUser).getUsername();

		if (!username.equals(LoggedUsername.toString())) {
			result.use(Results.http()).sendError(403);
		} else {
			Logger.getLogger(getClass()).info("validação de usuário OK");
			result.include("loggedUser", LoggedUsername);

			Logger.getLogger(getClass()).info("URI Called: /findForUpdateUser");

			Users user = this.usersDAO.getUserByID(username);
			// setando username
			user.setUsername(username);

			result.include("user", user);

			if (user != null) {
				Logger.getLogger(getClass())
						.info("Objeto do tipo Users não está vazio, atribuindo valores.");
				u = user;
			}
		}

	}

	@Post("/updateUser")
	public void updateUser(Users user) {
		//inserindo html title no result
		result.include("title","Atualizar Usuário");
		
		Object LoggedObjectUser = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String LoggedUsername = ((UserDetails) LoggedObjectUser).getUsername();
		if (!user.getUsername().equals(LoggedUsername.toString())) {
			result.use(Results.http()).sendError(403);
		} else {
			result.include("loggedUser", LoggedUsername);

			Logger.getLogger(getClass()).info("URI Called: /updateUser");
			SpringEncoder encode = new SpringEncoder();

			if (user.getNome().isEmpty()) {
				validator.add(new ValidationMessage(
						"O campo Nome deve ser informado", "Erro"));
			} else if (user.getUsername().isEmpty()) {
				validator.add(new ValidationMessage(
						"O campo Username deve ser informado", "Erro"));
			} else if (!user.getPassword().isEmpty()
					&& !user.getConfirmPass().isEmpty()) {
				if (!user.getPassword().equals(user.getConfirmPass())) {
					validator.add(new ValidationMessage(
							"As senhas informadas não são iguais.", "Erro"));
				}
			} else if (user.getMail().isEmpty()) {
				validator.add(new ValidationMessage(
						"O campo E-mail deve ser informado", "Erro"));
			} else if (user.getAuthority().isEmpty()) {
				validator.add(new ValidationMessage(
						"O campo Perfil deve ser informado", "Erro"));
			}

			if (user.getPassword().isEmpty()) {
				// pegar senha do banco e setar objeto
				user.setPassword(this.usersDAO.getPass(user.getUsername()));
			} else {
				user.setPassword(encode.encodePassUser(user.getPassword()));
			}
			validator.onErrorUsePageOf(UpdateController.class)
					.findForUpdateUser(user, "");

			this.usersDAO.updateUser(user);

			result.redirectTo(ConfigController.class).configUser();
		}
	}
}
