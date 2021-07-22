package RPC_code;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/7/22 1:16 下午
 * @describe
 */
public class myClient {
    public static void main(String[] args) {
        try {
            myInterface proxy = RPC.getProxy(myInterface.class, 1L, new InetSocketAddress("127.0.0.1",6901), new Configuration());
            String studentName1 = proxy.findName("20200343070326");
            String studentName2 = proxy.findName("20210123456789");
            String studentName3 = proxy.findName("20210000000000");
            System.out.println(studentName1);
            System.out.println(studentName2);
            System.out.println(studentName3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
