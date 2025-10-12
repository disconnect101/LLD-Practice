package org.solve;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        OddEven oddEven = new OddEven();

        Thread threadEven = new Thread(() -> {
            while (true) {
                try {
                    oddEven.printEven();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread threadOdd = new Thread(() -> {
            while (true) {
                try {
                    oddEven.printOdd();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        threadOdd.start();
        threadEven.start();


    }
}