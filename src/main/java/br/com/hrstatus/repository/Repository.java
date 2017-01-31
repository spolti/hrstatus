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

package br.com.hrstatus.repository;

import br.com.hrstatus.model.Setup;
import br.com.hrstatus.model.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public interface Repository {

    /**
     * Persists the initial configuration needed when HrStatus Server is run for the first time
     */
    void initialImport();

    /**
     * Load the HrStatus configurations
     *
     * @return br.com.hrstatus.model.Setup
     */
    Setup loadConfiguration();

    /**
     * Returns the mail-session jndi-name that will be used to delivery emails.
     * It should be properly configured in the WildFly Server otherwise all notifications through email will fail. The Hrstatus server will automatically
     * identify the mail sessions configured.
     *
     * @return mail-session jndi-name
     */
    String mailJndi();

    /**
     * Return the email sender
     *
     * @return sender
     */
    String mailFrom();

    /**
     * Returns the Welcome message that will be displayed in the HrStatus Login page
     *
     * @return welcomeMessage
     */
    String welcomeMessage();

    /**
     * Returns the date when Hrstatus was installed.
     *
     * @return installationDate
     */
    LocalDateTime installationDate();

    /**
     * Persist the given Object
     *
     * @param object Object to be persisted
     * @param <T>    Type
     * @param <O>    Object
     * @return success and if in case of fail the stacktrace
     */
    <T, O> T register(O object);

    /**
     * Delete the given object
     *
     * @param object   to be deleted
     * @param <T>      Type
     * @param <Object> Object
     */
    <T, Object> void delete(Object object);

    /**
     * List all objects
     *
     * @param clazz   Type of the objects to be searched
     * @param <T>     Type
     * @param <Clazz> return type class
     * @return List&#60;T&#62;
     */
    <T, Clazz> List<T> list(Clazz clazz);

    /**
     * Search an object based in the Class filtered by the parameterName
     *
     * @param clazz          Object Class
     * @param parameterName  parameter filter
     * @param parameterValue parameter value
     * @param <T>            Type of the Object
     * @param <Clazz>        Object class type
     * @return a single result type &#60;T&#62;
     */
    <T, Clazz> T search(Clazz clazz, String parameterName, Object parameterValue);

    /**
     * Update the given object
     *
     * @param object Object to be updated
     * @return success and if in case of fail the stacktrace
     */
    Object update(Object object);

    /**
     * List the blocked users, the reasons can be:
     * <p>first time login</p>
     * <p>more than 3 login failures</p>
     * <p>manually blocked by a admin</p>
     *
     * @return the list of the blocked users
     */
    List<User> getLockedUsers();

}