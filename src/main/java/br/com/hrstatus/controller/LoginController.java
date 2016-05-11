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
import br.com.hrstatus.utils.PropertiesLoaderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

/*
 * @author spolti
 */

@Resource
public class LoginController {

    Logger log = Logger.getLogger(LoginController.class.getCanonicalName());

    @Autowired
    private Result result;
    private PropertiesLoaderImpl load = new PropertiesLoaderImpl();

    @SuppressWarnings("static-access")
    @Get("/login")
    public void login(String message) throws Exception {

        log.info("[ Not Logged ] URI Called: /login");

        // Loading HrStatus version information from property file
        final String version = load.getValor("version");
        result.include("version", version);
        result.include("info", message);
    }
}