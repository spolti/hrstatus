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

package br.com.hrstatus.verification.os;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.hrstatus.controller.HomeController;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.verification.Verification;
import br.com.hrstatus.verification.helper.VerificationHelper;
import com.jcraft.jsch.JSchException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.logging.Logger;

/*
 * @author spolti
 */

@Resource
public class NotFullVerification extends VerificationHelper {

    private Logger log = Logger.getLogger(NotFullVerification.class.getName());

    @Autowired
    private Result result;
    @Autowired
    private Verification verification;

    @SuppressWarnings("static-access")
    @Get("/home/startVerification/notFull")
    public void startNotFullVerification() throws InterruptedException, JSchException {

        // Inserting HTML title in the result
        result.include("title", "Hr Status Home");

        // Verifica se já tem alguma verificação ocorrendo...
        if (!resource.islocked("notOkverification")) {

            log.info("[ " + userInfo.getLoggedUsername() + " ] The resource notOkverification is not locked, locking and continuing.");

            // Inserting HTML title in the result
            result.include("title", "Hr Status Home");
            final List<Servidores> serverList = this.serversDAO.getServersNOKVerActive();

            if (serverList.size() <= 0) {
                log.info("[ " + userInfo.getLoggedUsername() + " ] No server found or no servers with active check.");
                result.include("info", "Nenhum servidor encontrado ou não há servidores com verficação ativa").forwardTo(HomeController.class).home("");

            } else {
                resource.lockRecurso("notOkverification");

                verification.serverVerification(serverList);

                final List<Servidores> resultServers = this.serversDAO.getServersNOKVerActive();
                result.include("class", "activeServer");
                result.include("server", resultServers).forwardTo(HomeController.class).home("");

            }
        } else {
            result.include("class", "activeServer");
            result.include("info", "O recurso notOkverification está locado, aguarde o término da mesma").forwardTo(HomeController.class).home("");
        }
        // Release the resource when the verification ends
        resource.releaseLock("notOkverification");
    }
}