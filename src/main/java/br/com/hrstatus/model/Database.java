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
import br.com.hrstatus.model.support.deserializer.CustomSupportedDatabaseDeserializer;
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
@Table(name = "RESOURCE_DATABASES")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Database {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private int id;

    @Column(name = "hostname", nullable = false, unique = true)
    private String hostname;

    @Column(name = "address", nullable = false, unique = true)
    private String address;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "port", nullable = false)
    private int port;

    @Column(name = "vendor", nullable = false)
    private Enum vendor;

    @Column(name = "queryDate", nullable = false)
    private String queryDate;

    @Column(name = "status", nullable = false)
    private Enum status;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "serverTime")
    private String serverTime;

    @Column(name = "lastCheck")
    private String lastCheck;

    @Column(name = "difference")
    private long difference;

    @Column(name = "instance", nullable = false)
    private String instance;

    // Used only by SQL Server
    @Column(name = "db_name")
    private String db_name;

    @Column(name = "verify", nullable = false)
    private boolean verify;

    public int getId() {
        return id;
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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Enum getVendor() {
        return vendor;
    }

    @JsonProperty("vendor")
    @JsonDeserialize(using = CustomSupportedDatabaseDeserializer.class)
    public void setVendor(Enum vendor) {
        this.vendor = vendor;
    }

    public String getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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
        return difference;
    }

    public void setDifference(long difference) {
        this.difference = difference;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getDb_name() {
        return db_name;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
    }

    public boolean isVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }

    @Override
    public String toString() {
        return "Database{" +
                "id=" + id +
                ", hostname='" + hostname + '\'' +
                ", address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", port=" + port +
                ", vendor=" + vendor +
                ", queryDate='" + queryDate + '\'' +
                ", status=" + status +
                ", timestamp='" + timestamp + '\'' +
                ", serverTime='" + serverTime + '\'' +
                ", lastCheck='" + lastCheck + '\'' +
                ", difference=" + difference +
                ", instance='" + instance + '\'' +
                ", db_name='" + db_name + '\'' +
                ", verify=" + verify +
                '}';
    }
}