package org.example;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;


public class ThreadPool implements Executor {
    Set<Thread> threads = new HashSet<>();

    public ThreadPool(int owners, int thieves, Apartment apartment) {
        for (int i = 0; i < thieves; i++) {
            threads.add(new Thief(apartment, new Backpack()));
        }
        for (int i = 0; i < owners; i++) {
            threads.add(new Owner(apartment, new Backpack()));
        }
    }

    @Override
    public void execute(Runnable command) {
        threads.parallelStream()
                .forEach(Thread::start);
    }

    public void joinAllThreads() {
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Set<Thread> getThreads() {
        return threads;
    }
}
