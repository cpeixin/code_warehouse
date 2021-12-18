package thread;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/6/25 10:53 下午
 * @describe
 */
public class StopAndSeeThread {
    public static void main(String[] args){
        System.out.println(Thread.currentThread().getName());
        int a = 0;
        m1();
    }

    public static void m1(){
        System.out.println(Thread.currentThread().getName());
        int a = 1;
        m2();
    }

    public static void m2(){
        System.out.println(Thread.currentThread().getName());
        int a = 2;
        m3();
    }

    public static void m3(){
        System.out.println(Thread.currentThread().getName());
        int a = 3;
        m4();
    }

    public static void m4(){
        System.out.println(Thread.currentThread().getName());
        int a = 4;
        m5();
    }

    public static void m5(){
        System.out.println(Thread.currentThread().getName());
        int a = 5;
        m6();
    }

    public static void m6(){
        System.out.println(Thread.currentThread().getName());
        int a = 6;
        System.out.println("！！线程停止！！");
    }
}
