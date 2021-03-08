package com.kozlov.localthread;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
* Можно было бы использовать WeakHashMap но он в своей реализации может использовать LocalThread
* По условию ThreadLocal стандартного не существует.
* */
public class ThreadLocal<T> {

    private final Map<Thread, T> threadsValue = new IdentityHashMap<>();

    public T get() {
        removeTerminated();
        Thread thread = Thread.currentThread();
        T value;
        synchronized (threadsValue) {
            value = threadsValue.get(thread);
        }
        return value;
    }

    public void set(T value) {
        removeTerminated();
        Thread thread = Thread.currentThread();
        synchronized (threadsValue) {
            threadsValue.put(thread, value);
        }
    }

    public void remove() {
        removeTerminated();
        Thread thread = Thread.currentThread();
        synchronized (threadsValue) {
            threadsValue.remove(thread);
        }
    }

    private void removeTerminated() {
        synchronized (threadsValue) {
            Iterator<Thread> iterator = threadsValue.keySet().iterator();
            while (iterator.hasNext()) {
                Thread next = iterator.next();
                if (next.getState() == Thread.State.TERMINATED) {
                    iterator.remove();
                }
            }
        }
    }

}
