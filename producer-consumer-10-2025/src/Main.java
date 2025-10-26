import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String []args) throws InterruptedException {
        Random r = new Random();
        BlockingQueue<Integer> bq = new BlockingQueue<>(4);
        long producerSleep = 2000;
        long consumerSleep = 3000;

        Runnable producer = () -> {
            try {
                while (true) {
                    int val = r.nextInt(100);
                    bq.put(val);
                    Thread.sleep(producerSleep);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable consumer = () -> {
            try {
                while (true) {
                    int val = bq.take();
                    Thread.sleep(consumerSleep);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Thread t1 = new Thread(producer);
        Thread t2 = new Thread(producer);
        Thread t3 = new Thread(consumer);
        Thread t4 = new Thread(consumer);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
    }
}


class BlockingQueue <T> {
    private final Queue<T> queue;
    private final int maxSize;

    BlockingQueue(int size) {
        this.queue = new LinkedList<>();
        this.maxSize = size;
    }

    public synchronized void put(T val) throws InterruptedException {
        while (queue.size() == this.maxSize) {
            this.wait();
        }
        this.queue.add(val);
        System.out.println("produced: " + val);
        System.out.println(this.queue.size());
        this.notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (this.queue.isEmpty()) {
            this.wait();
        }
        T val = this.queue.poll();
        System.out.println("consumed: " + val);
        System.out.println(this.queue.size());
        this.notifyAll();
        return val;
    }
}