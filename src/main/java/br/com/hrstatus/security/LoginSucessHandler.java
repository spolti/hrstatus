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

package br.com.hrstatus.security;

import br.com.hrstatus.dao.UsersInterface;
import br.com.hrstatus.model.Users;
import br.com.hrstatus.utils.UserInfo;
import br.com.hrstatus.utils.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/*
 *Spring Framework
 *Customization, rewrite LoginSuccessHandler
 */

@Service
public class LoginSucessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private Logger log = Logger.getLogger(LoginSucessHandler.class.getName());

    @Autowired
    private UsersInterface userDAO;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {

        final UserInfo userInfo = new UserInfo();
        final DateUtils dt = new DateUtils();

        final Users user = userDAO.getUserByID(userInfo.getLoggedUsername());
        final String lastLoginTime = dt.getTime();
        user.setLastLogin(lastLoginTime);
        log.info("[ " + userInfo.getLoggedUsername() + " ] Successful login at " + lastLoginTime + " from " + sourceAddress(request));
        userDAO.updateUser(user);

        super.setDefaultTargetUrl("/home");

        request.getSession().setMaxInactiveInterval(30 * 30);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    public String sourceAddress(HttpServletRequest request) {

        final String IP = request.getRemoteAddr();
        return IP;
    }
}