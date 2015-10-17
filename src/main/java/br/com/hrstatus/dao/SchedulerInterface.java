package br.com.hrstatus.dao;

import java.util.List;

import br.com.hrstatus.model.VerificationScheduler;
import br.com.hrstatus.model.VerificationSchedulerHistory;

/*
 * @author spolti
 */

public interface SchedulerInterface {

	public String saveScheduler (VerificationScheduler scheduler);
	
	public void updateScheduler (VerificationScheduler scheduler);
	
	public String saveSchedulerNotLogged (VerificationScheduler scheduler);
	
	public List<VerificationScheduler> schedulers();
	
	public VerificationScheduler getSchedulerDefault(String schedulerName);
	
	public void saveHistory(VerificationSchedulerHistory schedulerHistory, String schedulerName);
	
	public List<VerificationSchedulerHistory> getSchedulerHistory (String schedulerName);
	
	public VerificationScheduler getSchedulerByID(int id);
}