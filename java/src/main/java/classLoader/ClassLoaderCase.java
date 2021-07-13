package classLoader;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/5/27 8:36 上午
 * @describe
 */
public class ClassLoaderCase {
    public static String s = "test";
    public static void main(String[] args) {
        System.out.println(ClassLoaderCase.class.getClassLoader());
    }

}
