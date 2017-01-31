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

package br.com.hrstatus.security.interceptor;

import br.com.hrstatus.security.interceptor.event.AuthenticatedEvent;
import br.com.hrstatus.security.interceptor.event.FailedAuthenticatedEvent;
import br.com.hrstatus.security.interceptor.event.LoggedOutEvent;
import io.undertow.security.api.NotificationReceiver;
import io.undertow.security.api.SecurityNotification;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import javax.enterprise.inject.spi.CDI;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public final class HrStatusAuthHandler implements HttpHandler {

    private final HttpHandler next;
    private final SecurityNotificationReceiver NOTIFICATION_RECEIVER = new SecurityNotificationReceiver();
    private String FAILED_AUTH_USER;

    public HrStatusAuthHandler(final HttpHandler next) {
        this.next = next;
    }

    /**
     * Handle a http request to filter logout failures to manually trigger a {@link FailedAuthenticatedEvent} event
     *
     * @param exchange {@link HttpServerExchange}
     * @throws Exception for any issue
     */
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        //trigger failed authentication event
        if (exchange.getResponseHeaders().get("UT010031") != null && !exchange.getSecurityContext().isAuthenticated()) {
            FAILED_AUTH_USER = exchange.getResponseHeaders().get("UT010031").get(0);
            //force an failedAuthentication to trigger the event
            exchange.getSecurityContext().authenticationFailed(exchange.getResponseHeaders().get("UT010031").get(0), null);
            exchange.getResponseHeaders().get("UT010031").clear();
        }

        exchange.getSecurityContext().registerNotificationReceiver(NOTIFICATION_RECEIVER);
        next.handleRequest(exchange);

    }

    /**
     * CDI based event trigger
     */
    private class SecurityNotificationReceiver implements NotificationReceiver {

        @Override
        public void handleNotification(final SecurityNotification notification) {

            switch (notification.getEventType()) {
                case AUTHENTICATED:
                    CDI.current().getBeanManager().fireEvent(new AuthenticatedEvent(notification, notification.getAccount().getPrincipal()));
                    break;
                case LOGGED_OUT:
                    CDI.current().getBeanManager().fireEvent(new LoggedOutEvent(notification, notification.getAccount().getPrincipal()));
                    break;
                case FAILED_AUTHENTICATION:
                    CDI.current().getBeanManager().fireEvent(new FailedAuthenticatedEvent(notification, FAILED_AUTH_USER));
                    break;
                default:
                    break;
            }
        }
    }
}