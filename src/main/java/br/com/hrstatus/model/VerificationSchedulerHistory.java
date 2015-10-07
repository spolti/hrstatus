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


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * @author spolti
 */

@Entity
@Table(name = "VerificationSchedulerHistory")
public class VerificationSchedulerHistory {

	@Id
	@Column(name = "schedulerHistoryId")
	@GeneratedValue
	private int schedulerHistoryId;
	
	@Column(name = "schedulerName")
	private String schedulerName;
	
	@Column(name = "everyday", nullable = false)
	private boolean everyday;
	
	@Column(name = "defaultScheduler", nullable = false)
	private boolean defaultScheduler;
	
	@Column(name = "finished")
	private boolean finished;
	
	@Column(name = "enabled")
	private boolean enabled;
	
	@Column(name = "startedAt")
	private Date startedAt;
	
	@Column(name = "finishedAt")
	private Date finishedAt;

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public Date getFinishedAt() {
		return finishedAt;
	}

	public void setFinishedAt(Date finishedAt) {
		this.finishedAt = finishedAt;
	}

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
		return schedulerHistoryId;
	}

	public void setSchedulerId(int schedulerId) {
		this.schedulerHistoryId = schedulerId;
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