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

package br.com.hrstatus.verification.executor;

import br.com.hrstatus.rest.Verification;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class VerificationExecutor {

    private Logger log = Logger.getLogger(Verification.class.getName());

    //threads that will be always alive until the Executor is running
    private int corePoolSize = 2;
    // if the 2 first threads are being used, create new one, max 5.
    private int maxPoolSize = 5;
    //wait 5 seconds after the extra threads are in idle state before close it.
    private long keepAliveTime = 5000;
    // Make the thread run as deamon and set the Name format just for easy identification
    private ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder()
            .setDaemon(true)
            .setNameFormat("HrStatusVerification-worker-%d");

    private ExecutorService executor;

    /**
     * Starts the Executor service with a pre defined thread pool.
     */
    public void startExecutorService() {
        executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(2),
                threadFactoryBuilder.build()
        );
        log.info("Executor service iniciado");
    }

    /**
     * @return {@link ExecutorService}
     */
    synchronized public ExecutorService getExecutor() {
        log.fine("VerificationExecutor service já está iniciado, retornando.");
        return executor;
    }

    /**
     * Shutdown the Executor Service
     */
    public void shutdownExecutorService() {
        log.info("Parando Executor Service");
        executor.shutdown();
    }
}