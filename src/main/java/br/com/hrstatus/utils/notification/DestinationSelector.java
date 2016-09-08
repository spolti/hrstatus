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

/**
 * Created by ataxexe on 9/1/16.
 */
public class DestinationSelector {

    private String message;
    private String  subject = "HrStatus";


    public DestinationSelector(String message) {
        this.message = message;
    }

    public DestinationSelector(String message, String subject) {
        this.message = message;
        this.subject =  !subject.equals(null) ? subject : this.subject;
    }

    public ChannelSelector to(String receiver) {
        return new ChannelSelector(this.message, receiver, this.subject);
    }

    public DestinationSelector subject(String subject) {
        this.subject = subject;
        return this;
    }

}