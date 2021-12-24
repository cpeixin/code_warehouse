package RPC.SparkDag.service;

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
 * @date 2021/12/22 9:16 上午
 * @describe
 */
public class RPCServer {
    private final ExecutorService threadPool;

    private static final int DEFAULT_THREAD_NUMS = 50;

    public RPCServer() {
        threadPool = Executors.newFixedThreadPool(DEFAULT_THREAD_NUMS);
    }

    public void register(Object service, int port) {
        System.out.println("server starts...");
        try {
            ServerSocket server = new ServerSocket(port);
            Socket socket = null;
            while((socket=server.accept())!=null){
                System.out.println("conntected!!!");
                threadPool.execute(new Processor(service, socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    static class Processor implements Runnable{
        Object service;
        Socket socket;

        public Processor(Object service, Socket socket) {
            this.service = service;
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                String methodName = in.readUTF();
                Class<?>[] parameterTypes = (Class<?>[]) in.readObject();
                Object[] args = (Object[])in.readObject();
                Method method = service.getClass().getMethod(methodName, parameterTypes);

                try {
                    Object result = method.invoke(service, args);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(result);

                    socket.close();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }


}
