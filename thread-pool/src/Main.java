import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Main {
    public static void main(String[] args) throws InterruptedException {
//      ThreadPool threadPool = new FixedSizeThreadPool(5, 5);
        ThreadPool threadPool = new DynamicSizeThreadPool(5, 1000);

        int concurrentTasks = 1;

        // thread for shutting everything down
        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            threadPool.shutdownNow();
        }).start();

        // tasks producer loop
        while (true) {
            for (int i=0 ; i<concurrentTasks ; i++) {
                threadPool.submit(new Task());
            }
            Thread.sleep(500);
        }
    }
}

class Task implements Runnable {

    private String taskId;

    Task() {
        this.taskId = UUID.randomUUID().toString();
    }

    @Override
    public void run() {
           System.out.println("Task: " + this.taskId + " in progress");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

interface ThreadPool {
    public void submit(Runnable task) throws InterruptedException ;
    public void shutdownNow();
}

class FixedSizeThreadPool implements ThreadPool {
    protected BlockingQueue<Runnable> taskQueue;
    protected Map<String, Thread> threadsMap;

    public int getMinThreadPoolSize() {
        return minThreadPoolSize;
    }

    private int minThreadPoolSize;

    FixedSizeThreadPool(int minSize, int maxTasks) {
        this.minThreadPoolSize = minSize;
        this.taskQueue = new ArrayBlockingQueue<>(maxTasks);
        this.threadsMap = new HashMap<>();

        for (int i=0 ; i<minSize ; i++) {
            Thread t = new Thread(new Worker(this.taskQueue));
            this.threadsMap.put(t.getName(), t);
            t.start();
        }
    }

    public void submit(Runnable task) throws InterruptedException {
        this.taskQueue.put(task);
    }

    public void shutdownNow() {
        for (Map.Entry<String, Thread> e : this.threadsMap.entrySet()) {
            String threadName = e.getKey();
            System.out.println("trying to stop thread: " + threadName);
            e.getValue().interrupt();
        }

        for (Map.Entry<String, Thread> entry : this.threadsMap.entrySet()) {
            try {
                entry.getValue().join();
            } catch (InterruptedException e) {
                return;
            }
        }
        System.out.println("All worker threads successfully stopped!!");
    }
}

class DynamicSizeThreadPool extends FixedSizeThreadPool {

    private Thread balancerThread;

    DynamicSizeThreadPool(int minSize, int maxTasks) {
        super(minSize, maxTasks);
        this.balancerThread = dynamicallyBalanceThread();
        this.balancerThread.start();
    }

    @Override
    public void shutdownNow() {
        super.shutdownNow();
        this.balancerThread.interrupt();
    }

    private Thread dynamicallyBalanceThread() {
        return new Thread(this::threadBalancerRunner);
    }

    private void threadBalancerRunner() {
        String prevPeekObjectId = "";
        while (!Thread.currentThread().isInterrupted()) {

            Runnable latestTask = this.taskQueue.peek();
            String currPeekObjectId;
            if (latestTask != null) {
                currPeekObjectId = latestTask.toString();
            } else {
                currPeekObjectId = "NO-OBJECT";
            }

            if (prevPeekObjectId.compareTo(currPeekObjectId) == 0) {
                System.out.println("Waiting task detected, creating new worker thread to help");
                Thread workerThread = new Thread(new Worker(this.taskQueue));
                workerThread.start();
                this.threadsMap.put(workerThread.getName(), workerThread);
                System.out.println("Total THREADS: " + this.threadsMap.size());
            } else if (this.threadsMap.size() > this.getMinThreadPoolSize() && this.taskQueue.isEmpty()) {
                Map.Entry<String, Thread> e = this.threadsMap.entrySet().stream().findFirst().orElse(null);
                if (e != null) {
                    String threadName = e.getKey();
                    Thread workerThread = e.getValue();
                    System.out.println("Excess Threads detected, Stopping Worker Thread: " + threadName);
                    workerThread.interrupt();
                    this.threadsMap.remove(threadName);
                }
            }

            if (!currPeekObjectId.equals("NO-OBJECT")) {
                prevPeekObjectId = currPeekObjectId;
            }

            try{
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Stopping balancer thread");
                break;
            }
        }
    }

}

class Worker implements Runnable {

    private BlockingQueue<Runnable> tasks;
    private Runnable currentTask;

    Worker(BlockingQueue<Runnable> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                this.currentTask = tasks.take();
                System.out.println("Thread: " + threadName + " has picked up a task");
            } catch (InterruptedException e) {
                System.out.println("Thread: " + threadName + " was interrupted");
                break;
            }

            try {
                this.currentTask.run();
            } catch (RuntimeException e) {
                if (e.getCause() instanceof InterruptedException) {
                    System.out.println("Thread: " + threadName + " was interrupted");
                    break;
                }
            }
            System.out.println("Thread: " + threadName + " has completed the task");
        }
        System.out.println("stopping thread: " + threadName);
    }
}