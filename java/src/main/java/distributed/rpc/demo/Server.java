package distributed.rpc.demo;

public class Server {
    /**
     * 服务端对接口的具体实现
     */
    public static class HelloServiceImpl implements HelloService {
        @Override
        public String sayHello(String msg) {
            String result = "hello world " + msg;
            System.out.println(result);
            return result;
        }
    }
}