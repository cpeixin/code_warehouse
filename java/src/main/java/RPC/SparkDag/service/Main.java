package RPC.SparkDag.service;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/22 2:28 下午
 * @describe
 */
public class Main {
    public static void main(String[] args) {
        SparkDagService service = new SparkDagServiceImpl();
        RPCServer server = new RPCServer();
        server.register(service, 8864);
    }
}
