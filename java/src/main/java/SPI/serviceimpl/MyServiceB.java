package SPI.serviceimpl;

import SPI.serviceprovider.MyService;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/5/28 8:35 上午
 * @describe
 */
public class MyServiceB implements MyService {

    @Override
    public void doSomething() {
        System.out.println("Hello,MyService B!");
    }
}