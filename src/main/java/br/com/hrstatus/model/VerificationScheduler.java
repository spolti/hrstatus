package br.com.hrstatus.model;

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


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * @author spolti
 */

@Entity
@Table(name = "VerificationScheduler")
public class VerificationScheduler {

	@Id
	@Column(name = "schedulerId")
	@GeneratedValue
	private int schedulerId;
	
	@Column(name = "schedulerName")
	private String schedulerName;
	
	@Column(name = "everyday", nullable = false)
	private boolean everyday;
	
	@Column(name = "defaultScheduler", nullable = false)
	private boolean defaultScheduler;
	
	@Column(name = "finished", nullable = false)
	private boolean finished;
	
	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	public boolean isFinished() {
		return finished;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isDefaultScheduler() {
		return defaultScheduler;
	}

	public void setDefaultScheduler(boolean defaultScheduler) {
		this.defaultScheduler = defaultScheduler;
	}

	public int getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(int schedulerId) {
		this.schedulerId = schedulerId;
	}

	public String getSchedulerName() {
		return schedulerName;
	}

	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}

	public boolean isEveryday() {
		return everyday;
	}

	public void setEveryday(boolean everyday) {
		this.everyday = everyday;
	} 
	
}