---
date: 2015-08-10
layout: default
category: JAVA
tags: MultiThread
title: Java使用wait()以及notifyAll()实现生产者消费者模型
---

之前已经简单介绍过wait()以及notifyAll()的使用，下面通过它们来实现进程间通信，从而解决生产者消费者问题。

<!--more-->

#### 一、首先定义一个生产者线程

生产者负责生产商品，每次生产一个商品之后都会通知消费者可以来取商品了，如果保存商品的队列满了则停止生产，等待消费者来取走商品。

```java
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
```

run()方法中使用一个while(true)循环让生产者一直运行。如果队列满了则执行`mQueue.wait();`，否则执行`mQueue.add(mGoods);`。

<!--值得注意的是在synchronized(mQueue)里面的while(mQueue.size() == MAX)循环，我们是要判断当前队列有没有满，为什么不使用if语句来判断呢？
但if语句存在一些微妙的小问题，导致即使条件没被满足，你的线程你也有可能被错误地唤醒。所以如果你不在线程被唤醒后再次使用while循环检查唤醒条件是否被满足，你的程序就有可能会出错-->

#### 二、接着定义一个消费者线程

消费者负责消费商品，每次消费一个商品之后都会通知生产者，如果保存商品的队列空了，则等待生产者生产商品。

```java
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
```

run()方法中使用一个while(true)循环让消费者一直运行。如果队列空了则执行`mQueue.wait();`，否则执行`mQueue.remove();`。

#### 三、最后来测试一下

```java
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
```

每次都向商品队列中添加"hello world"字符串。也可以同时启动两个消费者线程。

看一下运行结果

```
$ java MyProducerConsumer
397223348 | Producer --> queue.size() : 0 | Add : hello world
397223448 | Producer --> queue.size() : 1 | Add : hello world
397223548 | Producer --> queue.size() : 2 | Add : hello world
397223648 | Producer --> queue.size() : 3 | Add : hello world
397223748 | Producer --> queue.size() : 4 | Add : hello world
397223848 | Producer --> queue.size() : 5 | Add : hello world
397223948 | Consumer1 --> queue.size() : 6 | mQueue.remove() : hello world
397224048 | Producer --> queue.size() : 5 | Add : hello world
397224148 | Producer --> queue.size() : 6 | Add : hello world
397224248 | Producer --> queue.size() : 7 | Add : hello world
397224348 | Producer --> queue.size() : 8 | Add : hello world
397224448 | Producer --> queue.size() : 9 | Add : hello world
397224548 | Producer --> queue.size() : 10 | Add : hello world
397224648 | Producer --> queue.size() : 11 | Add : hello world
397224748 | Producer --> queue.size() : 12 | Add : hello world
397224848 | Producer --> queue.size() : 13 | Add : hello world
397224948 | Producer --> queue.size() : 14 | Add : hello world
397225048 | Producer --> queue.size() : 15 | Add : hello world
397225148 | Producer --> queue.size() : 16 | Add : hello world
397225248 | Producer --> queue.size() : 17 | Add : hello world
397225348 | Producer --> queue.size() : 18 | Add : hello world
397225448 | Producer --> queue.size() : 19 | Add : hello world
397225548 | Consumer1 --> queue.size() : 20 | mQueue.remove() : hello world
397225648 | Consumer1 --> queue.size() : 19 | mQueue.remove() : hello world
397225748 | Consumer1 --> queue.size() : 18 | mQueue.remove() : hello world
397225848 | Consumer1 --> queue.size() : 17 | mQueue.remove() : hello world
397225948 | Consumer1 --> queue.size() : 16 | mQueue.remove() : hello world
397226048 | Producer --> queue.size() : 15 | Add : hello world
397226148 | Producer --> queue.size() : 16 | Add : hello world
397226248 | Producer --> queue.size() : 17 | Add : hello world
397226348 | Producer --> queue.size() : 18 | Add : hello world
397226448 | Producer --> queue.size() : 19 | Add : hello world
397226548 | Consumer1 --> queue.size() : 20 | mQueue.remove() : hello world
397226648 | Consumer1 --> queue.size() : 19 | mQueue.remove() : hello world
397226748 | Consumer1 --> queue.size() : 18 | mQueue.remove() : hello world
......
```


- - -
THE END.
