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

import br.com.hrstatus.model.support.SupportedDatabase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashMap;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Entity
@Table(name = "OS")
public class OperatingSystem {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private int id;

    @Column(name = "hostname", nullable = false)
    private String hostname;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "port")
    private int port;

    // user-pass
    @Column(name = "credentials", nullable = true)
    private HashMap<String, String> credentials;

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

}