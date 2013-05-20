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

/*
 * @author spolti
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Configurations")
public class Configurations {

	@Id
	@Column(name = "id")
	@GeneratedValue
	private int id;
	
	@Column(name = "difference")
	private int difference;
	
	@Column(name = "mailFrom")
	private String mailFrom;

	@Column(name = "subject")
	private String subject;
	
	@Column(name = "dests")
	private String dests;
	
	@Column(name = "jndiMail")
	private String jndiMail;
	
	@Column(name = "ntpServer")
	private String ntpServer;
	
	@Column(name = "updateNtpIsActive")
	private boolean updateNtpIsActive;
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDests() {
		return dests;
	}

	public void setDests(String dests) {
		this.dests = dests;
	}

	public String getJndiMail() {
		return jndiMail;
	}

	public void setJndiMail(String jndiMail) {
		this.jndiMail = jndiMail;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDifference() {
		return difference;
	}

	public void setDifference(int difference) {
		this.difference = difference;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getNtpServer() {
		return ntpServer;
	}

	public void setNtpServer(String ntpServer) {
		this.ntpServer = ntpServer;
	}

	public boolean isUpdateNtpIsActive() {
		return updateNtpIsActive;
	}

	public void setUpdateNtpIsActive(boolean updateNtpIsActive) {
		this.updateNtpIsActive = updateNtpIsActive;
	}

}
