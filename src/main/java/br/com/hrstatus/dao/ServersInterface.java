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

import java.util.List;

import br.com.hrstatus.model.Servidores;

/*
 * @author spolti
 */

public interface ServersInterface {

	public int insert_server(Servidores server);
	
	public List<Servidores> getHostnames();
	
	public Servidores getServerByID(int id);
	
	public void updateServer(Servidores server);
	
	public boolean deleteServerByID(Servidores server);
	
	public int countLinux();
	
	public int countWindows();
	
	public int countUnix();
	
	public int countOther();
	
	public int countAllServers();
	
	public  List<Servidores> listServers();
	
	public List<Servidores> listServersVerActive();
	
	public List<Servidores> getServersOK();
	
	public List<Servidores> getServersNOK();
	
	public List<Servidores> getServersNOKVerActive();
	
	public int countServersOK();
	
	public int countServersNOK();
	
	public int countLinuxOK();
			
	public int countLinuxNOK();
		
	public int countUnixOK();
		
	public int countUnixNOK();
	
	public int countWindowsOK();
	
	public int countWindowsNOK();
	
	public int countOtherOK();
	
	public int countOtherNOK();
	
	public List<Servidores> getSOLinux();
	
	public List<Servidores> getSOWindows();
	
	public List<Servidores> getSOUnix();
	
	public List<Servidores> getSOOthers();
	
	public List<Servidores> getListOfSO();
	
	public List<Servidores> getHostnamesWithLogDir();
	
	public Servidores getServerByHostname(String hostname);
	
	public List<Servidores> listServerByID(int id);
	
	public int countServerWithLog();
	
}