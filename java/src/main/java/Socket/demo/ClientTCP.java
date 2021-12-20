package Socket.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/20 9:18 上午
 * @describe
 */
public class ClientTCP {
    public static void main(String[] args) throws IOException {
        System.out.println("客户端发送数据...");

        // 创建服务端Socket
        Socket client = new Socket("127.0.0.1",8866);

        // 输出流
        OutputStream os = client.getOutputStream();
        // 写出数据
        os.write("连接服务器".getBytes());

        InputStream in = client.getInputStream();
        byte[] b = new byte[1024];
        int len = in.read(b);
        System.out.println(new String(b,0,len));

        // 关闭资源
        in.close();
        os.close();
        client.close();
    }
}
