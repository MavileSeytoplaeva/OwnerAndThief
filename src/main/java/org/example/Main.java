package org.example;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;


// Необходимо реализовать многопоточное приложение, которое решает следующую задачу:
//Есть два типа пользователя (два типа потока). Один - Хозяин, имеет в своем арсенале список вещей (Вещь: цена и вес),
// второй - Вор, имеет рюкзак (Рюкзак: предельный вес, который может в себя вместить).
// Поток Хозяина выполняет работу по выкладыванию вещей в квартиру.
// Поток Вора - забирает вещи из квартиры.
// При этом Вор должен забрать такие вещи, чтобы их ценность была максимальной и вес их должен
// быть меньше предельного веса, который может поместиться в рюкзак.
//
//Объектные модели:
//1. Вещь; атрибуты: вес, ценность
//2. Хозяин; атрибуты: Вещи; действия: внести вещи в квартиру
//3. Рюкзак; атрибуты: предельный вес
//3. Вор; атрибуты: рюкзак. Действия: сложить вещи в рюкзак.
//
//Ограничения:
//1. Если работает поток Хозяина, то вор не должен класть вещи в рюкзак.
//2. Если работает Вор, то Хозяин не может войти в квартиру
//
//Возможные ограничения системы:
//1. Хозяев может быть 1..n.
//2. потоки Хозяев БЕЗ взаимной блокировки: несколько хозяев могут выкладывать вещи в квартиру одновременно
//3. Воров может быть 1..m.
//4. Потоки Воров со ВЗАИМНОЙ блокировкой: воровать одновременно может только 1 вор."
public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<Item> initialListWithItems = new ArrayList<>();
        List<Item> allStolenItems = new ArrayList<>();

        int ownersCount = (int) (Math.random() * 10 + 5);
        int thievesCount = (int) (Math.random() * 10 + 5);
        System.out.println("Хозяев: " + ownersCount);
        System.out.println("Воров: " + thievesCount);
        System.out.println("------------");
        Apartment apartment = new Apartment();
        ThreadPool threadPool = new ThreadPool(ownersCount, thievesCount, apartment);

        threadPool.getThreads().stream()
                .filter(thread -> thread instanceof Owner)
                .forEach(owner -> {
                    initialListWithItems.addAll(((Owner) owner).getBackpackWithItems());
                });
        threadPool.startThreads();
        threadPool.joinAllThreads();
        threadPool.threads.forEach(
                thread -> {
                    if (thread instanceof Thief) {
                        allStolenItems.addAll(((Thief) thread).getBackpackWithItems().getItemsInBackpack());
                    }
                }
        );

//        List<Item> listOfItemsFromThiefBackpacks = allStolenItems;
        List<Item> listOfItemsLeftInApartment = apartment.getApartmentItems().stream().toList();
        List<Item> newListOfItemsToCompare = new ArrayList<>();
        newListOfItemsToCompare.addAll(listOfItemsLeftInApartment);
        newListOfItemsToCompare.addAll(allStolenItems);

        System.out.println("Всего принесли " + initialListWithItems.size() + " вещей");
        System.out.println("В квартире остались " + listOfItemsLeftInApartment.size() + " вещей: " + listOfItemsLeftInApartment);
        System.out.println("В рюкзаках нашли " + allStolenItems.size() + " вещей:" + allStolenItems);
        if (initialListWithItems.size() == newListOfItemsToCompare.size()
                && initialListWithItems.containsAll(newListOfItemsToCompare)) {
            System.out.println("Никакие вещи не потерялись");
        } else {
            throw new RuntimeException("Где-то потеряли вещи");
        }
    }
}