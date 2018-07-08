---
date: 2016-10-22
title: Java多线程-static synchronized 和 synchronized 修饰的方法的区别
tags: MultiThread
category: JAVA
layout: default
---

一晃眼居然两个多月没写博客了，那这两个月究竟忙什么了呢。仔细想想好像也没干啥。大概是忙着灌药了吧。唉，以后还是多写点博客吧，不然感觉虚度光阴了。🙏

<!--more-->

我们都知道 `staitc` 修饰的方法是静态方法，是属于类的，可以通过类名直接调用。那么题中所述的区别就显而易见了：`static synchronized` 关键字修饰的方法是为类加锁，只要是这个类的对象，调用到这个方法都要加锁。而 `synchronized` 修饰的方法则是对象级别的，只有同一个对象在多个线程访问这个方法才会加锁。下面通过一个简单的例子来说明一下两种修饰的方法在多线程中会对别的方法产生什么影响。

### 1.来吧，首先是一个用来测试 synchronized 方法的类 SynchronizedRun ：

它有两个方法：方法a和方法b。并且在它的 run 方法中会随机调用a和b中的一个方法。

```java
public static class SynchronizedRun implements Runnable {
    public void run () {
        int ran = new Random().nextInt(2);
        if(ran == 0) {
            a();
        }
        if(ran == 1) {
            b();
        }
    }

    public synchronized void a() {

        System.out.println("A");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

    }

    public synchronized void b(){
        System.out.println("B");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }

    }
}
```

### 2.再来一个，测试 static synchronized 方法的类 StaticSynchronizedRun

它有两个方法：c 和 d。并且在它的run方法中会随机调用c 和 d中的一个方法。

```java
public static class StaticSynchronizedRun implements Runnable {
    public void run () {
        int ran = new Random().nextInt(2);
        if(ran == 0) {
            c();
        }
        if(ran == 1) {
            d();
        }
    }

    public static synchronized void c(){
        System.out.println("C");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }

    }

    public static synchronized void d() {
        System.out.println("D");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

    }
}
```

### 3.来写一段测试代码吧

```java
public class StaticSynchronized {
    public static void main(String[] args) {

        System.out.println("*********** 一个对象的 synchronized 方法 ***********");
        SynchronizedRun synchronizedRun = new SynchronizedRun();
        for (int i = 0 ; i < 4 ; i++) {
            new Thread(synchronizedRun).start();
        }

        /*
        System.out.println("*********** 多个对象的 synchronized 方法 ***********");
        for (int i = 0 ; i < 4 ; i++) {
            new Thread(new SynchronizedRun()).start();
        }


        System.out.println("*********** 一个对象的 static synchronized 方法 ***********");
        StaticSynchronizedRun staticSynchronizedRun = new StaticSynchronizedRun();
        for (int i = 0 ; i < 4 ; i++) {
            new Thread(staticSynchronizedRun).start();
        }

        System.out.println("*********** 多个对象的 static synchronized 方法***********");
        for (int i = 0 ; i < 4 ; i++) {
            new Thread(new StaticSynchronizedRun()).start();
        }
        */

    }
}
```

- 测试1: 一个对象在不同线程调用它的两个 `synchronized` 方法。

![1.gif](/src/imgs/1610/1.gif)

可以看到这里a和b两个方法显然是加锁了的。因为 `synchronized` 修饰的方法是对象级的，这里只有一个对象，访问ab方法时肯定需要等待了。

- 测试2: 不同的对象在多个线程中调用他们的 `synchronized` 方法。

![2.gif](/src/imgs/1610/2.gif)

2这里和上面1的现象对比一下，很明显，几个方法是同时执行的。因为是多个对象各自执行他们的 `synchronized` 方法。并不需要加锁等待。

- 测试3: 一个对象在不同线程中调用它的两个 `static synchronized` 方法。

![3.gif](/src/imgs/1610/3.gif)

3这里的现象和测试一中几乎是一样的。

- 测试4: 不同对象在多个线程中调用他们的 `static synchronized` 方法。

![4.gif](/src/imgs/1610/4.gif)

而4这里的现象和3中也是一样的。这就证明了文章开头所说的`static synchronized` 方法是类级别的，不管你有多少对象，到我这就要加锁等待。

- - -
THE END.
