package reflectPart;

import reflectPart.bean.Student;
import sun.reflect.Reflection;

import java.lang.reflect.Field;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/17 5:24 下午
 * @describe
 */
public class Main {
    public static void main(String[] args) {
        try {
            getClassMethods();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取类对象的三种方式
     * @throws ClassNotFoundException
     */
    public static void getClassMethods() throws ClassNotFoundException {
        // 通过类路径字符串获取Class对象,反射获取 Class 对象的方法；
        Class studentClass = Class.forName("reflectPart.bean.Student");

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

    public void getFieldsMethods() throws ClassNotFoundException, NoSuchFieldException {
        Class studentClass = Class.forName("reflectPart.bean.Student");
        // 获取所有声明变量
        Field field = studentClass.getDeclaredField("studentAge");
        // 获取所有公有变量
        Field[] fields2 = studentClass.getFields();
    }


}
