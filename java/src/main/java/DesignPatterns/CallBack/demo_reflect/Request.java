package DesignPatterns.CallBack.demo_reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/24 5:35 下午
 * @describe
 */
public class Request {
    public void send(Class clazz, Method method){
        // 模拟等待响应
        try {
            Thread.sleep(3000);
            System.out.println("[Request]:收到响应");
            method.invoke(clazz.newInstance());
        } catch (InterruptedException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
