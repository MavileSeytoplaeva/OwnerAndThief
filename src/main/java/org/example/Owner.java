package org.example;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Owner extends Thread {
    private Semaphore sem;
    final String OWNER_BRINGS_ITEM = "Owner %s adds item with weight %s and value %s to the apartment \n";
    final String OWNER_LEAVES = "Owner leaves";
    ConcurrentLinkedQueue<Item> apartment;
    Random random = new Random();
    ReentrantLock lock;
    Condition condition;

    public Owner(Semaphore sem, ConcurrentLinkedQueue<Item> apartment, ReentrantLock lock, Condition condition) {
        this.sem = sem;
        this.lock = lock;
        this.condition = condition;
        this.apartment = apartment;
    }

    private Item newItem() {
        return new Item(random.nextInt(20), random.nextInt(20));
    }

    public void addItem() {
//        lock.lock();
        try {
            sem.acquire();
            Item item = newItem();
            apartment.add(item);
            System.out.printf(OWNER_BRINGS_ITEM, getName(), item.getWeight(), item.getValue());
            System.out.println(OWNER_LEAVES);
            sem.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
//            lock.unlock();
        }
    }

    @Override
    public void run() {
        addItem();
    }

    @Override
    public String toString() {
        return "Owner{" +
                ", name =" + getName() +
                '}';
    }
}

