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

import br.com.hrstatus.repository.Repository;
import br.com.hrstatus.repository.impl.DataBaseRepository;

/**
 * Created by ataxexe on 9/1/16.
 *            fspolti
 */
public class Notification {

    private Repository repository = new DataBaseRepository();

    /**
     * @param message String
     * @return {@link DestinationSelector}
     */
    public DestinationSelector send(String message) {
        return new DestinationSelector(message);
    }

    /**
     * @param jndi Stirng
     * @return {@link MailJndiSelector}
     */
    public MailJndiSelector usingJndi(String jndi) {
        return new MailJndiSelector(jndi);
    }
}