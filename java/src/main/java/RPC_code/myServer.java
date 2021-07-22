package RPC_code;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/7/22 1:10 下午
 * @describe
 */
public class myServer {
    public static void main(String[] args) {
        RPC.Builder builder = new RPC.Builder(new Configuration());
        builder.setBindAddress("127.0.0.1");
        builder.setPort(6901);

        builder.setProtocol(myInterface.class);
        builder.setInstance(new myInterfaceImpl());

        try {
            RPC.Server server = builder.build();
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
