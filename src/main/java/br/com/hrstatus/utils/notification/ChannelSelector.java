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

package br.com.hrstatus.utils.notification;

import javax.transaction.Transactional;

/**
 * Created by ataxexe on 9/1/16.
 *            fspolti
 */
@Transactional
public class ChannelSelector {

    private final String message;
    private final String receiver;
    private final String subject;
    private String jndi;

    public ChannelSelector(String message, String receiver, String subject, String jndi) {
        this.message = message;
        this.receiver = receiver;
        this.subject = subject;
        this.jndi = jndi;
    }

    /**
     * Selects the channel that will be used to deliver a message
     * @param channel {@link Channel}
     * @return the message plus the {@link Channel} selected
     */
    public String by(Channel channel) {
        return channel.send(this.message, this.receiver, this.subject, this.jndi);
    }

}