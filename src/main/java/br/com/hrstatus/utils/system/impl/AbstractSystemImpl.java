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

package br.com.hrstatus.utils.system.impl;

import br.com.hrstatus.utils.system.System;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class AbstractSystemImpl implements System {

    private final Logger log = Logger.getLogger(AbstractSystemImpl.class.getName());


    @Override
    public String getServerHttpAddress() {

        final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();


        return null;
    }
}