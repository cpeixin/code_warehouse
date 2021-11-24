package distributed.rpc.demo;

/**
 * 本地调用：客户端和服务端之间应该是依赖于接口，而不必依赖于具体的实现的
 */
public class Client {

    public static void main(String[] args) {
        HelloService service = new Server.HelloServiceImpl();
        service.sayHello("直接调用");
    }
}