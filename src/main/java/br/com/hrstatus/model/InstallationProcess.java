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

package br.com.hrstatus.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * @author spolti
 */

@Entity
@Table(name = "InstallationProcess")
public class InstallationProcess {

	@Id
	@Column(name = "id")
	@GeneratedValue
	private int id;
	
	@Column(name = "freshInstall")
	private boolean freshInstall;
	
	@Column(name = "installDate")
	private String installDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isFreshInstall() {
		return freshInstall;
	}

	public void setFreshInstall(boolean freshInstall) {
		this.freshInstall = freshInstall;
	}

	public String getInstallDate() {
		return installDate;
	}

	public void setInstallDate(String installDate) {
		this.installDate = installDate;
	}
		
}