
import java.util.Queue;
import java.util.LinkedList;
public class MyProducerConsumer {

    public static void main(String[] args) {
        int max = 20;
        Queue<String> queue = new LinkedList<String>();
        MyProducer<String> p = new MyProducer<String>(queue,max,"Producer");
        p.setGoods("hello world");
        MyConsumer<String> c1 = new MyConsumer<String>(queue,max,"Consumer1");
        //MyConsumer<String> c2 = new MyConsumer<String>(queue,max,"Consumer2");
        p.start();
        c1.start();
        //c2.start();
    }

}

class MyProducer<T>  extends Thread {
    private Queue<T> mQueue;
    private int MAX = 0;
    private String mName = null;
    private T mGoods = null;
    public MyProducer(Queue<T> queue, int max,String name) {
        super(name);
        this.mQueue = queue;
        this.MAX = max;
        this.mName = name;
    }

    public void setGoods(T goods) {
        this.mGoods = goods;
    }

    public void run() {
        while(true) {
            synchronized(mQueue) {
                while(mQueue.size() == MAX) {
                    try {
                        mQueue.wait();
                    } catch (Exception e) {

                    }

                }

                int time = (int)System.currentTimeMillis();
                System.out.println(time+ " | "+mName+" --> queue.size() : " + mQueue.size() + " | Add : "+mGoods);
                mQueue.add(mGoods);
                try {
                    sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mQueue.notify();
            }
        }

    }
}

class MyConsumer <T>  extends Thread {
    private Queue<T> mQueue;
    private int MAX = 0;
    private String mName = null;
    public MyConsumer(Queue<T> queue , int max,String name) {
        super(name);
        this.mQueue = queue;
        this.MAX = max;
        this.mName = name;
    }
    public void run() {
        while(true) {
            synchronized(mQueue) {
                if(mQueue.isEmpty()) {
                    try {
                        mQueue.wait();
                    } catch (Exception e) {

                    }
                }
                int time = (int)System.currentTimeMillis();
                System.out.println(time+" | "+mName+" --> queue.size() : " + mQueue.size()+" | mQueue.remove() : "+mQueue.remove());

                try {
                    sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mQueue.notify();
            }
        }

    }
}
