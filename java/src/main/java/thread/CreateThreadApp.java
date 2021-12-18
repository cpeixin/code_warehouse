package thread;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/6/26 1:58 下午
 * @describe
 */
public class CreateThreadApp {

    public static final String TEXT = "因为进程比较重量，创建进程、请求资源、资源管理和进程间切换代价太大了，如果多进程解决并发问题，可能进程切换的时间就占了一半， 但是资源必须得有啊";

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++){
            // TODO: 创建线程
            Thread thread = new Thread(new PrintStoryRunnable(TEXT, 100));
            thread.start();
        }
    }
    // TODO：实现线程
    static class PrintStoryRunnable implements Runnable {
        private String text;
        private long interval;

        public PrintStoryRunnable(String text, long interval) {
            this.text = text;
            this.interval = interval;
        }
        @Override
        public void run() {
            try {
                System.out.println("执行当前代码的线程名字叫做： " + Thread.currentThread().getName());
                printSlowly(text, interval);
                System.out.println(Thread.currentThread().getName() + "执行结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printSlowly(String str, Long interval) throws InterruptedException {
        for(char c : str.toCharArray()){
            Thread.sleep(interval);
            System.out.print(c);
        }
        System.out.println();
    }
}
