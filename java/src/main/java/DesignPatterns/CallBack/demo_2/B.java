package DesignPatterns.CallBack.demo_2;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/24 3:36 下午
 * @describe
 */
public class B {
    public void b(A a){
        // do somethings
        System.out.println("is doing B.b() function");
        a.callBack();
    }
}
