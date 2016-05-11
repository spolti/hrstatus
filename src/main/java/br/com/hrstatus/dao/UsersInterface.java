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

import br.com.hrstatus.model.PassExpire;
import br.com.hrstatus.model.Users;

import java.util.List;

/*
 * @author spolti
 */

public interface UsersInterface {

    void saveORupdateUser(Users user);

    List<Users> listUser();

    boolean deleteUserByID(Users user);

    Users getUserByID(String username);

    Users getUserByIDNotLogged(String username);

    void updateUser(Users user);

    void updateUserNotLogged(Users user);

    void saveORupdateUserNotLogged(Users user);

    String getPass(String username);

    int searchUser(String username);

    int searchUserNotLogged(String username);

    String getMail(String username);

    String getMailNotLogged(String username);

    void setExpirePasswordTime(PassExpire passExpire);

    void setExpirePasswordTimeNotLogged(PassExpire passExpire);

    int searchUserChangePass(String username);

    int searchUserChangePassNotLogged(String username);

    List<PassExpire> getExpireTime();

    void delUserExpireTime(PassExpire passExpire);

    void delUserExpireTimeNotLogged(PassExpire passExpire);

    void delUserHasChangedPass(String username);

    String getRole(String user);

    boolean getFirstLogin(String username);

    void lastActivityTimeStamp(String lastActivityTimeStamp);

    List<Integer> getIds_access_server(String user);

    List<Users> getBlockedUsers();
}