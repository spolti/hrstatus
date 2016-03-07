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
    ResourcesManagement resource;
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

            List<Servidores> list = this.serversDAO.listServersVerActive();
            if (list.size() <= 0) {
                log.info("[ " + userInfo.getLoggedUsername() + " ] No server found or no servers with active check.");
                result.include("info", "Nenhum servidor encontrado ou não há servidores com verficação ativa").forwardTo(HomeController.class).home("");

            } else {
                // locar recurso.
                resource.lockRecurso("verificationFull");
                for (Servidores servidores : list) {
                    // if Linux
                    if (servidores.getSO().equals("UNIX") && servidores.getVerify().equals("SIM")) {
                        servidores.setServerTime(getTime());
                        servidores.setLastCheck(servidores.getServerTime());
                        // Decrypting password
                        try {
                            servidores.setPass(String.valueOf(Crypto.decode(servidores.getPass())));
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

                        try {
                            String dateSTR = ExecRemoteCommand.exec(servidores.getUser(), servidores.getIp(), servidores.getPass(), servidores.getPort(), "/bin/date");
                            log.fine("[ " + userInfo.getLoggedUsername() + " ] Time recieved from the server " + servidores.getHostname() + ": " + dateSTR);
                            servidores.setClientTime(dateSTR);
                            // Calculating time difference
                            servidores.setDifference(differenceTime(servidores.getServerTime(), dateSTR));

                            if (servidores.getDifference() < 0) {
                                servidores.setDifference(servidores.getDifference() * -1);
                            }

                            if (servidores.getDifference() <= this.configurationDAO.getDiffirenceSecs()) {
                                servidores.setTrClass("success");
                                servidores.setStatus("OK");
                            } else {
                                servidores.setTrClass("error");
                                servidores.setStatus("não OK");
                            }
                            try {
                                // Encrypting the password
                                servidores.setPass(encodePass.encode(servidores.getPass()));

                            } catch (Exception e1) {
                                log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e1);
                            }
                            this.serversDAO.updateServer(servidores);

                        } catch (JSchException e) {
                            servidores.setStatus(e + "");
                            servidores.setTrClass("error");
                            try {
                                // Encrypting the password
                                servidores.setPass(encodePass.encode(servidores.getPass()));

                            } catch (Exception e1) {
                                log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e1);
                            }
                            this.serversDAO.updateServer(servidores);
                        } catch (IOException e) {
                            servidores.setStatus(e + "");
                            servidores.setTrClass("error");
                            try {

                                // Encrypting the password
                                servidores.setPass(encodePass.encode(servidores.getPass()));

                            } catch (Exception e1) {
                                log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e1);
                            }
                            this.serversDAO.updateServer(servidores);
                        }

                    } else if (servidores.getVerify().equals("NAO")) {
                        log.info("[ " + userInfo.getLoggedUsername() + " ] - The server " + servidores.getHostname() + " has the verification inactive, it will not be verified.");
                    }

                    // if Windows
                    if (servidores.getSO().equals("WINDOWS") && servidores.getVerify().equals("SIM")) {
                        servidores.setServerTime(getTime());
                        servidores.setLastCheck(servidores.getServerTime());
                        try {

                            String dateSTR = ExecCommand.Exec(servidores.getIp(), "I");
                            if (dateSTR == null || dateSTR == "") {
                                log.fine("The net time -I parameter returuns null, trying the parameter -S");
                                dateSTR = ExecCommand.Exec(servidores.getIp(), "S");
                            }
                            log.fine("[ " + userInfo.getLoggedUsername() + " ] Time retrieved from the server " + servidores.getHostname() + ": " + dateSTR);
                            servidores.setClientTime(dateSTR);
                            // Calculating time difference
                            servidores.setDifference(differenceTime(servidores.getServerTime(), dateSTR));

                            if (servidores.getDifference() < 0) {
                                servidores.setDifference(servidores.getDifference() * -1);
                            }

                            if (servidores.getDifference() <= this.configurationDAO.getDiffirenceSecs()) {
                                servidores.setTrClass("success");
                                servidores.setStatus("OK");
                            } else if (dateSTR == null || dateSTR == "") {
                                servidores.setTrClass("error");
                                servidores.setStatus("Não foi possível obter data/hora deste servidor, verique conectividade.");
                                servidores.setDifference(00);
                            } else {
                                servidores.setTrClass("error");
                                servidores.setStatus("não OK");
                            }
                            this.serversDAO.updateServer(servidores);
                        } catch (IOException e) {
                            servidores.setStatus(e + "");
                            servidores.setTrClass("error");
                            this.serversDAO.updateServer(servidores);
                        }

                    } else if (servidores.getVerify().equals("NAO")) {
                        log.info("[ " + userInfo.getLoggedUsername() + " ] - The server " + servidores.getHostname() + " has the verification inactive, it will not be verified.");
                    }

                }

                result.include("server", list).forwardTo(HomeController.class).home("");
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