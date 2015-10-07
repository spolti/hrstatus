package br.com.hrstatus.scheduler;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.hrstatus.dao.SchedulerInterface;
import br.com.hrstatus.model.VerificationScheduler;
import br.com.hrstatus.model.VerificationSchedulerHistory;
import br.com.hrstatus.utils.date.DateParser;
import br.com.hrstatus.utils.date.DateUtils;
import br.com.hrstatus.verification.scheduler.DbFullCheckScheduler;
import br.com.hrstatus.verification.scheduler.ServerFullCheckScheduler;

import com.jcraft.jsch.JSchException;


@Service
public class SchedulerVerification {

	static Logger log =  Logger.getLogger(VerificationScheduler.class.getCanonicalName());

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
	
	public SchedulerVerification() {}
	
	@Scheduled(cron = "${br.com.hrstatus.scheduler.NtpScheduler.cron}")
	public void defaultScheduler () throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InterruptedException, JSchException {
	
		log.fine("The default scheduler will start a full server and Db verification everyday at 00:00 if enabled");
		
		scheduler = this.schedulerDAO.getSchedulerDefault("defaultScheduler");
		
		String schedulerName = scheduler.getSchedulerName();
		
		if (!scheduler.isEnabled() && !scheduler.isEveryday() && !scheduler.isDefaultScheduler()){
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