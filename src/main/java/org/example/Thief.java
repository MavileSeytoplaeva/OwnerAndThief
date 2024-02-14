package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Thief extends Thread {
    ConcurrentLinkedQueue<Item> apartment;
    private Backpack backpack;
    final String THIEF_STEALS = "Thief %s steals item with weight %s and value %s from the apartment \n";
    final String THIEF_LEAVES = "Thief leaves";
    final String THIEF_BACKPACK_CAPACITY_NOT_ENOUGH = "thief's backpack capacity is %s which is not enough so he leaves empty \n";
    ReentrantLock lock;
    Runnable task;

    public Thief(ConcurrentLinkedQueue<Item> apartment, int backpackCapacity, ReentrantLock lock, Runnable task) {
        super(task);
        this.task = task;
        this.lock = lock;
        this.apartment = apartment;
        this.backpack = new Backpack(backpackCapacity);
    }

    private Item findOptionalItem(ConcurrentLinkedQueue<Item> apartment) {
        return apartment.stream()
                .filter(item1 -> item1.getWeight() < backpack.getBackpackCapacity())
                .max(Comparator.comparing(Item::getValue))
                .orElse(null);
    }

    public void stealItems(ConcurrentLinkedQueue<Item> apartment) {
        Item item = findOptionalItem(apartment);
        if (item != null) {
            if (item.getWeight() < backpack.getBackpackCapacity()) {
                apartment.remove(item);
                System.out.printf(THIEF_STEALS, getName(), item.getWeight(), item.getValue());
                System.out.println(THIEF_LEAVES);
            }
        } else {
            System.out.printf(THIEF_BACKPACK_CAPACITY_NOT_ENOUGH, backpack.getBackpackCapacity());
        }
    }

    @Override
    public void run() {
            task.run();
    }
}


