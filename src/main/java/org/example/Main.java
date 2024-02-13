package org.example;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


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
    public static void main(String[] args) {
        ConcurrentLinkedQueue<Item> apartment = new ConcurrentLinkedQueue<>();
        Set<Thread> threads = new HashSet<>();
        int count = 0;
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        int owners = (int) (Math.random() * 10 + 3);
        Semaphore sem = new Semaphore(owners);
        int thieves = (int) (Math.random() * 10 + 3);
        System.out.println(owners);
        System.out.println(thieves);
        for (int i = 0; i < owners; i++) {
            threads.add(new Owner(sem, apartment, lock, condition));
        }
        for (int i = 0; i < thieves; i++) {
            threads.add(new Thief(apartment, (int) (Math.random() * 10 + 5), lock, condition));
        }

        try {
            for (Thread thread : threads) {
                if (thread instanceof Owner) {
                    ((Owner) thread).addItem();
                    count++;
                } else if (thread instanceof Thief) {
                    ((Thief) thread).stealItems(apartment);

                }
            }
            System.out.println(count + " items were brought");
            System.out.println("items left in apartment " + apartment.size() + " " + apartment.toString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}