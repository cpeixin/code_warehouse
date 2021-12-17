package DesignPatterns.EventBus;

import sun.plugin2.message.Message;

public class ConcreteObserverOne implements Observer {
    @Override
    public void update(Message message) {
        // TODO: 获取消息通知，实现自己的方法
        System.out.println("ConcreteObserverOne is notified");
    }
}
