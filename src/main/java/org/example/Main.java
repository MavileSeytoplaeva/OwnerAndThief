package org.example;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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
        AtomicInteger totalItems = new AtomicInteger(0);
        int owners = (int) (Math.random() * 10 + 5);
        Semaphore sem = new Semaphore(owners);
        int thieves = (int) (Math.random() * 10 + 5);
        System.out.println("Хозяев: " + owners);
        System.out.println("Воров: " + thieves);
        System.out.println("------------");
        Apartment apartment = new Apartment();
        ThreadPool threadPool = new ThreadPool(owners,thieves, sem, apartment, totalItems);
        threadPool.execute(null);
        sleep(500);
        System.out.println("Всего принесли " + totalItems + "вещей");
        System.out.println("Вещи, которые остались в квартире " + apartment.apartmentItems.size() + " " + apartment.apartmentItems);

    }
}