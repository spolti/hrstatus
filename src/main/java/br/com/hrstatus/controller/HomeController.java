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
import br.com.caelum.vraptor.Validator;
import br.com.hrstatus.dao.InstallProcessInterface;
import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.utils.GetSystemInformation;
import br.com.hrstatus.utils.PropertiesLoaderImpl;
import br.com.hrstatus.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.logging.Logger;

/*
 * @author spolti
 */

@Resource
public class HomeController {

    Logger log = Logger.getLogger(HomeController.class.getCanonicalName());

    @Autowired
    private Result result;
    @Autowired
    private ServersInterface iteracoesDAO;
    @Autowired
    private Validator validator;
    @Autowired
    private UsersInterface userDAO;
    @Autowired
    private InstallProcessInterface ipi;

    private UserInfo userInfo = new UserInfo();
    private GetSystemInformation getSys = new GetSystemInformation();

    @SuppressWarnings("static-access")
    @Get("/home")
    public void home(String verification) {

        //Sending information to "About" page
        final PropertiesLoaderImpl load = new PropertiesLoaderImpl();
        final String version = load.getValor("version");
        result.include("version", version);
        final List<String> info = getSys.SystemInformation();
        result.include("jvmName", info.get(2));
        result.include("jvmVendor", info.get(1));
        result.include("jvmVersion", info.get(0));
        result.include("osInfo", info.get(3));
        result.include("installDate", ipi.getInstallationDate());


        // Inserting HTML title in the result
        result.include("title", "Hr Status Home");

        log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /home");

        result.include("loggedUser", userInfo.getLoggedUsername());

        // Verifying if is the first login after the password change or after the user registration
        final boolean isFirstLogin = this.userDAO.getFirstLogin(userInfo.getLoggedUsername());
        if (isFirstLogin) {
            log.fine("[ " + userInfo.getLoggedUsername() + " ] First login of " + userInfo.getLoggedUsername() + ": " + isFirstLogin);
            log.fine("[ " + userInfo.getLoggedUsername() + " ] Redirecting the user to the password change page");
            result.forwardTo(UpdateController.class).findForUpdateUser(null, userInfo.getLoggedUsername(), "changePass");
        } else {
            log.fine("[ " + userInfo.getLoggedUsername() + " ] First login of " + userInfo.getLoggedUsername() + ": " + isFirstLogin);
        }
    }

    @Get("/navbar")
    public void navbar() {
        log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /navbar");
    }

    @Get("/home/showByStatus/{status}")
    public void showByStatus(String status) {

        // Inserting HTML title in the result
        result.include("title", "Hr Status Home");

        log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /home/showByStatus/" + status);

        if ("OK".equals(status)) {
            final List<Servidores> list = this.iteracoesDAO.getServersOK();
            result.include("class", "activeServer");
            result.include("server", list).forwardTo(HomeController.class).home("");
            result.include("class", "activeServer");

        } else if (!"OK".equals(status)) {
            final List<Servidores> list = this.iteracoesDAO.getServersNOK();
            result.include("class", "activeServer");
            result.include("server", list).forwardTo(HomeController.class).home("");

        } else {
            validator.onErrorUsePageOf(HomeController.class).home("");
        }
    }
}