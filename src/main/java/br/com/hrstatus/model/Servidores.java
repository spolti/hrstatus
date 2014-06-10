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

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Servidores")
public class Servidores {

	@Override
	public String toString() {
		return ""+SO;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue
	private int id;
	
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinTable(name = "USER_SERVER", joinColumns = { @JoinColumn(name = "id") }, inverseJoinColumns = { @JoinColumn(name = "username") })
	private List<Users> users;
	
	//@Column(name = "hostname")
	private String hostname;
	
	@Column(name = "ip")
	private String ip;

	@Column(name = "user")
	private String user;

	@Column(name = "pass")
	private String pass;

	@Column(name = "port")
	private int port;

	@Column(name = "SO")
	private String SO;

	@Column(name = "status")
	private String status;
	
	@Column(name = "clientTime")
	private String clientTime;
	
	@Column(name = "serverTime")
	private String serverTime;
	
	@Column(name = "lastCheck")
	private String lastCheck;
	
	@Column(name = "difference")
	private long difference;
	
	@Column(name = "trClass")
	private String trClass;
	
	@Column(name = "logDir")
	private String logDir;

	@Column(name = "suCommand")
	private String suCommand;
	
	@Column(name = "verify", length = 3)
	private String verify;
	
	@Transient
	private String selected;
	
		
	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public String getTrClass() {
		return trClass;
	}

	public void setTrClass(String trClass) {
		this.trClass = trClass;
	}

	public long getDifference() {
		return difference;
	}

	public void setDifference(long l) {
		this.difference = l;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public String getClientTime() {
		return clientTime;
	}

	public void setClientTime(String clientTime) {
		this.clientTime = clientTime;
	}

	public String getSO() {
		return SO;
	}

	public void setSO(String sO) {
		SO = sO;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

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
	
	public String getLastCheck() {
		return lastCheck;
	}

	public void setLastCheck(String lastCheck) {
		this.lastCheck = lastCheck;
	}

	public String getLogDir() {
		return logDir;
	}

	public void setLogDir(String logDir) {
		this.logDir = logDir;
	}

	public String getSuCommand() {
		return suCommand;
	}

	public void setSuCommand(String suCommand) {
		this.suCommand = suCommand;
	}

	public List<Users> getUsers() {
		return users;
	}

	public void setUsers(List<Users> users) {
		this.users = users;
	}
	
}