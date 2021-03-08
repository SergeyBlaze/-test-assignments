package com.kozlov.localthread.test;

import com.kozlov.localthread.ThreadLocal;

public class TestHelper {

    public static final ThreadLocal<String> custom = new ThreadLocal<>();

    private TestHelper() { }

    public static String get() {
        return custom.get();
    }

    public static void set(String value) {
        custom.set(value);
    }

}
