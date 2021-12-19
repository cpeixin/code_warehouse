package Reflect;

import Reflect.bean.Student;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/17 5:24 下午
 * @describe
 * // https://zhuanlan.zhihu.com/p/86293659#:~:text=%E5%8F%8D%E5%B0%84%EF%BC%88Reflection%EF%BC%89%20%E6%98%AF%20Java%20%E7%A8%8B%E5%BA%8F%E5%BC%80%E5%8F%91%E8%AF%AD%E8%A8%80%E7%9A%84%E7%89%B9%E5%BE%81%E4%B9%8B%E4%B8%80%EF%BC%8C%E5%AE%83%E5%85%81%E8%AE%B8%E8%BF%90%E8%A1%8C%E4%B8%AD%E7%9A%84%20Java%20%E7%A8%8B%E5%BA%8F%E5%AF%B9%E8%87%AA%E8%BA%AB%E8%BF%9B%E8%A1%8C%E6%A3%80%E6%9F%A5%EF%BC%8C%E6%88%96%E8%80%85%E8%AF%B4%22%E8%87%AA%E5%AE%A1%22%EF%BC%8C%E5%B9%B6%E8%83%BD%E7%9B%B4%E6%8E%A5%E6%93%8D%E4%BD%9C%E7%A8%8B%E5%BA%8F%E7%9A%84%E5%86%85%E9%83%A8%E5%B1%9E%E6%80%A7%E5%92%8C%E6%96%B9%E6%B3%95%E3%80%82.%20%E5%8F%8D%E5%B0%84%E6%98%AF%E4%B8%80%E9%A1%B9%E9%AB%98%E7%BA%A7%E5%BC%80%E5%8F%91%E4%BA%BA%E5%91%98%E5%BA%94%E8%AF%A5%E6%8E%8C%E6%8F%A1%E7%9A%84%22%E9%BB%91%E7%A7%91%E6%8A%80%22%EF%BC%8C%E5%85%B6%E5%AE%9E%E5%8F%8D%E5%B0%84%E5%B9%B6%E4%B8%8D%E6%98%AF,Java%20%E7%8B%AC%E6%9C%89%E7%9A%84%EF%BC%8C%E8%AE%B8%E5%A4%9A%E7%BC%96%E7%A8%8B%E8%AF%AD%E8%A8%80%E9%83%BD%E6%8F%90%E4%BE%9B%E4%BA%86%E5%8F%8D%E5%B0%84%E5%8A%9F%E8%83%BD%E3%80%82.%20%E5%9C%A8%E9%9D%A2%E8%AF%95%E4%B8%AD%E9%9D%A2%E8%AF%95%E5%AE%98%E4%B9%9F%E7%BB%8F%E5%B8%B8%E5%AF%B9%E5%8F%8D%E5%B0%84%E9%97%AE%E9%A2%98%E8%BF%9B%E8%A1%8C%E8%80%83%E5%AF%9F%EF%BC%8C%E5%8F%8D%E5%B0%84%E6%98%AF%E6%89%80%E6%9C%89%E6%B3%A8%E8%A7%A3%E5%AE%9E%E7%8E%B0%E7%9A%84%E5%8E%9F%E7%90%86%EF%BC%8C%E5%B0%A4%E5%85%B6%E5%9C%A8%E6%A1%86%E6%9E%B6%E8%AE%BE%E8%AE%A1%E4%B8%AD%EF%BC%8C%E6%9C%89%E4%B8%8D%E5%8F%AF%E6%9B%BF%E4%BB%A3%E7%9A%84%E4%BD%9C%E7%94%A8%E3%80%82.%20%E5%85%B3%E4%BA%8E%E5%8F%8D%E5%B0%84%EF%BC%8C%E5%B8%B8%E8%A7%81%E7%9A%84%E9%9D%A2%E8%AF%95%E8%80%83%E5%AF%9F%E7%82%B9%E5%8C%85%E6%8B%AC%EF%BC%9A.%20%E5%A6%82%E4%BD%95%E5%8F%8D%E5%B0%84%E8%8E%B7%E5%8F%96%20Class%20%E5%AF%B9%E8%B1%A1.
 */
