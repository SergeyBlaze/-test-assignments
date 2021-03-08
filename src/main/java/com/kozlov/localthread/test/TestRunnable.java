package com.kozlov.localthread.test;

import static com.kozlov.localthread.ThreadLocalRun.sleep;
import static com.kozlov.localthread.test.TestHelper.*;

public class TestRunnable implements Runnable {

    private String value;
    private Long sleep;

    public TestRunnable(String value, Long sleep) {
        this.value = value;
        this.sleep = sleep;
    }

    @Override
    public void run() {
        Thread thread = Thread.currentThread();

        System.out.println("Thread " + thread + " value = " + get() + " on start");
        set(value);
        sleep(sleep);
        System.out.println("Thread " + thread + " value = " + get() + " on complete");

        assert value == get();
    }

}
