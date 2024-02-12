
package org.example;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
public class Owner extends Thread {
    private Object lock;
    private Semaphore sem;
    private BlockingQueue<Item> apartment;
    private List<Item> items;
    private boolean leftTheItem = false;
    Random random = new Random();

    public Owner(Semaphore sem, BlockingQueue<Item> apartment, Object lock) {
        this.sem = sem;
        this.lock = lock;
        this.apartment = apartment;
    }

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
        synchronized (lock) {
            try {
                if (!leftTheItem) {
                    sem.acquire();
                    System.out.println("Owner enters the apartment");
                    addItem();
                    addItem();
                    leftTheItem = true;
                    sleep(1000);
                    System.out.println("Owner leaves the apartment");
                    sem.release();

                    lock.notify();
                    lock.wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
