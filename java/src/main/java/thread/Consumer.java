//package thread;
//
//import sun.misc.Queue;
//
///**
// * @author Congpeixin
// * @version 1.0
// * @date 2021/6/26 4:52 下午
// * @describe
// */
//public class Consumer<T> {
//    private Queue<T> tasks;
//
//    public Consumer(Queue<T> tasks) {
//        this.tasks = tasks;
//    }
//
//    public T consume(){
//        synchronized (tasks){
//            while (tasks.size() == 0){
//                System.out.println("消费者线程进入等待： " + Thread.currentThread().getName());
//                tasks.wait();
//            }
//
//            T res = tasks.poll();
//            return res;
//        }
//    }
//}
