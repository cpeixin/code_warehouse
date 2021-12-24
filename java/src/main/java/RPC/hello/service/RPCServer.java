package RPC.hello.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/21 9:13 上午
 * @describe
 */
public class RPCServer {
    // 创建线程池
    private ExecutorService threadPool;

    private static final int DEFAULT_THREAD_NUM = 50;

     public RPCServer() {
         threadPool = Executors.newFixedThreadPool(DEFAULT_THREAD_NUM);
     }

     // 服务注册, 服务端调用。
    public void register(Object service, int port) {
        System.out.println("server starts...");
        try {
            ServerSocket server = new ServerSocket(port);
            Socket socket = null; //接收客户端发来的请求
            while ((socket = server.accept()) != null){
                System.out.println("client connected");
                threadPool.execute(new Processor(socket, service));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



     class Processor implements Runnable {
         Socket socket;
         Object service;

         public Processor(Socket socket, Object service) {
             this.socket = socket;
             this.service = service;
         }

         public void process() {}


         @Override
         public void run() {
             try {
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                 String methodName = in.readUTF(); // utf-8编码读取
                 Class<?>[] parameterTypes = (Class<?>[]) in.readObject(); // 参数类型
                 Object[] parameters = (Object[]) in.readObject(); // 参数

                 Method method = service.getClass().getMethod(methodName, parameterTypes);// 方法名以及参数调用服务类的相关方法

                 try {
                     Object result = method.invoke(service, parameters);
                     ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                     out.writeObject(result);
                 } catch (IllegalAccessException e) {
                     e.printStackTrace();
                 } catch (InvocationTargetException e) {
                     e.printStackTrace();
                 }


             } catch (IOException e) {
                 e.printStackTrace();
             } catch (ClassNotFoundException | NoSuchMethodException e) {
                 e.printStackTrace();
             }
         }
     }


}
