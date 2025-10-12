package org.solve;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue {
    private Queue<Integer> queue;
    private int size;
    private Object producer;
    private Object consumer;

    public BlockingQueue(int size) {
        this.queue = new LinkedList<>();
        this.size = size;
        this.producer = new Object();
        this.consumer = new Object();
    }

    synchronized public void put(Integer value) throws InterruptedException {
        while (queue.size() == size) {
            this.wait();
        }
        System.out.println(this.queue.size());
        queue.add(value);
        this.notify();
    }

    synchronized public Integer get() throws InterruptedException {
        while (queue.size() == 0) {
            this.wait();
        }
        System.out.println(this.queue.size());
        Integer val = queue.peek();
        queue.remove();
        this.notify();
        return val;
    }

    public int getSize() {
        return this.queue.size();
    }
}
