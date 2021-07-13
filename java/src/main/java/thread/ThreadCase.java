package thread;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/6/25 10:40 下午
 * @describe
 */
public class ThreadCase {
    public static void main(String[] args) throws InterruptedException {
        String str = "因为进程比较重量，创建进程、请求资源、资源管理和进程间切换代价太大了，如果多进程解决并发问题，可能进程切换的时间就占了一半， 但是资源必须得有啊";
        threadProcess(str, 3000);
    }

    public static void threadProcess(String str, Integer interval) throws InterruptedException {
        for(char c : str.toCharArray()){
            Thread.sleep(interval);
            System.out.print(c);
        }
        System.out.println();
    }
}
