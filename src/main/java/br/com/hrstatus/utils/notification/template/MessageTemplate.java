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

package br.com.hrstatus.utils.notification.template;

import br.com.hrstatus.utils.system.HrstatusSystem;
import br.com.hrstatus.utils.system.impl.AbstractSystemImpl;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class MessageTemplate {

    private static HrstatusSystem sys = new AbstractSystemImpl();

    public static String NEW_USER_SUBJECT = "HrStatus - Informações de acesso";


    /**
     * Returns a html format Message containing the new user information
     *
     * @param username String
     * @param password String
     * @return message formatted in html
     */
    public static String newUserMessage(String username, String password) {
        final String url = sys.getServerHttpAddress();
        return "<html>"
                + "<body>"
                + "<div style='text-align:center; width: 100%;'>"
                + "<div style='width: 700px; margin: 0 auto;'>"
                + "<a href='http://" + url + "/hs/home'>"
                + "<img src='http://" + url + "/hs/image/up.jpg' height='125' width='700'/></a><br>"
                + "<h2><br><p align='left' style='width:630px; margin: 0 auto;'>Olá <a style='color: blue;'>"
                + username
                + "</a><br> Sua nova é senha: <a style='color: blue;'>"
                + password
                + "</a><br>"
                + " Para acesso utilize a url: <br><a href='http://" + url + "/hs/'> HrStatus </p></h2>"
                + "<br><a href='http://" + url + "/hs/home'>"
                + "<img src='http://" + url + "/hs/image/down.jpg' height='125' width='700'/></a>"
                + "</div>" + "</div>" + "</body>" + "</html>";
    }
}