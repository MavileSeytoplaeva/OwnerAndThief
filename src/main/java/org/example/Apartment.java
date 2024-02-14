package org.example;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Apartment implements Runnable{
    ConcurrentLinkedQueue<Item> apartmentItems = new ConcurrentLinkedQueue<>();
    ReentrantLock lock;

    public Apartment(ReentrantLock lock) {
        this.lock = lock;
    }

    public void addItems(Thread owner) {
        if (owner instanceof Owner) {
            ((Owner) owner).addItem();
        }
    }

    public void stealItems(Thread thief) {
        if (thief instanceof Thief) {
            ((Thief) thief).stealItems(apartmentItems);
        }
    }

    @Override
    public void run() {
        if (Thread.currentThread() instanceof Owner){
            addItems(Thread.currentThread());
        } else if (Thread.currentThread() instanceof Thief) {
                lock.lock();
                try {
                    stealItems(Thread.currentThread());
                } finally {
                    lock.unlock();
                }
            }


        }
    }
