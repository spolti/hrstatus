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

package br.com.hrstatus.utils;

/*
 *Spring Framework 
 *Customization, rewrite LoginSuccessHandler
 */

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class LogoutListener implements ApplicationListener<SessionDestroyedEvent> {

    public void onApplicationEvent(SessionDestroyedEvent event)
    {
    	
    	try{
    	 SecurityContext securityContext = (SecurityContext) event.getSecurityContext();
    	 UserDetails ud = (UserDetails) securityContext.getAuthentication().getPrincipal();
         Logger.getLogger(getClass()).info("Sessão expirou ou foi realizado logout para o usuário " + ud.getUsername());
         
    	}catch (Exception e){
    		 Logger.getLogger(getClass()).debug("Não existe usuário na sessão");
    	}
    }
}