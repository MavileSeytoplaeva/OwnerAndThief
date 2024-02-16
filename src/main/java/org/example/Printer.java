package org.example;

public interface Printer {
    void printThreadEnters(Thread thread);

    void printThreadDoesAction(Thread thread, Item item);

    void printThreadLeaves(Thread thread);
}
