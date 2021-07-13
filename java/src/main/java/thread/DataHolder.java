package thread;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/6/26 3:01 下午
 * @describe
 */
public class DataHolder {
    private long number = 0;
    // TODO: synchronized 会有性能影响，synchronized是对象级别的，同一对象进行调用，同时只能有一个线程进入
    public synchronized void change(long delta){
        number += delta;
    }

    public void print(){
        System.out.println("Number="+number);
    }
}
