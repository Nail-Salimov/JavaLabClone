import java.util.Deque;
import java.util.LinkedList;

public class ThreadPool {
        private PoolWorker threads[];
        private final Deque<Runnable> tasks;
        public ThreadPool(int threadCount) {
            tasks = new LinkedList<>();
            threads = new PoolWorker[threadCount];
            for (int i = 0; i < threadCount; i++) {
                threads[i] = new PoolWorker();
                threads[i].start();
            }
        }
        public void submit(Runnable task) {
            synchronized (tasks) {
                tasks.add(task);
                tasks.notify();
            }
        }
    private class PoolWorker extends Thread {
        @Override
        public void run() {
            Runnable task;
            while (true) {
                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                    task = tasks.removeFirst();
                }
                task.run();
            }
        }
    }
}
