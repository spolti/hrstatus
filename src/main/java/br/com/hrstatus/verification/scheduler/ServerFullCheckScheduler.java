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

package br.com.hrstatus.verification.scheduler;

import br.com.hrstatus.action.os.unix.ExecRemoteCommand;
import br.com.hrstatus.action.os.windows.ExecCommand;
import br.com.hrstatus.model.Lock;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.verification.helper.VerificationHelper;
import com.jcraft.jsch.JSchException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

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

@Service
@Configurable
public class ServerFullCheckScheduler extends VerificationHelper {

    protected final Logger log = Logger.getLogger(getClass().getName());

    @SuppressWarnings("static-access")
    public void startFullVerification(String schedulerName) throws InterruptedException, JSchException {

        log.info("[ " + schedulerName + " ] Initializing a full verification.");

        lockedResource.setRecurso("verificationFull");
        lockedResource.setUsername(schedulerName);
        final List<Lock> lockList = this.lockDAO.listLockedServicesScheduler("verificationFull", schedulerName);
        if (lockList.size() != 0) {
            for (Lock lock : lockList) {
                log.info("[ " + schedulerName + " ] The resource verificationFull is locked by the user " + lock.getUsername());
                log.info("O recurso verificationFull está locado pelo usuário " + lock.getUsername() + ", aguarde o término da mesma");

            }
        } else {
            log.info("[ " + schedulerName + " ] The resource verificationFull is not locked, locking and continuing.");

            final List<Servidores> list = this.serversDAO.listServersVerActiveScheduler(schedulerName);
            if (list.size() <= 0) {
                log.info("[ " + schedulerName + " ] No server found or no servers with active check.");
                log.info("Nenhum servidor encontrado ou não há servidores com verficação ativa");

            } else {
                // locar recurso.
                lockDAO.insertLockScheduler(lockedResource, schedulerName);
                for (Servidores servidores : list) {
                    // if Linux
                    if (servidores.getSO().equals("LINUX") && servidores.getVerify().equals("SIM")) {
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
                            final String dateSTR = ExecRemoteCommand.exec(servidores.getUser(), servidores.getIp(), servidores.getPass(), servidores.getPort(), "/bin/date");
                            log.fine("[ " + schedulerName + " ] Time received from the server " + servidores.getHostname() + ": " + dateSTR);
                            servidores.setClientTime(dateSTR);
                            // Calculating time difference
                            servidores.setDifference(differenceTime(servidores.getServerTime(), dateSTR));

                            if (servidores.getDifference() < 0) {
                                servidores.setDifference(servidores.getDifference() * -1);
                            }

                            if (servidores.getDifference() <= this.configurationDAO.getDiffirenceSecsScheduler(schedulerName)) {
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
                                log.severe("[ " + schedulerName + " ] Error: " + e1);
                            }
                            this.serversDAO.updateServerScheduler(servidores, schedulerName);

                        } catch (JSchException e) {
                            servidores.setStatus(e + "");
                            servidores.setTrClass("error");
                            try {
                                // Encrypting the password
                                servidores.setPass(encodePass.encode(servidores.getPass()));

                            } catch (Exception e1) {
                                log.severe("[ " + schedulerName + " ] Error: " + e1);
                            }
                            this.serversDAO.updateServerScheduler(servidores, schedulerName);
                        } catch (IOException e) {
                            servidores.setStatus(e + "");
                            servidores.setTrClass("error");
                            try {

                                // Encrypting the password
                                servidores.setPass(encodePass.encode(servidores.getPass()));

                            } catch (Exception e1) {
                                log.severe("[ " + schedulerName + " ] Error: " + e1);
                            }
                            this.serversDAO.updateServerScheduler(servidores, schedulerName);
                        }

                    } else if (servidores.getVerify().equals("NAO")) {
                        log.info("[ " + schedulerName + " ] - The server " + servidores.getHostname() + " has the verification inactive, it will not be verified.");
                    }

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
                            final String dateSTR = ExecRemoteCommand.exec(servidores.getUser(), servidores.getIp(), servidores.getPass(), servidores.getPort(), "/bin/date");
                            log.fine("[ " + schedulerName + " ] Time retrieved from the server " + servidores.getHostname() + ": " + dateSTR);
                            servidores.setClientTime(dateSTR);
                            // Calculating time difference
                            servidores.setDifference(differenceTime(servidores.getServerTime(), dateSTR));

                            if (servidores.getDifference() < 0) {
                                servidores.setDifference(servidores.getDifference() * -1);
                            }
                            if (servidores.getDifference() <= this.configurationDAO.getDiffirenceSecsScheduler(schedulerName)) {
                                servidores.setTrClass("success");
                                servidores.setStatus("OK");
                            } else {
                                servidores.setTrClass("error");
                                servidores.setStatus("não OK");
                            }
                            try {

                                // Encrypting the password
                                servidores.setPass(encodePass.encode(servidores.getPass()));

                            } catch (Exception e) {
                                log.severe("[ " + schedulerName + " ] Error: " + e);
                            }
                            this.serversDAO.updateServerScheduler(servidores, schedulerName);
                        } catch (JSchException e) {
                            servidores.setStatus(e + "");
                            servidores.setTrClass("error");
                            try {

                                // Encrypting the password
                                servidores.setPass(encodePass.encode(servidores.getPass()));

                            } catch (Exception e1) {
                                log.severe("[ " + schedulerName + " ] Error: " + e1);
                            }
                            this.serversDAO.updateServerScheduler(servidores, schedulerName);
                        } catch (IOException e) {
                            servidores.setStatus(e + "");
                            servidores.setTrClass("error");
                            try {

                                // Encrypting the password
                                servidores.setPass(encodePass.encode(servidores.getPass()));

                            } catch (Exception e1) {
                                log.severe("[ " + schedulerName + " ] Error: " + e1);
                            }
                            this.serversDAO.updateServerScheduler(servidores, schedulerName);
                        }

                    } else if (servidores.getVerify().equals("NAO")) {
                        log.info("[ " + schedulerName + " ] - Server verification on " + servidores.getHostname() + "  is disabled, it will not be verified.");
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
                            log.fine("[ " + schedulerName + " ] Time retrieved from the server " + servidores.getHostname() + ": " + dateSTR);
                            servidores.setClientTime(dateSTR);
                            // Calculating time difference
                            servidores.setDifference(differenceTime(servidores.getServerTime(), dateSTR));

                            if (servidores.getDifference() < 0) {
                                servidores.setDifference(servidores.getDifference() * -1);
                            }

                            if (servidores.getDifference() <= this.configurationDAO.getDiffirenceSecsScheduler(schedulerName)) {
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
                            this.serversDAO.updateServerScheduler(servidores, schedulerName);
                        } catch (IOException e) {
                            servidores.setStatus(e + "");
                            servidores.setTrClass("error");
                            this.serversDAO.updateServerScheduler(servidores, schedulerName);
                        }

                    } else if (servidores.getVerify().equals("NAO")) {
                        log.info("[ " + schedulerName + " ] - The server " + servidores.getHostname() + " has the verification inactive, it will not be verified.");
                    }

                    // if Others
                    if (servidores.getSO().equals("OUTRO") && servidores.getVerify().equals("SIM")) {
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
                            final String dateSTR = ExecRemoteCommand.exec(servidores.getUser(), servidores.getIp(), servidores.getPass(), servidores.getPort(), "/bin/date");
                            log.fine("[ " + schedulerName + " ] Time retrieved from the server " + servidores.getHostname() + ": " + dateSTR);
                            servidores.setClientTime(dateSTR);
                            // Calculating time difference
                            servidores.setDifference(differenceTime(servidores.getServerTime(), dateSTR));


                            if (servidores.getDifference() < 0) {
                                servidores.setDifference(servidores.getDifference() * -1);
                            }
                            if (servidores.getDifference() <= this.configurationDAO.getDiffirenceSecsScheduler(schedulerName)) {
                                servidores.setTrClass("success");
                                servidores.setStatus("OK");
                            } else {
                                servidores.setTrClass("error");
                                servidores.setStatus("não OK");
                            }
                            try {

                                // Encrypting the password
                                servidores.setPass(encodePass.encode(servidores.getPass()));

                            } catch (Exception e) {
                                log.severe("[ " + schedulerName + " ] Error: " + e);
                            }
                            this.serversDAO.updateServerScheduler(servidores, schedulerName);
                        } catch (JSchException e) {
                            servidores.setStatus(e + "");
                            servidores.setTrClass("error");
                            try {

                                // Critpografando a senha
                                servidores.setPass(encodePass.encode(servidores.getPass()));

                            } catch (Exception e1) {
                                log.severe("[ " + schedulerName + " ] Error: " + e1);
                            }
                            this.serversDAO.updateServerScheduler(servidores, schedulerName);
                        } catch (IOException e) {
                            servidores.setStatus(e + "");
                            servidores.setTrClass("error");
                            try {

                                // Critpografando a senha
                                servidores.setPass(encodePass.encode(servidores.getPass()));

                            } catch (Exception e1) {
                                log.severe("[ " + schedulerName + " ] Error: " + e1);
                            }
                            this.serversDAO.updateServerScheduler(servidores, schedulerName);
                        }

                    } else if (servidores.getVerify().equals("NAO")) {
                        log.info("[ " + schedulerName + " ] - The server " + servidores.getHostname() + " has the verification inactive, it will not be verified.");
                    }
                }
            }
            // releasing the resource.
            log.info("[ " + schedulerName + " ] Verification finished, releasing the resource " + lockedResource.getRecurso());
            lockDAO.removeLockScheduler(lockedResource, schedulerName);
        }
    }
}