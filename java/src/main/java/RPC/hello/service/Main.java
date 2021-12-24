package RPC.hello.service;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/21 1:02 下午
 * @describe
 */
public class Main {
    public static void main(String[] args) {
        HelloService service = new IHelloService();
        RPCServer server = new RPCServer();
        server.register(service, 50001);
    }
}
