package br.com.hrstatus.dao;

import java.util.List;

import br.com.hrstatus.model.VerificationScheduler;

/*
 * @author spolti
 */

public interface SchedulerInterface {

	public String saveScheduler (VerificationScheduler scheduler);
	
	public String saveSchedulerNotLogged (VerificationScheduler scheduler);
	
	public List<VerificationScheduler> schedulers();
	
	public VerificationScheduler getSchedulerDefault(String schedulerName);
}
