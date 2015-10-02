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
import br.com.hrstatus.verification.scheduler.DbFullCheckScheduler;
import br.com.hrstatus.verification.scheduler.ServerFullCheckScheduler;

import com.jcraft.jsch.JSchException;


@Service
public class EJBVerificationScheduler {

	static Logger log =  Logger.getLogger(EJBVerificationScheduler.class.getCanonicalName());

	@Autowired
	private SchedulerInterface schedulerDAO;
	
	private VerificationScheduler scheduler;
	
	@Autowired	
	private DbFullCheckScheduler dbfull;
	@Autowired
	private ServerFullCheckScheduler serverfull;
	
	public EJBVerificationScheduler() {}
	
	@Scheduled(cron = "${br.com.hrstatus.scheduler.NtpScheduler.cron}")
	public void defaultScheduler () throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InterruptedException, JSchException {
	
		log.fine("The default scheduler will start a full server and Db verification everyday at 00:00 if enabled");
		
		scheduler = this.schedulerDAO.getSchedulerDefault("defaultScheduler");
		
		if (!scheduler.isEnabled() && !scheduler.isEveryday() && !scheduler.isDefaultScheduler()){
			log.fine("Default Schduler is disabled, skipping.");
			
		} else {
			log.fine("Default Schduler Enabled.");
			log.fine("Starting full Db verification");
			
			dbfull.startFullDataBaseVerification("defaultScheduler");
			serverfull.startFullVerification("defaultScheduler");
			
		}
		
		
	}

}