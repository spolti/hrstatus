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

import br.com.hrstatus.model.PassExpire;
import br.com.hrstatus.model.Users;

/*
 * @author spolti
 */

public interface UsersInterface {

	public void saveORupdateUser(Users user);
	
	public List<Users> listUser();
	
	public boolean deleteUserByID(Users user);
	
	public Users getUserByID(String username);
	
	public void updateUser(Users user);
	
	public void saveORupdateUserNotLogged(Users user);
	
	public String getPass(String username);
	
	public int searchUser(String username);
	
	public String getMail (String username);
	
	public void setExpirePasswordTime(PassExpire passExpire);
	
	public int searchUserChangePass(String username);
	
	public List<PassExpire> getExpireTime();
	
	public void delUserExpireTime(PassExpire passExpire);
	
	public void delUserHasChangedPass(String username);

	public String getRole(String user);
	
	public boolean getFirstLogin(String username);
	
	public void lastActivityTimeStamp(String lastActivityTimeStamp);
	
	public List<Integer> getIds_access_server(String user);
}