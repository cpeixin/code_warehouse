package DesignPatterns.CallBack.demo_1;

import java.util.List;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/24 3:05 下午
 * @describe
 */

// 定义泛型接口，返回值和参数类型都灵活
public interface CallBackInterface<T> {
    T process(List<Object> param);
}
