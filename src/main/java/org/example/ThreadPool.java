package org.example;

import java.time.chrono.ThaiBuddhistEra;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ThreadPool implements Executor {
    Set<Thread> threads = new HashSet<>();

    public ThreadPool(int owners, int thieves, Semaphore sem, Apartment apartment, AtomicInteger totalItems) throws InterruptedException {
        for (int i = 0; i < thieves; i++) {
            threads.add(new Thief(apartment, (int) (Math.random() * 10 + 5)));
        }
        for (int i = 0; i < owners; i++) {
            threads.add(new Owner(sem, apartment, totalItems));
        }
    }

    @Override
    public void execute(Runnable command) {
        threads.parallelStream()
                .forEach(Thread::start);
    }
}
