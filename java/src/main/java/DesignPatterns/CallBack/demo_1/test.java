package DesignPatterns.CallBack.demo_1;

import java.util.List;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/24 3:09 下午
 * @describe
 */
public class test {
    public static void main(String[] args) {
        A a = new A();

        a.execute(new CallBackInterface() {
            @Override
            public Object process(List param) {
                List<String> wordList = param;
                wordList.remove("1");
                System.out.println(wordList);
                return true;
            }
        });

        a.execute(new CallBackInterface() {
            @Override
            public Object process(List param) {
                List<String> wordList = param;
                wordList.add("100");
                System.out.println(wordList);
                return true;
            }
        });
    }
}
