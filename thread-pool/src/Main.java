import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool(5, 5);

        int concurrentTasks = 5;

        new Thread(() -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            threadPool.shutdownNow();
        }).start();

        while (true) {
            for (int i=0 ; i<concurrentTasks ; i++) {
                threadPool.submit(new Task());
            }
            Thread.sleep(3000);
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
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


class ThreadPool {
    private BlockingQueue<Runnable> taskQueue;
    private Map<String, Thread> threadsMap;

    ThreadPool(int minSize, int maxTasks) {
        this.taskQueue = new ArrayBlockingQueue<>(maxTasks);
        this.threadsMap = new HashMap<>();

        for (int i=0 ; i<minSize ; i++) {
            Thread t = new Thread(new WorkerThread(this.taskQueue));
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
    }

}

class WorkerThread implements Runnable {

    private BlockingQueue<Runnable> tasks;
    private Runnable currentTask;

    WorkerThread(BlockingQueue<Runnable> tasks) {
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
            this.currentTask.run();
            System.out.println("Thread: " + threadName + " has completed the task");
        }
        System.out.println("stopping thread: " + threadName);
    }
}