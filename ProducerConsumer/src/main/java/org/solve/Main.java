package org.solve;

import java.util.Random;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue queue = new BlockingQueue(10);

        Thread producer = new Thread(() -> {
            int i = 20;
           while (i > 0) {
               int producedValue = new Random().nextInt();
               try {
                   queue.put(producedValue);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
               System.out.println("produced value " + producedValue);

           }
        });

        Thread consumer = new Thread(() -> {
            while (true) {
                int gotValue;
                try {
                    gotValue = queue.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("consumed value " + gotValue);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

}

