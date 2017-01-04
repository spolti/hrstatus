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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Entity
@Table(name = "SETUP")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Setup {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "mailJndi")
    private String mailJndi;

    @Column(name = "mailFrom")
    private String mailFrom;

    @Column(name = "welcomeMessage")
    private String welcomeMessage;

    @Column(name = "difference")
    private int difference;

    @Column(name = "sendNotification")
    private boolean sendNotification;

    @Column(name = "subject")
    private String subject;

    @Column(name = "destinatarios", unique = true)
    @ElementCollection(targetClass=String.class, fetch = FetchType.EAGER)
    @JsonProperty("destinatarios")
    private List<String> destinatarios;

    @Column(name = "ntpServer")
    private String ntpServer;

    @Column(name = "updateNtpIsActive")
    private boolean updateNtpIsActive;

    @JsonIgnore
    @Column(name = "installationDate")
    private LocalDateTime installationDate;

    public void setId(int id) {
        this.id = id;
    }

    public String getMailJndi() {
        return mailJndi;
    }

    public void setMailJndi(String mailJndi) {
        this.mailJndi = mailJndi;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }

    public boolean isSendNotification() {
        return sendNotification;
    }

    public void setSendNotification(boolean sendNotification) {
        this.sendNotification = sendNotification;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(List<String> destinatarios) {
        this.destinatarios = destinatarios;
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

    public LocalDateTime getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDateTime installationDate) {
        this.installationDate = installationDate;
    }

    @Override
    public String toString() {
        return "Setup{" +
                "id=" + id +
                ", mailJndi='" + mailJndi + '\'' +
                ", mailFrom='" + mailFrom + '\'' +
                ", welcomeMessage='" + welcomeMessage + '\'' +
                ", difference=" + difference +
                ", sendNotification=" + sendNotification +
                ", subject='" + subject + '\'' +
                ", destinatarios=" + destinatarios +
                ", ntpServer='" + ntpServer + '\'' +
                ", updateNtpIsActive=" + updateNtpIsActive +
                ", installationDate=" + installationDate +
                '}';
    }
}