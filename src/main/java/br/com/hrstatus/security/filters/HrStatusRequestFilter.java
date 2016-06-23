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

package br.com.hrstatus.security.filters;

import br.com.hrstatus.dao.RolesInterface;
import br.com.hrstatus.dao.UserInterface;
import br.com.hrstatus.model.Role;
import br.com.hrstatus.model.User;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */

@WebFilter(urlPatterns = {"/*"})
public class HrStatusRequestFilter implements Filter {

    private Logger log = Logger.getLogger(HrStatusRequestFilter.class.getName());
    private FilterConfig filterConfig;

    @Inject
    private User user;
    @Inject
    private Role role;
    @Inject
    private UserInterface userDao;
    @Inject
    private RolesInterface roleDao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    //dump headers and parameters. to activate set -Dbr.com.hrstatus.filter.dump.headers -Dbr.com.hrsatatus.filter.dump.reqParams -Dbr.com.hrstatus.filter.dump.all
//        HttpServletRequest req = (HttpServletRequest) request;
//
////        Map<String, String[]> params = req.getParameterMap();
//
//        for(String params: Collections.list(req.getParameterNames())){
//
//            log.info("Parameter " + params + " value: " + req.getParameter(params));
//        }
//
////        Enumeration headerNameTemp = req.getHeaderNames();
////
////        while (headerNameTemp.hasMoreElements()) {
////            String headerName = (String)headerNameTemp.nextElement();
////            log.info("header name " + headerName + ": " + req.getHeader(headerName));
////        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.fine("Destruindo filtro + " + HrStatusRequestFilter.class.getName());
    }

}