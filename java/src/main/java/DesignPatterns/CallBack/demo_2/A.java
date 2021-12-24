package DesignPatterns.CallBack.demo_2;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/24 3:37 下午
 * @describe
 * 回调：根本目的是，类A想要了解它本身无法知道，或者无法控制的事，但是这件事可以由类B替它了解或者控制。
 *      类B在这里是起到观察，通知的作用
 *
 *
 * 场景：回调两个应用场景也就顺理成章了。
 *      - 模块之间的解耦
 *      - 异步调用
 */
public class A {
    private B b = new B();

    public void a(){
        b.b(new A());
    }

    public void callBack(){
        // do something
        System.out.println("is doing A.callBack function");
    }
}
