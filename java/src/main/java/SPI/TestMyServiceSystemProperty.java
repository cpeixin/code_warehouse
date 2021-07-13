package SPI;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/5/28 8:37 上午
 * @describe
 */
public class TestMyServiceSystemProperty {

    public static void main(String[] args) {
        System.out.println("hello,world!\n ");

        String path1 = System.getProperty("sun.boot.class.path");
        System.out.println("BootStrapClassLoader 加载的资源:" + path1);

        String path2 = System.getProperty("java.ext.dirs");
        System.out.println("ExtClassLoader 加载的资源：" + path2);

        String path3 = System.getProperty("java.class.path");
        System.out.println("AppClassLoader 加载的资源:" + path3);
    }
}