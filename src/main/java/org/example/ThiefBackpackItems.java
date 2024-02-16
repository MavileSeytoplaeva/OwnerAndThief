package org.example;

import java.util.ArrayList;
import java.util.List;

public class ThiefBackpackItems {
    private static List<Item> itemsFromThiefBackpacks = new ArrayList<>();

    public static void addBackpackItems(Backpack backpack) {
        itemsFromThiefBackpacks.addAll(backpack.getItemsInBackpack());
    }

    public static List<Item> getThiefBackpackItems() {
        return itemsFromThiefBackpacks;
    }
}
