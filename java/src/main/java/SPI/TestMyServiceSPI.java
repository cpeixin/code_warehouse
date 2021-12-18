package SPI;

import SPI.serviceprovider.MyService;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/5/28 8:29 上午
 * @describe
 *
 * 服务提供者提供了接口的具体实现后，需要在资源文件夹中创建 META-INF/services 文件夹，并且新建一个以全类名为名字的文本文件，
 * 文件内容为实现类的全名（如图中下面的红框）；
 * 接口实现类必须在工程的 classpath 下，也就是 maven 中需要加入依赖或者 jar 包引用到工程里
 * （如图中的 serviceimpl 包，我就放在了当前工程下了，执行的时候，会把类编译成 class 文件放到当前工程的 classpath 下的）；
 * SPI 的实现类中，必须有一个不带参数的空构造方法
 *
 */
public class TestMyServiceSPI {

    public static void main(String[] args) {
        ServiceLoader<MyService> services = ServiceLoader.load(MyService.class);
        Iterator<MyService> iterator = services.iterator();
        while (iterator.hasNext()) {
            MyService myService = iterator.next();
            myService.doSomething();
        }
    }
}
