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

package br.com.hrstatus.tests.executor;

import br.com.hrstatus.verification.executor.VerificationExecutor;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class ExecutorThreadPoolTest {

    private VerificationExecutor verificationExecutor = new VerificationExecutor();

    @Test
    public void testExecutorLifecycle() {
        verificationExecutor.startExecutorService();
        Assert.assertTrue(verificationExecutor.getExecutor() instanceof ExecutorService);
        verificationExecutor.shutdownExecutorService();
        Assert.assertTrue(verificationExecutor.getExecutor().isTerminated());
    }

    @Test
    public void testWorkerNamePatter() throws ExecutionException, InterruptedException {
        verificationExecutor.startExecutorService();
        Callable<Object> task = () -> {
            Assert.assertTrue(Thread.currentThread().getName().contains("HrStatusVerification-worker"));
            return "task executada";
        };
        Future<Object> future = verificationExecutor.getExecutor().submit(task);
        Assert.assertEquals("task executada", future.get());
        Assert.assertTrue(future.isDone());
        verificationExecutor.shutdownExecutorService();
        Assert.assertTrue(verificationExecutor.getExecutor().awaitTermination(5, TimeUnit.SECONDS));
    }

    @Test
    public void testThreadDeamon() throws ExecutionException, InterruptedException {
        verificationExecutor.startExecutorService();
        Callable<Object> task = () -> {
            Assert.assertTrue(Thread.currentThread().isDaemon());
            return "task executada";
        };
        Future<Object> future = verificationExecutor.getExecutor().submit(task);
        Assert.assertEquals("task executada", future.get());
        Assert.assertTrue(future.isDone());
        verificationExecutor.shutdownExecutorService();
        Assert.assertTrue(verificationExecutor.getExecutor().awaitTermination(5, TimeUnit.SECONDS));
    }

    @Test(expected = RejectedExecutionException.class)
    public void testMaxThreadPool() throws InterruptedException {
        verificationExecutor.startExecutorService();
        List<Callable<String>> callables = Arrays.asList(
                () -> {
                    Thread.sleep(1000);
                    return "task1";
                },
                () -> {
                    Thread.sleep(1000);
                    return "task2";
                },
                () -> {
                    Thread.sleep(1000);
                    return "task3";
                },
                () -> {
                    Thread.sleep(1000);
                    return "task4";
                },
                () -> {
                    Thread.sleep(1000);
                    return "task5";
                },
                () -> {
                    Thread.sleep(1000);
                    return "task6";
                },
                () -> {
                    Thread.sleep(1000);
                    return "task7";
                },
                () -> {
                    Thread.sleep(1000);
                    return "task8";
                });

        verificationExecutor.getExecutor().invokeAll(callables)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    }
                    catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(System.out::println);
    }
}