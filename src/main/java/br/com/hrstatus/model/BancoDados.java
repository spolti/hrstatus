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
@Table(name = "bancoDados")
public class BancoDados {

	@Id
	@Column(name = "id")
	@GeneratedValue
	private int id;
	
	@Column(name = "hostname")
	private String hostname;
	
	@Column(name = "ip")
	private String ip;

	@Column(name = "user")
	private String user;

	@Column(name = "pass")
	private String pass;

	@Column(name = "port")
	private int port;

	@Column(name = "vendor")
	private String vendor;
	
	@Column(name = "queryDate")
	private String queryDate;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "clientTime")
	private String clientTime;
	
	@Column(name = "serverTime")
	private String serverTime;
	
	@Column(name = "lastCheck")
	private String lastCheck;
	
	@Column(name = "Difference")
	private long Difference;
	
	@Column(name = "trClass")
	private String trClass;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getClientTime() {
		return clientTime;
	}

	public void setClientTime(String clientTime) {
		this.clientTime = clientTime;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public String getLastCheck() {
		return lastCheck;
	}

	public void setLastCheck(String lastCheck) {
		this.lastCheck = lastCheck;
	}

	public long getDifference() {
		return Difference;
	}

	public void setDifference(long difference) {
		Difference = difference;
	}

	public String getTrClass() {
		return trClass;
	}

	public void setTrClass(String trClass) {
		this.trClass = trClass;
	}

}
