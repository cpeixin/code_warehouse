package thread;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/6/26 3:03 下午
 * @describe
 */
public class ChangeData implements Runnable {

    private long delta;
    private long loopCount;
    private DataHolder dataholder;

    public ChangeData(long delta, long loopCount, DataHolder dataholder){
        this.delta = delta;
        this.loopCount = loopCount;
        this.dataholder = dataholder;
    }

    @Override
    public void run() {
        for (int i = 0; i < loopCount; i++){
            dataholder.change(delta);
        }
        dataholder.print();
    }
}
