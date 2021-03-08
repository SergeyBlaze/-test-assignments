package com.kozlov.localthread;

import com.kozlov.localthread.test.TestHelper;
import com.kozlov.localthread.test.TestRunnable;

public class ThreadLocalRun {

    public static void main(String[] args) {
        Thread thread = new Thread(new TestRunnable("_nvl", 1000L));

        thread.start();

        System.out.println("Thread " + Thread.currentThread() + "     value = " + TestHelper.get());
        sleep(300L);
        System.out.println("Thread " + Thread.currentThread() + "     value = " + TestHelper.get());
        TestHelper.set("test1");
        System.out.println("Thread " + Thread.currentThread() + "     value = " + TestHelper.get());
        sleep(1000);
        TestHelper.set(null);
        System.out.println("Thread " + Thread.currentThread() + "     value = " + TestHelper.get());
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignore) {}
    }

}
