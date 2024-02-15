package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Owner extends Thread {
    private Semaphore sem;
    AtomicInteger totalItems;

    final String OWNER_BRINGS_ITEM = "Хозяин %s кладёт вещь весом %s и ценностью %s \n";
    final String OWNER_ENTERS = "Хозяин пришёл";
    final String OWNER_LEAVES = "Хозяин ушёл";

    ConcurrentLinkedQueue<Item> apartment;
    final Apartment apartmentCl;
    Random random = new Random();
    private List<Item> backpackWithItems = new ArrayList<>();

    public Owner(Semaphore sem, Apartment apartment, AtomicInteger totalItems) {
        this.sem = sem;
        this.totalItems = totalItems;
        this.apartmentCl = apartment;
        this.apartment = apartment.apartmentItems;
    }

    public void itemsInBackpack() {
        int backpackCapacity = random.nextInt(1, 4);
        for (int i = 0; i < backpackCapacity; i++) {
            backpackWithItems.add(new Item(random.nextInt(20), random.nextInt(20)));
        }
        totalItems.addAndGet(backpackWithItems.size());
    }

    public void addItem() {
        try {
            sem.acquire();
            itemsInBackpack();
            System.out.println(OWNER_ENTERS);
            backpackWithItems.forEach(item -> {
                apartment.add(item);
                System.out.printf(OWNER_BRINGS_ITEM, getName(), item.getWeight(), item.getValue());
            });
            sem.release();
            System.out.println(OWNER_LEAVES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        apartmentCl.openDoor(Thread.currentThread());
        if (apartmentCl.canComeIn || apartmentCl.counter >= 1) {
            addItem();
            System.out.println(apartmentCl.counter);
        }
        apartmentCl.closeDoor(Thread.currentThread());
    }

    @Override
    public String toString() {
        return "Owner{" +
                ", name =" + getName() +
                '}';
    }
}



