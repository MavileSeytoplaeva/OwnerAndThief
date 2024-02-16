package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Backpack {
    private int backpackCapacity;
//    private int leftBackpackStorage;
    private List<Item> itemsInBackpack = new ArrayList<>();
    private final Random random = new Random();

    public Backpack() {
        this.backpackCapacity = getRandom() + 5;
//        this.leftBackpackStorage = getBackpackCapacity();
    }

    public void addItemsToBackPack(Item item) {
        itemsInBackpack.add(item);
        backpackCapacity -= item.getWeight();
    }

    public List<Item> getItemsInBackpack() {
        return itemsInBackpack;
    }

    public int getRandom() {
        return random.nextInt(1, 11);
    }

    public void addItemInBackpack(Item item) {
        this.itemsInBackpack.add(item);
        backpackCapacity -= item.getWeight();

    }

    public void fillOwnersBackpackWithItems() {
        while (true) {
            Item item = new Item(getRandom(), getRandom());
            if (backpackCapacity - item.getWeight() > 0) {
                itemsInBackpack.add(item);
                backpackCapacity -= item.getWeight();
//                backpackCapacity -= item.getWeight();
//                addItemInBackpack(item);
            } else {
                break;
            }
        }
    }

    public int getBackpackCapacity() {
        return backpackCapacity;
    }
}