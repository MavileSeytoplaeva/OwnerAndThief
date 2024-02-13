


package org.example;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;


public class Thief implements Runnable {
    private BlockingQueue<Item> apartment;
    private Backpack backpack;
    private List<Item> stolenItems = new ArrayList<>();

    private int totalWeight;

    ReentrantLock lock;
    Condition condition;

    public Thief(BlockingQueue<Item> apartment, int backpackCapacity, ReentrantLock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
        this.apartment = apartment;
        this.backpack = new Backpack(backpackCapacity);
    }


    public Item addItemsToBackpack(Item item) {

        if (totalWeight + item.getWeight() < backpack.getBackpackCapacity()) {
            totalWeight += item.getWeight();
            stolenItems.add(item);
            System.out.printf("Thief %s stoles item with weight %s and value %s from the apartment \n", currentThread().getName(), item.getWeight(), item.getValue());
            return item;
        }
        return null;
    }

    public void addItems(BlockingQueue<Item> apartment) throws InterruptedException {
        if (apartment.isEmpty()) {
            condition.await();
        }
        Item item = apartment.stream()
                .filter(item1 -> item1.getWeight() < backpack.getBackpackCapacity())
                .max(Comparator.comparing(Item::getValue))
                .orElse(null);
        sleep(1000);
        if (item != null) {
            addItemsToBackpack(item);
            apartment.remove(item);
            condition.signal();
        }
    }


    @Override
    public void run() {
        lock.lock();
        try {
            while (true) {
                System.out.println("thief is await");
                condition.await();
                totalWeight = 0;
                System.out.println("Thief got the signal and enters the apartment");
                addItems(apartment);
                if (totalWeight > backpack.getBackpackCapacity()){
                    System.out.println("totalWeight > backpack.getBackpackCapacity()");
                    System.out.println("backPack is full");
                    condition.signal();
                    break;
                }
                System.out.println("Thief leaves the apartment");
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}


