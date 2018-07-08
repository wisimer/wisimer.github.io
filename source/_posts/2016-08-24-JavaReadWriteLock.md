---
date: 2016-08-24
title: Java并发-读写锁ReadWriteLock
tags: [MultiThread,Lock]
category: JAVA
layout: default
---

读写锁是为了帮助解决多线程中读操作和写操作分离而设计的。想象一下，如果在系统中，读操作远远大于写操作，而每一次写操作进行时，别的写操作也要等待，这样对系统的性能会有很大的影响。ReadWriteLock是JDK5开始提供的读写分离锁。

<!--more-->

读写锁允许多个线程同时读，是的读线程可以真正的并行。但是考虑到数据的完整性，读操作和写操作是一定要互斥的。看一下读写锁的访问约束情况：

|     |Read   |Write|
|-----|-------|-----|
|Read |UnBlock|Block|
|Write|Block  |Block|

来看一下具体的使用例子

#### 1. 获取读写锁

通过ReentrantReadWriteLock这个类的readLock()方法和writeLock()方法分别拿到读锁和写锁

```java
private static Lock lock = new ReentrantLock(); //普通的可重入锁
private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();//读写分离锁
private static Lock readLock = readWriteLock.readLock();
private static Lock writeLock = readWriteLock.writeLock();
```

#### 2. 为读操作和写操作加锁

```java
public Object handleRead(Lock lock) throws InterruptedException {
    try {
        lock.lock();
        Thread.sleep(1000);
        System.out.println("ReadThread : "+Thread.currentThread().getId() + " | value = "+value);
        return value;
    } finally {
        lock.unlock();
    }
}

public void handleWrite(Lock lock,int value) throws InterruptedException {
    try {
        lock.lock();
        Thread.sleep(1000);
        System.out.println("WriteThread : "+Thread.currentThread().getId() + " | value = "+value);
        this.value = value;
    } finally {
        lock.unlock();
    }
}
```

#### 3. 测试代码

```java
public static void main(String[] args) {
    final ReadWriteLockTest test = new ReadWriteLockTest();
    Runnable readRunnable = new Runnable(){
        public void run() {
            try{
                test.handleRead(readLock);
                //对比使用一般的锁
                //test.handleRead(lock);
            } catch (InterruptedException e) {

            }
        }
    };

    Runnable writeRunnable = new Runnable(){
        public void run() {
            try{
                test.handleWrite(writeLock,new Random().nextInt());
                //对比使用一般的锁
                //test.handleWrite(lock,new Random().nextInt());
            } catch (InterruptedException e) {

            }
        }
    };

    new Thread(writeRunnable).start();
    for(int i = 0 ; i < 10 ; i++) {
        new Thread(readRunnable).start();
    }
    new Thread(writeRunnable).start();

}
```

运行代码之后发现使用读写锁的时候，会有多个读操作并发执行，然后等待一秒执行写操作。而使用普通的可重入锁，则不管读操作还是写操作，都要等待一秒才能继续执行。

- - -
THE END.
