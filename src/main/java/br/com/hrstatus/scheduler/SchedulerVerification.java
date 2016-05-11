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

package br.com.hrstatus.scheduler;

import br.com.hrstatus.action.databases.helper.IllegalVendorException;
import br.com.hrstatus.dao.SchedulerInterface;
import br.com.hrstatus.model.VerificationScheduler;
import br.com.hrstatus.model.VerificationSchedulerHistory;
import br.com.hrstatus.utils.date.DateParser;
import br.com.hrstatus.utils.date.DateUtils;
import br.com.hrstatus.verification.scheduler.DbFullCheckScheduler;
import br.com.hrstatus.verification.scheduler.ServerFullCheckScheduler;
import com.jcraft.jsch.JSchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/*
 * @author spolti
 */

@Service
public class SchedulerVerification {

    static Logger log = Logger.getLogger(VerificationScheduler.class.getCanonicalName());

    @Autowired
    private SchedulerInterface schedulerDAO;

    private VerificationScheduler scheduler;
    private VerificationSchedulerHistory schedulerHistory = new VerificationSchedulerHistory();

    @Autowired
    private DbFullCheckScheduler dbfull;
    @Autowired
    private ServerFullCheckScheduler serverfull;

    private DateUtils dt = new DateUtils();
    private DateParser dp = new DateParser();

    public SchedulerVerification() {
    }

    @Scheduled(cron = "${br.com.hrstatus.scheduler.DefaultScheduler.cron}")
    public void defaultScheduler() throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InterruptedException, JSchException, IllegalVendorException {

        log.fine("The default scheduler will start a full server and Db verification everyday at 00:00 if enabled");

        scheduler = this.schedulerDAO.getSchedulerDefault("defaultScheduler");

        final String schedulerName = scheduler.getSchedulerName();

        if (!scheduler.isEnabled() && !scheduler.isEveryday() && !scheduler.isDefaultScheduler()) {
            log.fine("Default Schduler is disabled, skipping.");

        } else {

            schedulerHistory.setSchedulerName(schedulerName);
            schedulerHistory.setEnabled(scheduler.isEnabled());
            schedulerHistory.setEveryday(scheduler.isEveryday());
            schedulerHistory.setDefaultScheduler(scheduler.isDefaultScheduler());
            schedulerHistory.setStartedAt(dp.parser(dt.getTime()));

            log.fine("Default Schduler Enabled.");
            log.fine("Starting full verification");

            dbfull.startFullDataBaseVerification("defaultScheduler");
            serverfull.startFullVerification("defaultScheduler");

            schedulerHistory.setFinished(true);
            schedulerHistory.setFinishedAt(dp.parser(dt.getTime()));

        }
        //populating the scheduler history table with this current execution.
        this.schedulerDAO.saveHistory(schedulerHistory, schedulerName);
    }
}