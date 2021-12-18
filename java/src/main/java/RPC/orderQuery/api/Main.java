package RPC.orderQuery.api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/16 9:00 上午
 * @describe
 */
public class Main {
    // rpc方法实现
    public static Object rpc(final Class iProductServiceClass) {
        return Proxy.newProxyInstance(iProductServiceClass.getClassLoader(), new Class[]{iProductServiceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = new Socket("127.0.0.1", 19088);

                String apiClassName = iProductServiceClass.getName();
                String methodName = method.getName();
                Class[] parameterTypes = method.getParameterTypes();

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeUTF(apiClassName);
                objectOutputStream.writeUTF(methodName);
                objectOutputStream.writeObject(parameterTypes);
                objectOutputStream.writeObject(args);
                objectOutputStream.flush();

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Object o = objectInputStream.readObject();
                objectInputStream.close();
                objectOutputStream.close();

                socket.close();
                return o;
            }
        });
    }

    // rpc启动
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(19088);
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                String apiClassName = objectInputStream.readUTF();
                String methodName = objectInputStream.readUTF();
                Class[] parameterTypes = (Class[]) objectInputStream.readObject();
                Object[] args4method = (Object[]) objectInputStream.readObject();

                Class clazz = null;

                if (apiClassName.equals(IProductService.class.getName())) {
                    clazz = ProductService.class;
                }
                Method method = clazz.getMethod(methodName, parameterTypes);
                Object invoke = method.invoke(clazz.newInstance(), args4method);

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(invoke);
                objectOutputStream.flush();

                objectInputStream.close();
                objectOutputStream.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
