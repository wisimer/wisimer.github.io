---
date: 2015-06-24
layout: default
category: JAVA
tags: MultiThread
title: Java线程中yield()方法的使用
---

> yield()方法的作用是：暂停当前正在执行的线程对象，并执行其他线程。注意：这里的其他也包含当前线程。

<!--more-->

#### 1.首先继承Thread实现一个子类YieldTest，在它的run方法中执行yield()方法。

```java
class YieldTest extends Thread {

  public void run() {
    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
    System.out.println("YieldTest-run-begin");
    System.out.println("YieldTest-1"); 
    YieldAndSleep.calculate();
    yield();
    System.out.println("YieldTest-2");
    YieldAndSleep.calculate(); 
    System.out.println("YieldTest-run-end");
  }
}
```

#### 2.然后继承Thread实现一个子类SleepTest，在它的run方法中执行sleep方法，用来对比yield方法。

```java
class SleepTest extends Thread {
  
  public void run() {
  Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
    System.out.println("SleepTest-run-begin");
    System.out.println("SleepTest-1"); 
    YieldAndSleep.calculate();
    try{  
        sleep(1000);
    } catch (Exception e) {}  
    System.out.println("SleepTest-2"); 
    YieldAndSleep.calculate();  
    System.out.println("SleepTest-run-end");
  }   
}  

```

#### 3.最后分别启动两个YieldTest线程和两个SleepTest线程，观察它们的运行状况

```java
public class YieldAndSleep {

  public static void calculate() {
      for (int i = 0;i<100000000;i++) {
       int j = i*i;
      }
  }
  public static void main(String[] args) {   
    for (int i = 1; i <= 2; i++) { 
      System.out.println("YieldTest().start()-begin");  
      new YieldTest().start(); 
      System.out.println("YieldTest().start()-end");  
    }   
    
    for (int i = 1; i <= 2; i++) { 
      System.out.println("SleepTest().start()-begin");  
      new SleepTest().start(); 
      System.out.println("SleepTest().start()-end");  
    }  
  }  
}
```

#### 4.看一下运行的结果

```
YieldTest().start()-begin
YieldTest().start()-end
YieldTest().start()-begin
YieldTest-run-begin
YieldTest-1
YieldTest().start()-end
SleepTest().start()-begin
YieldTest-run-begin
YieldTest-1
SleepTest().start()-end
SleepTest().start()-begin
SleepTest().start()-end
SleepTest-run-begin
SleepTest-1 
SleepTest-run-begin
SleepTest-1
YieldTest-2
YieldTest-run-end
YieldTest-2
YieldTest-run-end
SleepTest-2
SleepTest-run-end
SleepTest-2
SleepTest-run-end
```

![0624_yield.gif](/src/imgs/1506/0624_yield.gif)

每次的运行结果可能都不太一样，就拿这次的来分析一下。可以看到第14行输出`SleepTest-1 `,它是在`YieldTest-1`之后打印出来的，就是因为在`System.out.println("YieldTest-1"); `之后执行了`yield(); `方法，所以从YieldTest线程转换到了SleepTest线程。当然也有可能还是转换到YieldTest自己这个线程。

- - -
THE END.