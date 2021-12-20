package Socket.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/20 9:11 上午
 * @describe
 */
public class ServerTCP {
    public static void main(String[] args) throws IOException {

        System.out.println("服务端启动  等待连接中.....");

        // 创建ServerSocket对象 绑定端口  开始等待连接
        ServerSocket serverSocket = new ServerSocket(8866);
        // 创建Socket对象，阻塞！！ 返回socket对象
        Socket socket = serverSocket.accept();
        // 服务端获取输入对象
        InputStream is = socket.getInputStream();

        //创建字节数组
        byte[] b = new byte[1024];
        //读取字符数组
        int len = is.read(b);
        //解析字符数组
        String msg = new String(b,0,len);
        System.out.println(msg);
        // 回应请求
        OutputStream os = socket.getOutputStream();
        os.write("我已经收到了请求".getBytes());

        //关闭资源
        os.close();
        is.close();
        socket.close();
    }
}
