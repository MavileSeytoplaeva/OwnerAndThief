


package org.example;

import java.util.*;
import java.util.concurrent.BlockingQueue;


public class Thief extends Thread {
    private BlockingQueue<Item> apartment;
    private Backpack backpack;
    private List<Item> stolenItems = new ArrayList<>();

    private Object lock;
    private int totalWeight;



    public Thief(BlockingQueue<Item> apartment, int backpackCapacity, Object lock) {
        this.lock = lock;
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
        Item item = apartment.stream()
                .filter(item1 -> item1.getWeight() < backpack.getBackpackCapacity())
                .max(Comparator.comparing(Item::getValue))
                .orElse(null);
        sleep(1000);
        if (item != null) {
            addItemsToBackpack(item);
            apartment.remove(item);
//                    } else {
//                        System.out.println("Thief's backpack is full, he leaves");
////                        break;
//                    }
        }
    }


    @Override
    public void run() {
        synchronized (lock) {
            while (true) {
                try {
                    totalWeight = 0;
                    System.out.println("Thief enters the apartment");
                    addItems(apartment);
                    System.out.println("Thief leaves the apartment");
                    sleep(1000);
                    lock.notify();
                    lock.wait();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}


