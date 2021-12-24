package DesignPatterns.CallBack.demo_1;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/24 3:01 下午
 * @describe
 */
public class A {
    public List<String> wordList = loadList();


    public <T> T execute(CallBackInterface callback) {
        return (T) callback.process(wordList);
    }


    // 加载词到内存中
    public List<String> loadList() {
        List<String> wordList = new ArrayList<String>();
        for(int i=0;i<10;i++){
            wordList.add(Integer.toString(i));
        }
        return wordList;
    }
}
