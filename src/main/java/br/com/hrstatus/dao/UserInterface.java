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

import br.com.hrstatus.model.User;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public interface UserInterface extends Serializable {

    /*
    * Register the given user
    * @param Object Users
    */
    void registerUser(User user) throws Exception;

    /*
    * Delete the given user object
    * @param Users
    */
    void delete(User user);

    /*
    * List the registered users
    * @returns list containing all users
    */
    List<User> getUsers();

    /*
    * Search the given user
    * @returns the User object if found
    */
    User searchUser (String username);

    /*
    * Update the given user
    */
    void update (User user);

    /*
    * Get the locked users
    */
    List<User> getLockedUsers();
}