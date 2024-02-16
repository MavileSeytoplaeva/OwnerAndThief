package org.example;

import java.util.List;

public class Owner extends Thread {
    private final Apartment apartment;
    private final OwnerPrinter ownerPrinter = new OwnerPrinter();
    private final Backpack backpackWithItems;

    public Owner(Apartment apartment, Backpack backpack) {
        this.apartment = apartment;
        this.backpackWithItems = backpack;
        backpackWithItems.fillOwnersBackpackWithItems();
    }

    public List<Item> getBackpackWithItems() {
        return backpackWithItems.getItemsInBackpack();
    }

    public void addItemsToApartment() {
        getBackpackWithItems()
                .forEach(item -> {
                    apartment.addApartmentItem(item);
                    ownerPrinter.printThreadDoesAction(currentThread(), item);
                });
    }

    @Override
    public void run() {
        synchronized (apartment) {
            while (apartment.getThiefCount() == 1) {
                try {
                    apartment.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        apartment.lockApartment(currentThread());
        ownerPrinter.printThreadEnters(currentThread());
        addItemsToApartment();
        ownerPrinter.printThreadLeaves(currentThread());
        apartment.unlockApartment(currentThread());
    }

    @Override
    public String toString() {
        return "Owner{" +
                ", name =" + getName() +
                '}';
    }
}



