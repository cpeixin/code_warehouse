package RPC.hello.client;

import RPC.hello.service.HelloService;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/21 1:04 下午
 * @describe
 */
public class RpcClient {
    public static void main(String[] args) {
        // 生成接口的代理类对象 （代理类字节码文件是运行时生成的）
        HelloService helloService = getClient(HelloService.class, "127.0.0.1", 50001);
        // 代理类通过内部invoke方法调用接口方法hello,bye
        System.out.println(helloService.sayHello("chenqihang"));
        System.out.println(helloService.bye());
    }

    public static <T> T getClient(Class<T> clazz, String ip, int port){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = new Socket(ip, port);
                // 发送信息到服务端
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                out.writeUTF(method.getName()); //hello bye
                out.writeObject(method.getParameterTypes()); //方法参数
                out.writeObject(args);

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());                  //arg1.invoke(service,args)
                //这个方法在服务端被调用并返回结果给客户端

                return in.readObject();
            }
        });
    }
}
