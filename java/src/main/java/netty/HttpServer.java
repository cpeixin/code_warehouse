package netty;

import io.netty.bootstrap.ServerBootstrap;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/23 6:51 下午
 * @describe
 */
public class HttpServer {
    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
    }
}
