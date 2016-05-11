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

import br.com.hrstatus.model.Lock;

import java.util.List;

/*
 * @author spolti
 */

public interface LockInterface {

    void insertLock(Lock lock);

    void insertLockScheduler(Lock lock, String schedulerName);

    void removeLock(Lock lock);

    void removeLockScheduler(Lock lock, String schedulerName);

    List<Lock> listLockedServices(String recurso);

    List<Lock> listLockedServicesScheduler(String recurso, String schedulerName);

    List<Lock> ListAllLocks();

    Lock getLockByID(int id);

}