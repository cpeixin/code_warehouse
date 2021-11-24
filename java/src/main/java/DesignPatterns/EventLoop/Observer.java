package DesignPatterns.EventLoop;

import sun.plugin2.message.Message;

public interface Observer {
    void update(Message message);
}
