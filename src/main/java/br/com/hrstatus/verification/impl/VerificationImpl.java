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

package br.com.hrstatus.verification.impl;

import br.com.hrstatus.action.databases.helper.IllegalVendorException;
import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.verification.Verification;
import br.com.hrstatus.verification.helper.VerificationHelper;
import com.jcraft.jsch.JSchException;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

/*
 * @author spolti
 */
@Service
public class VerificationImpl extends VerificationHelper implements Verification {

    protected final Logger log = Logger.getLogger(getClass().getName());

    public void databaseVerification(List<BancoDados> dataBases) throws IllegalVendorException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {

        for (BancoDados bancoDados : dataBases) {
            bancoDados.setServerTime(getTime());
            bancoDados.setLastCheck(bancoDados.getServerTime());

            // Decrypting password
            try {
                bancoDados.setPass(String.valueOf(Crypto.decode(bancoDados.getPass())));
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }

            try {

                String dateSTR = execQueryDate.getDate(bancoDados);

                log.fine("[ " + userInfo.getLoggedUsername() + " ] Hora obtida do banco de dados " + bancoDados.getHostname() + ": " + dateSTR);
                bancoDados.setClientTime(dateSTR);
                // Calculating time difference
                bancoDados.setDifference((differenceTime(bancoDados.getServerTime(), dateSTR)));

                if (bancoDados.getDifference() < 0) {
                    bancoDados.setDifference(bancoDados.getDifference() * -1);
                }

                if (bancoDados.getDifference() <= this.configurationDAO.getDiffirenceSecs()) {
                    bancoDados.setTrClass("success");
                    bancoDados.setStatus("OK");
                } else {
                    bancoDados.setTrClass("error");
                    bancoDados.setStatus("não OK");
                }
                try {
                    // Encrypting the password
                    bancoDados.setPass(encodePass.encode(bancoDados.getPass()));

                } catch (Exception e1) {
                    log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e1);
                }
                this.dbDAO.updateDataBase(bancoDados);

            } catch (JSchException e) {
                bancoDados.setStatus(e + "");
                bancoDados.setTrClass("error");
                try {
                    // Encrypting the password
                    bancoDados.setPass(encodePass.encode(bancoDados.getPass()));

                } catch (Exception e1) {

                    log.severe("[ " + userInfo.getLoggedUsername() + " ] Error: " + e1);
                }
                this.dbDAO.updateDataBase(bancoDados);
            } catch (IOException e) {
                bancoDados.setStatus(e + "");
                bancoDados.setTrClass("error");
                try {

                    // Encrypting the password
                    bancoDados.setPass(encodePass.encode(bancoDados.getPass()));

                } catch (Exception e1) {

                    log.severe("[ " + userInfo.getLoggedUsername()+ " ] Error: " + e1);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
                bancoDados.setStatus("Erro: " + e.getMessage());
                bancoDados.setTrClass("error");
                bancoDados.setPass(encodePass.encode(bancoDados.getPass()));
                this.dbDAO.updateDataBase(bancoDados);
            }
        }
    }
}