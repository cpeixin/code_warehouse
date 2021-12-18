package DesignPatterns.EventBus;


import sun.plugin2.message.Message;

public interface Observer {
    void update(Message message);
}
