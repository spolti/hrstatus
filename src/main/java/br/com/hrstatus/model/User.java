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

import br.com.hrstatus.model.support.deserializer.CustomRolesDeserializer;
import br.com.hrstatus.model.support.deserializer.CustomSupportedOperatingSystemDeserializer;
import br.com.hrstatus.security.PasswordUtils;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.Fetch;

import javax.inject.Inject;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Entity
@Table(name = "USERS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {

    @Id
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    //@JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "mail", nullable = false, unique = true)
    private String mail;

    @Column(name = "firstLogin")
    private boolean firstLogin;

    @Column(name = "lastLogin")
    private String lastLogin;

    @Column(name = "failedLogins", nullable = false)
    private int failedLogins;

    @Column(name = "userLockTime")
    private String userLockTime;

    @Column(name = "lastLoginAddressLocation")
    private String lastLoginAddressLocation;

    @Column(name = "roles")
    @ElementCollection(targetClass=String.class, fetch = FetchType.EAGER)
    @JsonProperty("roles")
    @JsonDeserialize(using = CustomRolesDeserializer.class)
    private List<String> roles;

    @JsonIgnore
    @Transient
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getLastLoginAddressLocation() {
        return lastLoginAddressLocation;
    }

    public void setLastLoginAddressLocation(String lastLoginAddressLocation) {
        this.lastLoginAddressLocation = lastLoginAddressLocation;
    }

    public String getUserLockTime() {
        if (userLockTime == null ) {
            userLockTime = "00";
        }
        return userLockTime;
    }

    public void setUserLockTime(String userLockTime) {
        this.userLockTime = userLockTime;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled == true) {
            this.failedLogins = 0;
        }
        this.enabled = enabled;
    }

    public void disable() {
        this.enabled = false;
    }

    public void enable() {
        this.enabled = true;
        this.userLockTime = "00";
        this.failedLogins = 0;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public void addRoles(String[] rl) {
        List<String> roles = new ArrayList<>();
        for (String role : rl) {
            roles.add(role);
        }
        this.roles = roles;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public int getFailedLogins() {
        return failedLogins;
    }

    public void setFailedLogins(int failedLogins) {
        this.failedLogins = failedLogins;
    }

    public boolean isAdmin() {
        return roles.stream().anyMatch(role -> role.contentEquals("ROLE_ADMIN"));
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", nome='" + nome + '\'' +
                ", mail='" + mail + '\'' +
                ", firstLogin=" + firstLogin +
                ", lastLogin='" + lastLogin + '\'' +
                ", failedLogins=" + failedLogins +
                ", userLockTime='" + userLockTime + '\'' +
                ", lastLoginAddressLocation='" + lastLoginAddressLocation + '\'' +
                ", roles=" + roles +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}