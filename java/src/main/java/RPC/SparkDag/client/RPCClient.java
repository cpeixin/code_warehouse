package RPC.SparkDag.client;

import RPC.SparkDag.service.SparkDagService;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/22 2:29 下午
 * @describe
 */
public class RPCClient {
    public static void main(String[] args) throws InterruptedException {
        // 获取动态代理
        SparkDagService client = getClient(SparkDagService.class, "127.0.0.1", 8864);
        // 代理类通过内部 invoke方法调用接口方法
        System.out.println(client.submitJob("Flink TopN"));
        client.generateDAG();
        client.dagSchedule();
        client.stage2Task();
        client.taskSchedule();
        System.out.println(client.completedJob("Flink TopN"));

    }

    public static <T> T getClient(Class<T> service, String host, int port) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(),new Class<?>[]{service}, new InvocationHandler(){
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = new Socket(host, port);
                // 发送信息到服务端
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                out.writeUTF(method.getName());
                out.writeObject(method.getParameterTypes());
                out.writeObject(args);

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());                  //arg1.invoke(service,args)
                //这个方法在服务端被调用并返回结果给客户端

                return in.readObject();
            }
        });
    }


}
