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

import br.com.hrstatus.model.support.VerificationStatus;
import br.com.hrstatus.model.support.deserializer.CustomStatusDeserializer;
import br.com.hrstatus.model.support.deserializer.CustomSupportedOperatingSystemDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Entity
@Table(name = "RESOURCE_OPERATING_SYSTEMS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OperatingSystem {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private int id;

    @Column(name = "hostname", nullable = false)
    private String hostname;

    @Column(name = "address", nullable = false, unique = true)
    private String address;

    @Column(name = "port")
    private int port;

    @Column(name = "username", nullable = true)
    private String username;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "type", nullable = false)
    private Enum type;

    @Column(name = "status", nullable = false)
    private Enum status;

    @Column(name = "osTime")
    private String osTime;

    @Column(name = "hrstatusTime")
    private String hrstatusTime;

    @Column(name = "lastCheck")
    private String lastCheck;

    @Column(name = "difference")
    private long difference;

    @Column(name = "logDir")
    private String logDir;

    @Column(name = "suCommand")
    private String suCommand;

    @Column(name = "verify", nullable = false)
    private boolean verify;

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Enum getType() {
        return type;
    }

    @JsonProperty("type")
    @JsonDeserialize(using = CustomSupportedOperatingSystemDeserializer.class)
    public void setType(Enum type) {
        this.type = type;
    }

    public Enum getStatus() {
        return status;
    }

    @JsonProperty("status")
    @JsonDeserialize(using = CustomStatusDeserializer.class)
    public void setStatus(Enum status) {
        if (status == null) {
            this.status = VerificationStatus.NOT_VERIFIED;
        }
        this.status = status;
    }

    public String getOsTime() {
        return osTime;
    }

    public void setOsTime(String osTime) {
        this.osTime = osTime;
    }

    public String getHrstatusTime() {
        return hrstatusTime;
    }

    public void setHrstatusTime(String hrstatusTime) {
        this.hrstatusTime = hrstatusTime;
    }

    public String getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(String lastCheck) {
        this.lastCheck = lastCheck;
    }

    public long getDifference() {
        return difference;
    }

    public void setDifference(long difference) {
        this.difference = difference;
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

    public boolean isToVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }

    public void disableVerification() {
        this.verify = false;
    }

    public void enableVerification() {
        this.verify = true;
    }

    @Override
    public String toString() {
        return "OperatingSystem{" +
                "id=" + id +
                ", hostname='" + hostname + '\'' +
                ", address='" + address + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", osTime='" + osTime + '\'' +
                ", hrstatusTime='" + hrstatusTime + '\'' +
                ", lastCheck='" + lastCheck + '\'' +
                ", difference=" + difference +
                ", logDir='" + logDir + '\'' +
                ", suCommand='" + suCommand + '\'' +
                ", verify=" + verify +
                '}';
    }
}