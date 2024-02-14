package org.example;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Owner extends Thread {
    private Semaphore sem;
    final String OWNER_BRINGS_ITEM = "Owner %s adds item with weight %s and value %s to the apartment \n";
    final String OWNER_LEAVES = "Owner leaves";
    ConcurrentLinkedQueue<Item> apartment;
    Random random = new Random();
    ReentrantLock lock;
    Runnable task;
    public Owner(Semaphore sem, ConcurrentLinkedQueue<Item> apartment, ReentrantLock lock, Runnable task) {
        super(task);
        this.sem = sem;
        this.lock = lock;
        this.apartment = apartment;
        this.task = task;
    }

    private Item newItem() {
        return new Item(random.nextInt(20), random.nextInt(20));
    }

    public void addItem() {
        try {
            sem.acquire();
            Item item = newItem();
            apartment.add(item);
            System.out.printf(OWNER_BRINGS_ITEM, getName(), item.getWeight(), item.getValue());
            System.out.println(OWNER_LEAVES);
            sem.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        task.run();
    }

    @Override
    public String toString() {
        return "Owner{" +
                ", name =" + getName() +
                '}';
    }
}

