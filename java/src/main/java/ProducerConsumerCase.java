import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumerCase {
    public static void main(String[] args){
        Buffer buffer = new Buffer();
        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);
        producer.start();
        consumer.start();
    }
}


class Producer extends Thread {
    private Buffer buffer;
    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }
    @Override
    public void run() {
        for (int i = 0; i<=10; i++){
            try {
                buffer.add(i);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class Consumer extends Thread {
    private Buffer buffer;
    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }
    @Override
    public void run() {
        for (int i=0; i<=10; i++){
            try {
                int value = buffer.getValue();
                System.out.println(value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Buffer{
    private BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();
    private int size = 5;

    public synchronized void add(int value) throws InterruptedException {
        if (queue.size() >= size)
            wait();
        queue.add(value);
        notify();
    }

    public synchronized int getValue() throws InterruptedException {
        if (queue.size() == 0)
            wait();
        int value = queue.poll();
        notify();
        return value;
    }
}