package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Backpack {
    private final int backpackCapacity;
    private int leftBackpackStorage;
    private List<Item> itemsInBackpack = new ArrayList<>();
    private final Random random = new Random();

    public Backpack() {
        this.backpackCapacity = getRandom() + 10;
        this.leftBackpackStorage = getBackpackCapacity();
    }

    public void addItemsToBackPack(Item item) {
        itemsInBackpack.add(item);
    }

    public int getLeftBackpackStorage() {
        return leftBackpackStorage;
    }

    public void setLeftBackpackStorage(int leftBackpackStorage) {
        this.leftBackpackStorage = leftBackpackStorage;
    }

    public List<Item> getItemsInBackpack() {
        return itemsInBackpack;
    }

    public int getRandom() {
        return random.nextInt(1, 11);
    }

    public void addItemInBackpack(Item item) {
        this.itemsInBackpack.add(item);
    }

    public void fillBackpackWithItems() {
        while (true) {
            Item item = new Item(getRandom(), getRandom());
            if (leftBackpackStorage - item.getWeight() >= 0) {
                leftBackpackStorage -= item.getWeight();
                addItemInBackpack(item);
            } else {
                break;
            }
        }
    }

    public int getBackpackCapacity() {
        return backpackCapacity;
    }
}