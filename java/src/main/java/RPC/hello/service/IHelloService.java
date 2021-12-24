package RPC.hello.service;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/21 9:10 上午
 * @describe
 */
public class IHelloService implements HelloService{
    @Override
    public String sayHello(String name) {
        return "Hello: "+name;
    }

    @Override
    public String bye() {
        return "bye";
    }
}
