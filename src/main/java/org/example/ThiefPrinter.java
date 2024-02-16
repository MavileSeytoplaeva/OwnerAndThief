package org.example;

public class ThiefPrinter implements Printer {
    private final String THIEF_ENTERS = "Вор %s пришёл \n";
    private final String THIEF_STEALS = "Вор %s крадёт вёщь весом %s и ценой %s \n";
    private final String THIEF_LEAVES = "Вор %s ушёл \n";
    private final String THIEF_BACKPACK_CAPACITY_NOT_ENOUGH = "Вместимость рюкзака вора %s. Этого недостаточно, поэтому он уходит пустой \n";
    private final String APARTMENT_EMPTY = "Квартира пустая \n";


    @Override
    public void printThreadEnters(Thread thread) {
        System.out.printf(THIEF_ENTERS, thread.getName());
    }

    @Override
    public void printThreadDoesAction(Thread thread, Item item) {
        System.out.printf(THIEF_STEALS, thread.getName(), item.getWeight(), item.getValue());

    }

    @Override
    public void printThreadLeaves(Thread thread) {
        System.out.printf(THIEF_LEAVES, thread.getName());
    }

    public void printThiefBackpackCapacityIsNotEnough(Thread thread) {
        if (thread instanceof Thief) {
            System.out.printf(THIEF_BACKPACK_CAPACITY_NOT_ENOUGH, ((Thief) thread).getBackpackWithItems().getBackpackCapacity());
        }
    }

    public void printApartmentIsEmpty() {
        System.out.printf(APARTMENT_EMPTY);
    }
}
