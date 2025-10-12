package org.solve;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class OddEven {
    private int evenVal = 0;
    private int oddVal = 1;

    private volatile boolean evenTurn = true;

    synchronized public void printEven() throws InterruptedException {
        while (!evenTurn) {
            this.wait();
        }

        System.out.println("Thread: " + Thread.currentThread().getName() + " " + evenVal);
        evenVal += 2;
        evenTurn = false;
        this.notify();
    }

    synchronized public void printOdd() throws InterruptedException {
        while (evenTurn) {
            this.wait();
        }

        System.out.println("Thread: " + Thread.currentThread().getName() + " " + oddVal);
        oddVal += 2;
        evenTurn = true;
        this.notify();
    }
}
