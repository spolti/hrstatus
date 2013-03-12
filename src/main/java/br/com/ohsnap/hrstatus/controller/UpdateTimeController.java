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

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.ohsnap.hrstatus.dao.Iteracoes;
import br.com.ohsnap.hrstatus.model.Servidores;

@Resource
public class UpdateTimeController {

	Servidores servidor = new Servidores();

	private Iteracoes iteracoesDAO;

	public UpdateTimeController(Iteracoes iteracoesDAO) {
		this.iteracoesDAO = iteracoesDAO;
	}

	@Get("updateTimeSelectedClients/{ids}")
	public void updateTimeSelectedClients(String ids) {
		Logger.getLogger(getClass()).info(
				"URI Called: /updateTimeSelectedClients");

		String servidores[] = ids.split(",");
		Logger.getLogger(getClass()).debug(
				"Tentando atualizar data/hora do servidor(es): " + ids);

		int id = 0;

		for (int i = 0; i < servidores.length; i++) {
			id = Integer.parseInt(servidores[i]);
			servidor = this.iteracoesDAO.getServerByID(id);

			Logger.getLogger(getClass()).info("Dados obtidos do servidor:");

			
			
//			Logger.getLogger(getClass()).info(servidor.getClientTime());
//			Logger.getLogger(getClass()).info(servidor.getDifference());
//			Logger.getLogger(getClass()).info(servidor.getDifference());
//			Logger.getLogger(getClass()).info(servidor.getId());
//			Logger.getLogger(getClass()).info(servidor.getIp());
//			Logger.getLogger(getClass()).info(servidor.getLastCheck());
//			Logger.getLogger(getClass()).info(servidor.getLogDir());
//			Logger.getLogger(getClass()).info(servidor.getPass());
//			Logger.getLogger(getClass()).info(servidor.getPort());
//			Logger.getLogger(getClass()).info(servidor.getServerTime());
//			Logger.getLogger(getClass()).info(servidor.getSO());
//			Logger.getLogger(getClass()).info(servidor.getStatus());
//			Logger.getLogger(getClass()).info(servidor.getTrClass());
//			Logger.getLogger(getClass()).info(servidor.getUser());
//			Logger.getLogger(getClass()).info(servidor.getSuCommand());
			
			
		}

	}

	@Get("/updateTimeAllClients")
	public void updateTimeAllClients() {
		Logger.getLogger(getClass()).info("URI Called: /updateTimeAllClients");
		Logger.getLogger(getClass())
				.debug("Tentando atualizar data/hora de todos os servidores desatualizados.");
	}
}
