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
import br.com.hrstatus.action.os.unix.ExecRemoteCommand;
import br.com.hrstatus.action.os.windows.ExecCommand;
import br.com.hrstatus.controller.HomeController;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.resrources.ResourcesManagement;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.verification.helper.VerificationHelper;
import br.com.hrstatus.verification.impl.VerificationImpl;
import com.jcraft.jsch.JSchException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Logger;

/*
 * @author spolti
 */

@Resource
public class FullVerification extends VerificationHelper {

    protected final Logger log = Logger.getLogger(getClass().getName());

    @Autowired
    private Result result;
    @Autowired
    public VerificationImpl verification;

    private String LoggedUsername = userInfo.getLoggedUsername();

    @SuppressWarnings("static-access")
    @Get("/home/startVerification/full")
    public void startFullVerification() throws InterruptedException, JSchException {

        // Inserting HTML title in the result
        result.include("title", "Hr Status Home");

        log.info("[ " + userInfo.getLoggedUsername() + " ] URI called: /home/startVerification/full");

        log.info("[ " + userInfo.getLoggedUsername() + " ] Initializing a full verification.");

        // Verifica se já tem alguma verificação ocorrendo...
        if (!resource.islocked("verificationFull")) {
            log.info("[ " + userInfo.getLoggedUsername() + " ] The resource verificationFull is not locked, locking and continuing.");

            List<Servidores> serverList = this.serversDAO.listServersVerActive();
            if (serverList.size() <= 0) {
                log.info("[ " + userInfo.getLoggedUsername() + " ] No server found or no servers with active check.");
                result.include("info", "Nenhum servidor encontrado ou não há servidores com verficação ativa").forwardTo(HomeController.class).home("");

            } else {
                // locar recurso.
                resource.lockRecurso("verificationFull");

                verification.serverVerification(serverList);

                List<Servidores> checkedServers = this.serversDAO.listServersVerActive();
                result.include("server", checkedServers).forwardTo(HomeController.class).home("");
                result.include("class", "activeServer");

            }
        } else {
            result.include("class", "activeServer");
            result.include("info", "O recurso verificationFull está locado, aguarde o término da mesma").forwardTo(HomeController.class).home("");
        }
        // Release the resource when the verification ends
        resource.releaseLock("verificationFull");
    }
}