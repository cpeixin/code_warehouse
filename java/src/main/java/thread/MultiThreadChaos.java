package thread;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/6/26 3:16 下午
 * @describe
 */
public class MultiThreadChaos {
    public static void main(String[] args){
        // TODO: 多线程之间没有同步控制，计算会造成混乱
        DataHolder dh = new DataHolder();
        Thread increThread = new Thread(new ChangeData(2, 1000, dh));
        Thread decreThread = new Thread(new ChangeData(-2, 1000, dh));

        System.out.println("执行开始");

        increThread.start();
        decreThread.start();
    }
}
