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

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.model.Configurations;
import br.com.hrstatus.security.CriptoJbossDSPassword;
import br.com.hrstatus.utils.UserInfo;
import br.com.hrstatus.utils.mail.MailSender;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/*
 * @author spolti
 */

@Resource
public class UtilsController {

    private Logger log = Logger.getLogger(UtilsController.class.getName());

    @Autowired
    private Result result;
    @Autowired
    private Configuration configurationDAO;
    private UserInfo userInfo = new UserInfo();
    private MailSender send = new MailSender();
    private CriptoJbossDSPassword cript = new CriptoJbossDSPassword();

    @SuppressWarnings("static-access")
    @Get("/utils/criptPassJboss/{password}")
    public void criptPassJboss(String password) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        log.info("[ Not Logged ] URI Called: /utils/criptPassJboss/");
        result.include("EncriptedPassword", cript.encode(password));
    }

    @Get("/sendMailtest")
    public void sendMailtest(String rcpt) throws Exception {
        log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /sendMailtest/" + rcpt);
        final Configurations configs = this.configurationDAO.getConfigs();
        try {
            final String resultMail = send.sendTestMail(configs.getMailFrom(), rcpt, configs.getJndiMail());
            result.include("info", resultMail).forwardTo(ConfigController.class).configServer();
        } catch (Exception e) {
            log.severe("[ " + userInfo.getLoggedUsername() + " ] Error by sending the test email: " + e);
            result.include("errors", "Erro ao enviar mensagem de teste: " + e).forwardTo(ConfigController.class).configServer();
        }
    }
}