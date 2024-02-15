package org.example;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Apartment {
    ConcurrentLinkedQueue<Item> apartmentItems = new ConcurrentLinkedQueue<>();
    boolean canComeIn = true;
    int counter = 0;

    public synchronized void openDoor(Thread thread) {
        if (thread instanceof Owner) {
            canComeIn = true;
            counter++;
        } else if (thread instanceof Thief) {
            canComeIn = false;
        }
    }

    public void closeDoor(Thread thread) {
        synchronized (this) {
            if (thread instanceof Owner) {
                counter--;
                if (counter == 0) {
                    canComeIn = true;
                    this.notify();
                }
                System.out.println(counter);
            } else if (thread instanceof Thief) {
                canComeIn = true;
                this.notify();
            }
        }
    }
}