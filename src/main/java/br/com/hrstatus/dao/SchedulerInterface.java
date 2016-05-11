package br.com.hrstatus.dao;

import br.com.hrstatus.model.VerificationScheduler;
import br.com.hrstatus.model.VerificationSchedulerHistory;

import java.util.List;

/*
 * @author spolti
 */

public interface SchedulerInterface {

    String saveScheduler(VerificationScheduler scheduler);

    void updateScheduler(VerificationScheduler scheduler);

    String saveSchedulerNotLogged(VerificationScheduler scheduler);

    List<VerificationScheduler> schedulers();

    VerificationScheduler getSchedulerDefault(String schedulerName);

    void saveHistory(VerificationSchedulerHistory schedulerHistory, String schedulerName);

    List<VerificationSchedulerHistory> getSchedulerHistory(String schedulerName);

    VerificationScheduler getSchedulerByID(int id);
}