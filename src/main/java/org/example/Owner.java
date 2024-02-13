
package org.example;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class Owner implements Runnable {
    private Semaphore sem;
    private BlockingQueue<Item> apartment;
    private boolean leftTheItem = false;
    Random random = new Random();
    ReentrantLock lock;
    Condition condition;

    public Owner(Semaphore sem, BlockingQueue<Item> apartment, ReentrantLock lock, Condition condition) {
        this.sem = sem;
        this.lock = lock;
        this.condition = condition;
        this.apartment = apartment;
    }
//    public Owner(Semaphore sem, BlockingQueue<Item> apartment) {
//        this.sem = sem;
//        this.apartment = apartment;
//    }


    public void addItemToApartment(Item item) throws InterruptedException {
        apartment.add(item);
        System.out.printf("Owner %s adds item with weight %s and value %s to the apartment \n", currentThread().getName(), item.getWeight(), item.getValue());
    }

    public void addItem() throws InterruptedException {
        addItemToApartment(new Item(random.nextInt(20), random.nextInt(20)));
        sleep(1000);
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!leftTheItem) {
                    sem.acquire();
                    System.out.println("Owner enters the apartment");
                    addItem();
                    leftTheItem = true;
                    System.out.println("Owner leaves the apartment");
                    lock.lock();
                    sem.release();
                    System.out.printf("await and signal %s \n", currentThread().getName());
                    condition.signal();
                    condition.await();

                    lock.unlock();
                    leftTheItem = false;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

