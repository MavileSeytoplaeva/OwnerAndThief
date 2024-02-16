package org.example;

public class OwnerPrinter implements Printer{
    private final String OWNER_BRINGS_ITEM = "Хозяин %s кладёт вещь весом %s и ценностью %s \n";
    private final String OWNER_ENTERS = "Хозяин %s пришёл \n";
    private final String OWNER_LEAVES = "Хозяин %s ушёл \n";
    @Override
    public void printThreadEnters(Thread thread) {
        System.out.printf(OWNER_ENTERS, thread.getName());
    }

    @Override
    public void printThreadDoesAction(Thread thread, Item item) {
        System.out.printf(OWNER_BRINGS_ITEM, thread.getName(), item.getWeight(), item.getValue());
    }

    @Override
    public void printThreadLeaves(Thread thread) {
        System.out.printf(OWNER_LEAVES, thread.getName());
    }
}
