package br.com.hrstatus.resrources.impl;

import br.com.hrstatus.dao.LockInterface;
import br.com.hrstatus.model.Lock;
import br.com.hrstatus.resrources.ResourcesManagement;
import br.com.hrstatus.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ResourceManagementImpl implements ResourcesManagement {

    Logger log = Logger.getLogger(ResourceManagementImpl.class.getCanonicalName());

    @Autowired
    private LockInterface lockDAO;
    private Lock lockedResource = new Lock();
    private UserInfo userInfo = new UserInfo();

    public boolean lockRecurso(String resourceName) {

        log.info(" [ " + userInfo.getLoggedUsername() + "] Locking resource " + resourceName);
        lockedResource.setRecurso(resourceName);
        lockedResource.setUsername(userInfo.getLoggedUsername());
        try {
            this.lockDAO.insertLock(lockedResource);
            return true;
        } catch (Exception e) {
            log.severe(" [ " + userInfo.getLoggedUsername() + "] Falied to lock resource: " + e.getCause());
            return false;
        }
    }

    public boolean releaseLock(String resourceName) {

        log.info(" [ " + userInfo.getLoggedUsername() + "] Releasing resource " + resourceName);
        lockedResource.setRecurso(resourceName);
        lockedResource.setUsername(userInfo.getLoggedUsername());
        try {
            this.lockDAO.removeLock(lockedResource);
            return true;
        } catch (Exception e) {
            log.severe(" [ " + userInfo.getLoggedUsername() + "] Falied to release resource: " + e.getCause());
            return false;
        }
    }

    public boolean islocked(String resourceName) {

        log.info(" [ " + userInfo.getLoggedUsername() + "] Verifying if " + resourceName + " is locked.");

        final List<Lock> lockList = this.lockDAO.listLockedServices(resourceName);
        if (lockList.size() != 0) {
            for (Lock lock : lockList) {
                log.info("[ " + userInfo.getLoggedUsername() + " ] The resource " + resourceName + " is locked by the user " + lock.getUsername());
            }
            return true;
        } else {
            return false;
        }
    }
}