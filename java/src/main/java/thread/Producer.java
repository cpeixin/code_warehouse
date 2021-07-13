package thread;

import java.util.Queue;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/6/26 4:45 下午
 * @describe
 */
public class Producer<T> {
    private Queue<T> tasks;
    private int maxTaskCount = 0;

    public Producer(Queue<T> tasks, int maxTaskCount) {
        this.tasks = tasks;
        this.maxTaskCount = maxTaskCount;
    }

    public void produce(T task) throws InterruptedException {
        synchronized (tasks){
            while(tasks.size() > maxTaskCount){
                System.out.println("生产者线程进入等待： " + Thread.currentThread().getName());
                tasks.wait();
            }
            tasks.add(task);
            tasks.notifyAll();
        }
    }
}
