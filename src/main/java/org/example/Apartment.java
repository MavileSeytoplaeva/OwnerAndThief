package org.example;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Apartment {
    private ConcurrentLinkedQueue<Item> apartmentItems = new ConcurrentLinkedQueue<>();
    private int ownerCount = 0;
    private int thiefCount = 0;

    public ConcurrentLinkedQueue<Item> getApartmentItems() {
        return apartmentItems;
    }

    public void addApartmentItem(Item item) {
        apartmentItems.add(item);
    }

    public void deleteApartmentItem(Item item) {
        apartmentItems.remove(item);
    }

    public void lockApartment(Thread thread) {
        if (thread instanceof Owner) {
            ownerCount++;
        } else {
            thiefCount++;
        }
    }

    public void unlockApartment(Thread thread) {
        synchronized (this) {
            if (thread instanceof Owner) {
                ownerCount--;
            } else {
                thiefCount--;
            }
            this.notify();
        }
    }

    public int getOwnerCount() {
        return ownerCount;
    }

    public int getThiefCount() {
        return thiefCount;
    }

}