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
import br.com.ohsnap.hrstatus.dao.Iteracoes;
import br.com.ohsnap.hrstatus.dao.UsersInterface;
import br.com.ohsnap.hrstatus.model.Servidores;
import br.com.ohsnap.hrstatus.model.Users;
import br.com.ohsnap.hrstatus.security.Crypto;
import br.com.ohsnap.hrstatus.security.SpringEncoder;

@Resource
public class CadastroController {

	private Result result;
	private Iteracoes iteracoesDAO;
	private Validator validator;
	private UsersInterface userDAO;

	public CadastroController(Result result, Iteracoes iteracoesDAO,
			Validator validator, UsersInterface userDAO) {
		this.result = result;
		this.iteracoesDAO = iteracoesDAO;
		this.validator = validator;
		this.userDAO = userDAO;
	}

	@Get("/newServer")
	@Post("/newServer")
	public void newServer(Servidores servidores) {
		//inserindo html tittle no result
		result.include("title","Registrar Servidor");
		
		//inserindo username na home:
		Object  LoggedObjectUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String LoggedUsername = ((UserDetails) LoggedObjectUser).getUsername();
		result.include("loggedUser", LoggedUsername);
		
		Logger.getLogger(getClass()).info("URI Called: /newServer");
		result.include("servidores", servidores);

		// populating SO combobox
		List<Servidores> so = this.iteracoesDAO.getListOfSO();
		int size = 	so.size();
		if ((size < 1) || (size < 4)){
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
		}else {
			result.include("SO", so);
		}
		

	}

	@Post("/registerServer")
	public void registerServer(Servidores servidores) {
		//inserindo html tittle no result
		result.include("title","Registrar Servidor");
		
		//inserindo username noa home:
		Object  LoggedObjectUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String LoggedUsername = ((UserDetails) LoggedObjectUser).getUsername();
		result.include("loggedUser", LoggedUsername);
		
		Logger.getLogger(getClass()).info("URI Called: /registerServer");
		Crypto encodePass = new Crypto();

		// Regex to validade IP
		Pattern pattern = Pattern
				.compile("\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z");
		Matcher matcher = pattern.matcher(servidores.getIp());

		if (servidores.getIp().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo Ip deve ser informado", "Erro"));
		} else if (!matcher.matches()) {
			validator.add(new ValidationMessage("O ip " + servidores.getIp()
					+ " não é válido.", "Erro"));
		} else if (servidores.getHostname().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo Hostname deve ser informado", "Erro"));
		} else if (servidores.getUser().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo Usuário deve ser informado", "Erro"));
		} else if (servidores.getPass().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo Senha deve ser informado", "Erro"));
		} else if (servidores.getPort() <= 0 || servidores.getPort() >= 65536) {
			validator.add(new ValidationMessage(
					"O campo porta está incorreto ou vazio", "Erro"));
		} else if (servidores.getSO().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo SO deve ser informado", "Erro"));
		}
		
		
		// populating SO combobox
		List<Servidores> so = this.iteracoesDAO.getListOfSO();
		int size = 	so.size();
		if ((size < 1) || (size < 4)){
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
		}else {
			result.include("SO", so);
		}
		
		validator.onErrorUsePageOf(CadastroController.class).newServer(
				servidores);

		result.include("servidores", servidores);

		servidores.setSO(servidores.getSO().toUpperCase());
		servidores.setStatus("Servidor ainda não foi verificado.");
		servidores.setTrClass("error");

		//result.redirectTo(HomeController.class).home("null");
		
		try {

			// Critpografando a senha
			servidores.setPass(encodePass.encode(servidores.getPass()));

		} catch (Exception e) {
			Logger.getLogger(getClass()).error("Error: ", e);
		}

		if (this.iteracoesDAO.insert_server(servidores) == 0) {
			result.include("msg", "Server " + servidores.getHostname()
					+ " was sucessfully registred.");
			// result.forwardTo("/newServer");
			Logger.getLogger(getClass()).info(
					"Server " + servidores.getHostname()
							+ " was sucessfully registred.");
			result.redirectTo(ConfigController.class).configClients();
		} else {
			validator.add(new ValidationMessage("Server "
					+ servidores.getHostname()
					+ " was not registred because already exists.", "Erro"));
			validator.onErrorForwardTo(CadastroController.class).newServer(
					servidores);
		}

	}

	@Get("/newUser")
	@Post("/newUser")
	public void newUser(Users user) {
		//inserindo html tittle no result
		result.include("title","Registrar Usuário");
		
		//inserindo username noa home:
		Object  LoggedObjectUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String LoggedUsername = ((UserDetails) LoggedObjectUser).getUsername();
		result.include("loggedUser", LoggedUsername);
		
		Logger.getLogger(getClass()).info("URI Called: /newUser");
		result.include("user", user);

	}

	@Post("/registerUser")
	public void registerUser(Users user) {
		//inserindo html tittle no result
		result.include("title","Registrar Usuário");
		//inserindo username noa home:
		Object  LoggedObjectUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String LoggedUsername = ((UserDetails) LoggedObjectUser).getUsername();
		result.include("loggedUser", LoggedUsername);
		
		Logger.getLogger(getClass()).info("URI Called: /registerUser");
		SpringEncoder encode = new SpringEncoder();
		
		//expressão regular para validar email
		Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
		Matcher m = p.matcher(user.getMail());
		
		if (user.getNome().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo Nome deve ser informado", "Erro"));
		} else if (user.getUsername().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo Username deve ser informado", "Erro"));
		} else if (user.getPassword().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo Password deve ser informado", "Erro"));
		} else if (user.getConfirmPass().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo Comfirme Password deve ser informado", "Erro"));
		} else if (!user.getPassword().equals(user.getConfirmPass())) {
			validator.add(new ValidationMessage(
					"As senhas informadas não são iguais.", "Erro"));
		} else if (user.getMail().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo E-mail deve ser informado", "Erro"));
		} else if(!m.find()){
			validator.add(new ValidationMessage(
					"Favor informe o e-mail corretamente.", "Erro"));			
		} else if (user.getAuthority().isEmpty()) {
			validator.add(new ValidationMessage(
					"O campo Perfil deve ser informado", "Erro"));
		}

		//Criptografando senha MD5 springframework
		user.setPassword(encode.encodePassUser(user.getPassword()));
		
		
		validator.onErrorUsePageOf(CadastroController.class).newUser(user);
		result.include("user", user);
		this.userDAO.saveORupdateUser(user);
		result.redirectTo(HomeController.class).home("null");
	}
}