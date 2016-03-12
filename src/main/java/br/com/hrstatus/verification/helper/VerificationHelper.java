/*
Copyright (C) 2012 Filippe Costa Spolti

This file is part of Hrstatus.

Hrstatus is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.hrstatus.verification.helper;

import br.com.hrstatus.action.databases.SQLStatementExecute;
import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.dao.Configuration;
import br.com.hrstatus.dao.LockIntrface;
import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.model.Lock;
import br.com.hrstatus.resrources.ResourcesManagement;
import br.com.hrstatus.security.Crypto;
import br.com.hrstatus.utils.UserInfo;
import br.com.hrstatus.utils.date.DateParser;
import br.com.hrstatus.utils.date.DateUtils;
import br.com.hrstatus.verification.impl.VerificationImpl;
import com.jcraft.jsch.JSchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * @author spolti
 */

public abstract class VerificationHelper {

    @Autowired
    public BancoDadosInterface dbDAO;
    @Autowired
    public Configuration configurationDAO;
    @Autowired
    public LockIntrface lockDAO;
    @Autowired
    public ResourcesManagement resource;
    public UserInfo userInfo = new UserInfo();
    public  Crypto encodePass = new Crypto();
    public SQLStatementExecute execQueryDate = new SQLStatementExecute();
    public Lock lockedResource = new Lock();

    @Autowired
    public ServersInterface serversDAO;
    DateUtils getTime = new DateUtils();

    private DateUtils dt = new DateUtils();
    private DateParser parse = new DateParser();


    /*
     * Returns the date difference in seconds between the client and server
     */
    public long differenceTime(String serverTime, String clientTime) throws JSchException, IOException {

        long diff = 0;

        // Converting String dates to java.util.Date
        Date stime = parse.parser(serverTime);
        Date ctime = parse.parser(clientTime);

        diff = stime.getTime() - ctime.getTime();

        DecimalFormat df = new DecimalFormat();
        df.applyPattern("00.00;(00.00)");
        long result = diff / (1000);

        return result;
    }

    /*
     * Returns the the serverTime
     */
    public String getTime() {
        Calendar cal = Calendar.getInstance();
        String serverTime = cal.getTime().toString();
        serverTime = parse.parser(serverTime).toString();
        return serverTime;
    }
}