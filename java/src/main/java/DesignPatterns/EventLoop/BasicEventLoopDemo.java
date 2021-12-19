package DesignPatterns.EventLoop;

import sun.plugin2.message.Message;

public class BasicEventLoopDemo {
    public static void main(String[] args){
        ConcreteSubject subject = new ConcreteSubject();
        subject.registerObserver(new ConcreteObserverOne());
        subject.registerObserver(new ConcreteObserverTwo());

//        subject.notifyObservers(new Message());
    }
}
