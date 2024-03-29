package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Thief extends Thread {
    private final ThiefPrinter thiefPrinter = new ThiefPrinter();
    private final Apartment apartment;
    private final Backpack backpackWithItems;

    public Thief(Apartment apartment, Backpack backpack) {
        this.apartment = apartment;
        this.backpackWithItems = backpack;
    }
    private List<Item> findItems(ConcurrentLinkedQueue<Item> apartmentItems) {
        return apartmentItems.stream()
                .filter(item1 -> item1.getWeight() < backpackWithItems.getBackpackCapacity())
                .sorted(Comparator.comparing(Item::getWeight))
                .toList();
    }

    public void addItemsToBackpack(List<Item> items) {
        try {
        items.forEach(item -> {
                if (backpackWithItems.getBackpackCapacity() - item.getWeight() >= 0) {
                    backpackWithItems.addItemsToBackPack(item);
                    thiefPrinter.printThreadDoesAction(currentThread(), item);
                    apartment.deleteApartmentItem(item);
                } else {
                    throw new NoStorageLeftException("В рюкзаке нет места на ещё один предмет");
                }
        });
        } catch (NoStorageLeftException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println(e.getMessage());
        }
    }

    public synchronized void stealItemsFromApartment(ConcurrentLinkedQueue<Item> apartmentItems) {
        List<Item> items = findItems(apartmentItems);
        if (apartmentItems.isEmpty()) {
            thiefPrinter.printApartmentIsEmpty();
        } else if (!items.isEmpty()) {
            addItemsToBackpack(items);
        } else {
            thiefPrinter.printThiefBackpackCapacityIsNotEnough(currentThread());
        }
    }

    public Backpack getBackpackWithItems() {
        return backpackWithItems;
    }

    @Override
    public void run() {
        synchronized (apartment) {
            while (apartment.getOwnerCount() >= 1 || apartment.getThiefCount() == 1)
                try {
                    apartment.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            apartment.lockApartment(currentThread());
            thiefPrinter.printThreadEnters(currentThread());
            stealItemsFromApartment(apartment.getApartmentItems());
            apartment.unlockApartment(currentThread());
            thiefPrinter.printThreadLeaves(currentThread());
        }
    }
}



