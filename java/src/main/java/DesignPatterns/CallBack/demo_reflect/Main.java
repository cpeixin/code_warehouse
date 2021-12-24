package DesignPatterns.CallBack.demo_reflect;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/24 5:40 下午
 * @describe
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Request request = new Request();

        System.out.println("[Main]:我开个线程去异步发请求");

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    request.send(CallBack.class, CallBack.class.getMethod("processResponse"));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        System.out.println("[Main]:请求发完了，我去干点别的");
        Thread.sleep(100000);

    }
}
