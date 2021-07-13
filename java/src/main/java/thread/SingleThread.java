package thread;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/6/26 3:12 下午
 * @describe
 */
public class SingleThread {
    // TODO: 单线程调用
    public static void main(String[] args){
        DataHolder dh = new DataHolder();
        ChangeData increa = new ChangeData(2, 1000, dh);
        increa.run();
        ChangeData decrea = new ChangeData(-2, 1000, dh);
        decrea.run();
    }
}
