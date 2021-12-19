package Reflect.bean;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/17 4:57 下午
 * @describe
 * Student 类中有
 * 两个字段、
 * 两个构造方法、
 * 两个函数，
 * 且都是一个私有，一个公有。
 * 由此可知，这个测试类基本涵盖了我们平时常用的所有类成员。
 */
public class Student {
    private String studentName;
    public int studentAge;

    public Student() {
    }

    private Student(String studentName){
        this.studentName = studentName;
    }

    public void setStudentAge(int studentAge){
        this.studentAge = studentAge;
    }

    private String show(String message){
        System.out.println("show: " + studentName + "," + studentAge + "," + message);
        return "testReturnValue";
    }
}
