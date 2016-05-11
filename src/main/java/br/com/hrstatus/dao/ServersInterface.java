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

package br.com.hrstatus.dao;

import br.com.hrstatus.model.Servidores;

import java.util.List;

/*
 * @author spolti
 */

public interface ServersInterface {

    int insert_server(Servidores server);

    List<Servidores> getHostnames();

    Servidores getServerByID(int id);

    void updateServer(Servidores server);

    void updateServerScheduler(Servidores server, String SchedulerName);

    boolean deleteServerByID(Servidores server);

    int countWindows();

    int countUnix();

    int countAllServers();

    List<Servidores> listServers();

    List<Servidores> listServersVerActive();

    List<Servidores> listServersVerActiveScheduler(String schedulerName);

    List<Servidores> getServersOK();

    List<Servidores> getServersNOK();

    List<Servidores> getServersNOKVerActive();

    int countServersOK();

    int countServersNOK();

    int countUnixOK();

    int countUnixNOK();

    int countWindowsOK();

    int countWindowsNOK();

    List<Servidores> getSOWindows();

    List<Servidores> getSOUnix();

    List<Servidores> getHostnamesWithLogDir();

    Servidores getServerByHostname(String hostname);

    List<Servidores> listServerByID(int id);

    int countServerWithLog();

    Servidores getServerLogDir(String hostname);

}