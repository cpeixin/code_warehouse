package classLoader;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/5/27 9:14 上午
 * @describe
 */
public class CustomClassLoaderExample {
    public static void main(String[] args) throws Exception {
        ClassLoader myClassLoader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream inputStream = getClass().getResourceAsStream(fileName);
                    if (inputStream == null) {
                        return super.findClass(name);
                    }
                    byte[] bytes = new byte[inputStream.available()];
                    inputStream.read(bytes);
                    return defineClass(name, bytes, 0, bytes.length);
                } catch (IOException ex) {
                    throw new ClassNotFoundException(name, ex);
                }
            }
        };

        Object cl = myClassLoader.loadClass("classLoader.CustomClassLoaderExample");
        System.out.println(cl instanceof CustomClassLoaderExample);
    }
}