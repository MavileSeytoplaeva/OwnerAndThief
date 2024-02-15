package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Thief extends Thread {
    ConcurrentLinkedQueue<Item> apartmentItems;
    final Apartment apartmentClass;
    private int stolenItemsWeight = 0;
    List<Item> stolenItems = new ArrayList<>();
    private Backpack backpack;
    final String THIEF_STEALS = "Вор %s крадёт вёщь весом %s и ценой %s \n";
    final String THIEF_LEAVES = "Вор ушёл";
    final String OWNER_ENTERS = "Вор пришёл";
    final String THIEF_BACKPACK_CAPACITY_NOT_ENOUGH = "Вместимость рюкзака вора %s. Этого недостаточно, поэтому он уходит пустой \n";
    final String APARTMENT_EMPTY = "Квартира пустая";

    public Thief(Apartment apartmentClass, int backpackCapacity) {
        this.apartmentClass = apartmentClass;
        this.apartmentItems = apartmentClass.apartmentItems;
        this.backpack = new Backpack(backpackCapacity);
    }

    public int getStolenItemsWeight() {
        return stolenItemsWeight;
    }

    public void setStolenItemsWeight(int stolenItemsWeight) {
        this.stolenItemsWeight = stolenItemsWeight;
    }

    private List<Item> findItems(ConcurrentLinkedQueue<Item> apartmentItems) {
        return apartmentItems.stream()
                .filter(item1 -> item1.getWeight() < backpack.getBackpackCapacity())
                .sorted(Comparator.comparing(Item::getWeight))
                .toList();
    }

    public synchronized void stealItems(ConcurrentLinkedQueue<Item> apartmentItems) {
        System.out.println(OWNER_ENTERS);
        List<Item> items = findItems(apartmentItems);
        if (apartmentItems.isEmpty()) {
            System.out.println(APARTMENT_EMPTY);
        } else if (!items.isEmpty()) {
            items.forEach(item -> {
                if (getStolenItemsWeight() + item.getWeight() <= backpack.getBackpackCapacity()) {
                    setStolenItemsWeight(getStolenItemsWeight() + item.getWeight());
                    stolenItems.add(item);
                    System.out.printf(THIEF_STEALS, getName(), item.getWeight(), item.getValue());
                    apartmentItems.remove(item);
                }
            });
        } else {
            System.out.printf(THIEF_BACKPACK_CAPACITY_NOT_ENOUGH, backpack.getBackpackCapacity());
        }
        System.out.println(THIEF_LEAVES);
    }

    @Override
    public void run() {
        apartmentClass.openDoor(Thread.currentThread());
        while (!apartmentClass.canComeIn && apartmentClass.counter != 0) {
            synchronized (apartmentClass) {
                try {
                    apartmentClass.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        synchronized (apartmentClass) {
            stealItems(apartmentItems);
            apartmentClass.closeDoor(Thread.currentThread());
        }
    }
}

