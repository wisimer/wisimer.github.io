---
date: 2016-08-24
title: Java并发-CyclicBarrier
tags: MultiThread
category: JAVA
layout: default
---

CyclicBarrier是一种多线程并发控制实用工具。可以实现线程间的计数等待。CyclicBarrier翻译过来就是循环栅栏，可以用来阻止线程继续执行，要求线程在栅栏处等待。

<!--more-->

比如我们这里实现一个场景，有十个士兵要先集合再去执行任务。首先是士兵报道，所有人到齐之后才能去执行任务，然后每个人完成任务也要报告，所有人完成任务之后才能表示这个任务结束。

#### 1.看看如何创建一个CyclicBarrier

```java
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class CyclicBarrierTest {
    public static void main(String[] args) {
        final int COUNT = 10;
        Thread[] soliders = new Thread[COUNT];
        boolean flag = false;
        CyclicBarrier cb = new CyclicBarrier(COUNT,new BarrierWork(flag,COUNT));
        System.out.println("start");

        ExecutorService exec = Executors.newFixedThreadPool(COUNT);
        for(int i = 0 ; i < COUNT ; i++) {
            try {
                Thread.sleep(300);
            } catch (Exception e) {

            }
            System.out.println(i + " : hasArrive");
            exec.submit(new Solider(cb,i));
        }
        exec.shutdown();
    }
}
```

这里定义COUNT为10，实例化CyclicBarrier的时候传入COUNT作为参数，还有对应的任务线程BarrierWork，BarrierWork就是在设置了栅栏之后，每次执行工作完线程到达了栅栏之后所要执行的操作。

然后通过线程池启动十个士兵工作线程，我们这里用Thread.sleep(300);来模拟每个士兵到达的时间不同，每个士兵到达都会执行 `System.out.println(i + " : hasArrive");`。

#### 2.再看看BarrierWork这个Runnable

```java
public static class BarrierWork implements Runnable {

    //总数
    public int count;
    //报道和任务flag
    public boolean workFlag;

    public BarrierWork(boolean workFlag,int count) {
        this.count = count;
        this.workFlag = workFlag;
    }
    public void run() {
        if (workFlag) {
            System.out.println(count + " : allFinishWork");
        } else {
            System.out.println(count + " : allArrive");
            workFlag = true;
        }
    }
}
```

当workFlag为false时表示士兵刚来报道，还没执行任务。由于CyclicBarrier已经设置了计数器为10，每个士兵报道的时候，CyclicBarrier内部都会更新计数器，直到10个士兵全都报道完毕的时候都会执行`System.out.println(count + " : allArrive");`，

然后把`workFlag`更新为true，表示报道之后就要等待完成工作了。等待10个士兵完成任务的原理也和报道是一样的。


#### 3.还有Solider这个Runnable

```java
public static class Solider implements Runnable {
    public int name;
    public CyclicBarrier cb;

    public Solider(CyclicBarrier cb,int name) {
        this.name = name;
        this.cb = cb;
    }

    public void run() {
        try {
            cb.await();
            doWork();
            cb.await();
        } catch(Exception e) {

        }
    }

    public void doWork() {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }
        System.out.println(name + " : hasFinish");
    }
}
```

看看run方法里面， CyclicBarrier 的await()方法就是用来设置计数的，第一次执行cb.await()是设置栅栏，让士兵报道之后等待，直到CyclicBarrier的计数器达到10，再去执行BarrierWork，此时BarrierWork里面肯定是打印`allArrive`的，然后士兵们再执行doWork()。
第二次执行cb.await()是设置栅栏，让每个士兵完成任务后等待，直到计数器达到10，再去执行BarrierWork，此时就变成打印`allFinished`了。

看看最终的运行结果：

```
λ javac CyclicBarrierTest
start
0 : hasArrive
1 : hasArrive
2 : hasArrive
3 : hasArrive
4 : hasArrive
5 : hasArrive
6 : hasArrive
7 : hasArrive
8 : hasArrive
9 : hasArrive
10 : allArrive
1 : hasFinish
9 : hasFinish
5 : hasFinish
7 : hasFinish
8 : hasFinish
3 : hasFinish
4 : hasFinish
6 : hasFinish
0 : hasFinish
2 : hasFinish
10 : allFinishWork
```

- - -
THE END.