public class Main {
    public static void main(String[] args) {

        try {
            // 获取类
            Class studentClass = Class.forName("Reflect.bean.Student");
            // 获取构造函数，用来实例化，创建对象
            Constructor studentConstructor = studentClass.getDeclaredConstructor(String.class);
            // 私有构造函数需要进行设置
            studentConstructor.setAccessible(true);
            // 利用构造函数创建对象
            // Student student = (Student) studentConstructor.newInstance("cong");
            Object student = studentConstructor.newInstance("cong");
            // 获取声明的字段
            Field studentAgeField =studentClass.getDeclaredField("studentAge");
            // 字段赋值
            studentAgeField.set(student, 28);
            // 获取声明函数
            Method studentShowMethod = studentClass.getDeclaredMethod("show", String.class);
            // 私有函数使用设置
            studentShowMethod.setAccessible(true);
            // 函数的使用,使用函数的invoke方法调用此函数，传入此对象以及函数所需参数
            Object result = studentShowMethod.invoke(student, "message");

            System.out.println("result: " + result);

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取类对象的三种方式
     * @throws ClassNotFoundException
     */
    public static void getClassObjectMethods() throws ClassNotFoundException {
        // 通过类路径字符串获取Class对象,反射获取 Class 对象的方法；
        Class studentClass = Class.forName("Reflect.bean.Student");

        // 通过类的Class属性,限制条件：需要导入类的包；
        Class studentClass2 = Student.class;

        // 通过对象的getClass函数,已经有了 Student 对象，不再需要反射。
        Student student = new Student();
        Class studentClass3 = student.getClass();

        System.out.println("class1 = " + studentClass + "\n" +
                "class2 = " + studentClass2 + "\n" +
                "class3 = " + studentClass3 + "\n" +
                "class1 == class2 ? " + (studentClass == studentClass2) + "\n" +
                "class2 == class3 ? " + (studentClass2 == studentClass3));
        // 以上主要是证明，Java运行时 每个类会生成一个Class对象
    }

    /**
     * 获取类对象声明变量的两种方式
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     */
    public static void getFieldsMethods() throws ClassNotFoundException, NoSuchFieldException {
        Class studentClass = Class.forName("Reflect.bean.Student");
        // 获取所有声明变量
        Field[] fields = studentClass.getDeclaredFields();
        // 获取所有公有变量
        Field[] fields2 = studentClass.getFields();

        for(Field field : fields) {
            System.out.println("declared Field: " + field);
        }

        for(Field field : fields2) {
            System.out.println("field: " + field);
        }

    }

    /**
     * 获取类对象的构造方法两种方式
     * @throws ClassNotFoundException
     */
    public static void getConstructorMethods() throws ClassNotFoundException {
        Class studentClass = Class.forName("Reflect.bean.Student");

        // 获取所有声明的构造函数
        Constructor[] constructors = studentClass.getDeclaredConstructors();
        for(Constructor constructor: constructors){
            System.out.println("declared Constructor: " + constructor);
        }

        // 获取所有公有的构造函数
        Constructor[] constructors2 = studentClass.getConstructors();
        for(Constructor constructor: constructors2){
            System.out.println("Constructor: " + constructor);
        }
    }

    /**
     * 获取非构造函数的方式
     * @throws ClassNotFoundException
     */
    public static void getClassMethods() throws ClassNotFoundException {
        Class studentClass = Class.forName("Reflect.bean.Student");

        Method[] declaredMethodList = studentClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethodList) {
            System.out.println("declared Method: " + declaredMethod);
        }
        // 2.获取所有公有的函数
        // getMethods 方法不仅获取到了我们声明的公有方法setStudentAge，还获取到了很多 Object 类中的公有方法。因为 Object 是所有 Java 类的父类。
        Method[] methodList = studentClass.getMethods();
        for (Method method : methodList) {
            System.out.println("method: " + method);
        }

    }

}
