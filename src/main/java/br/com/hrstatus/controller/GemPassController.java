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
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.model.PassExpire;
import br.com.hrstatus.model.Users;
import br.com.hrstatus.security.SpringEncoder;
import br.com.hrstatus.utils.PassGenerator;
import br.com.hrstatus.utils.UserInfo;
import br.com.hrstatus.utils.date.DateParser;
import br.com.hrstatus.utils.date.DateUtils;
import br.com.hrstatus.utils.mail.MailSender;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

/*
 * @author spolti
 */

@Resource
public class GemPassController {

    private Logger log = Logger.getLogger(getClass().getName());

    @Autowired
    private UsersInterface userDAO;
    @Autowired
    private Configuration configurationDAO;
    @Autowired
    private Result result;
    private UserInfo userInfo = new UserInfo();
    private DateUtils dateUtils = new DateUtils();
    private DateParser parse = new DateParser();


    @Get("/atualizarCadastro/{username}")
    public void atualizarCadastro(String username) {

        // Inserting HTML title in the result
        result.include("title", "Atualizar Usuário");

        result.include("loggedUser", userInfo.getLoggedUsername());

        log.fine("[ " + userInfo.getLoggedUsername() + " ] Usuário logado: " + userInfo.getLoggedUsername());

        if (!username.equals(userInfo.getLoggedUsername().toString())) {
            result.use(Results.http()).sendError(403);
        } else {
            log.info("[ " + userInfo.getLoggedUsername() + " ] User validation OK");
            result.redirectTo(UpdateController.class).findForUpdateUser(null, userInfo.getLoggedUsername(), "changePass");
        }
    }

    @SuppressWarnings("static-access")
    @Post("/requestNewPass")
    public void requestNewPass(String username) throws Exception {

        log.info("[ Not Logged ] URI Called: /requestNewPass");
        log.info("[ Not Logged ] Username recebido: " + username);
        final SpringEncoder encode = new SpringEncoder();
        String password = "";
        final PassExpire passExpire = new PassExpire();
        String email_result = null;
        try {
            // Verifying if the user exists
            if (this.userDAO.searchUserNotLogged(username) == 1) {
                final Users user = this.userDAO.getUserByIDNotLogged(username);
                log.info("[ Not Logged ] User found.");
                final PassGenerator gemPass = new PassGenerator();
                final String mailSender = this.configurationDAO.getMailSenderNotLogged();
                final String jndiMail = this.configurationDAO.getJndiMailNotLogged();
                final String dest = this.userDAO.getMailNotLogged(username);
                final MailSender send = new MailSender();
                password = gemPass.gemPass();

                // Setting the expiration of the new password and storing the old password in the temporary table
                passExpire.setUsername(username);
                final String oldPwd = user.getPassword();
                passExpire.setOldPwd(oldPwd);

                // Getting the time and calculating the expiration time of the generated password
                final Date changeTime = parse.parser(dateUtils.getTime());
                passExpire.setChangeTime(changeTime.toString());

                // Calculating the sum
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(changeTime);
                calendar.add(Calendar.MINUTE, 30);

                // Assigning the sum for the variable.
                final Date expireTime = calendar.getTime();
                passExpire.setExpireTime(expireTime.toString());

                // Saving the new password in the database
                final String newPwd = encode.encodePassUser(password);
                user.setPassword(newPwd);
                passExpire.setNewPwd(newPwd);

                if (this.userDAO.searchUserChangePassNotLogged(username) != 1) {
                    user.setFirstLogin(true);
                    this.userDAO.updateUserNotLogged(user);
                    this.userDAO.setExpirePasswordTimeNotLogged(passExpire);

                    email_result = send.sendNewPass(mailSender, dest, jndiMail, password, username);

                    result.redirectTo(LoginController.class).login("Se o usuário for válido uma nova senha será enviada para seu e-mail. " + email_result);
                } else {
                    result.redirectTo(LoginController.class).login("Já foi solicitado uma troca de senha para este usuário, por favor cheque seu e-mail." + email_result);
                }

            } else {
                log.info("[ Not Logged ] User not Found.");
                result.redirectTo(LoginController.class).login("Se o usuário for válido uma nova senha será enviada para seu e-mail.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.redirectTo(LoginController.class).login("Se o usuário for válido uma nova senha será enviada para seu e-mail. " + email_result);
        }
    }
}