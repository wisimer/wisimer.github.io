---
date: 2016-10-25
title: Java多线程-ThreadLocal的使用
tags: MultiThread
category: JAVA
layout: default
---

ThreadLocal的设计不是为了解决多线程之间的数据共享问题，而是为了实现了线程间的数据隔离。ThreadLocal 很残暴的在每个线程内部自己维护一个变量，你别的线程的数据我不要，我只管我自己的数据。
这样的结果是降低了线程的同步的复杂度，但是内存使用也就上去了。是一种“以控件换空间”的方式。而已 "synchronized" 为代表的一系列线程同步方式，则是“以时间换空间”的方式。那就来看看ThreadLocal要怎么使用吧。

<!--more-->

### 一、ThreadLocal 的简单使用

#### 1. 首先创建一个简单的Student类吧

```java
public static class Student {  
    private int age = 0;

    public int getAge() {  
        return this.age;  
    }  

    public void setAge(int age) {  
        this.age = age;  
    }  
}
```

#### 2. 再来一个 ThreadLocalDemo

```java
public class ThreadLocalDemo implements Runnable {
    //创建线程局部变量studentLocal用来保存Student对象
    private final static ThreadLocal studentLocal = new ThreadLocal();
    //对比实现，直接保存到对象里
    public Student stu = new Student();

    public void run() {
        accessStudent();
    }

    public void accessStudent() {
        //获取当前线程的名字
        String currentThreadName = Thread.currentThread().getName();
        System.out.println(currentThreadName + " is running!");
        //产生一个随机数并打印
        Random random = new Random();
        int age = random.nextInt(100);
        System.out.println("thread " + currentThreadName + " set age to:" + age);
        //获取一个Student对象，并将随机数年龄插入到对象属性中
        Student student = getStudent();//getThreadLocalStudent
        student.setAge(age);
        System.out.println("thread " + currentThreadName + " first read age is:" + student.getAge());
        System.out.println("thread " + currentThreadName + " second read age is:" + student.getAge());
    }

    protected Student getThreadLocalStudent() {
        //获取本地线程变量并强制转换为Student类型
        Student student = (Student) studentLocal.get();
        //线程首次执行此方法的时候，studentLocal.get()肯定为null
        if (student == null) {
            //创建一个Student对象，并保存到本地线程变量studentLocal中
            student = new Student();
            studentLocal.set(student);
        }
        return student;
    }

    protected Student getStudent() {
        return stu;
    }

    protected synchronized Student getStudentSynchronized() {
        return stu;
    }
}
```

#### 3. 来测试一下吧

同时运行两个 ThreadLocalDemo 线程。

```java
public static void main(String[] agrs) {
    ThreadLocalDemo td = new ThreadLocalDemo();
    Thread t1 = new Thread(td, "a");
    Thread t2 = new Thread(td, "b");
    t1.start();
    t2.start();
}
```

（1）首先试一下直接存在 Student 对象里，直接调用 getStudent 方法

```
b is running!
a is running!
thread b set age to:64
thread a set age to:63
thread b first read age is:64
thread a first read age is:63
thread a second read age is:63
thread b second read age is:63
```

可以看到直接使用 stu 变量的话，线程 b 第二次读取的时候其实是读取到了线程 a 设置之后的值。

（2）再看看放在 ThreadLocal 里面，换成调用 getThreadLocalStudent 方法

```
a is running!
b is running!
thread a set age to:13
thread b set age to:90
thread b first read age is:90
thread a first read age is:13
thread b second read age is:90
thread a second read age is:13
```

a，b 两个线程两次都只是各自读取自己的局部变量。

- - -

课外思考，顺便来实现一下使用 synchronized 实现线程 ab 数据共享

```java
public void run() {
    synchronized(stu) {
        accessStudent();
    }
}
```

只要修改一下 run 方法里面，就可以了。这样打印出来的是：

```
a is running!
thread a set age to:35
thread a first read age is:35
thread a second read age is:35
b is running!
thread b set age to:94
thread b first read age is:94
thread b second read age is:94
```

此处我测试了一下 `synchronized(stu)` 只能加在 accessStudent 方法外部，在 accessStudent 方法里面加同步是不生效的。

- - -

### 二、ThreadLocal 的实现原理

上面看了 ThreadLocal 如何使用，下面干脆一起来看看它内部是如何实现的吧。

#### 1. ThreadLocal 的 set 方法

```java
public void set(T value) {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
}
```

首先可以看到这里有个 ThreadLocalMap 类，它是 ThreadLocal 的一个内部类， ThreadLocalMap一听这名字，就感觉是用来保存 ThreadLocal 的一个东西，它的内部呢又有一个 Entry 类，这是一个(key,value)形式的弱引用，为什么要把key作为弱引用呢？是为了让 ThreadLocalMap 尽可能的小，在 key 不被引用的时候就回收：

```java
static class Entry extends WeakReference<ThreadLocal<?>> {
    Object value;
    Entry(ThreadLocal<?> k, Object v) {
        super(k);
        value = v;
    }
}
```

再回到上面的 set 方法，首先是通过 getMap(t) 方法拿到当前线程的 ThreadLocalMap 对象，Thread 类里面就有一个名为 threadLocals 的 ThreadLocalMap 对象。

```java
ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}
```

（1）如果为空，则去创建:

```java
void createMap(Thread t, T firstValue) {
    t.threadLocals = new ThreadLocalMap(this, firstValue);
}
```
把新的 ThreadLocalMap 对象赋值给线程t的 threadLocals 对象。

看看 ThreadLocalMap 的构造函数：

```java
ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
    table = new Entry[INITIAL_CAPACITY];
    int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
    table[i] = new Entry(firstKey, firstValue);
    size = 1;
    setThreshold(INITIAL_CAPACITY);
}
```

这里的 table 就是一个 Entry 数组 。按照上面的 `Entry(ThreadLocal<?> k, Object v)` 构造函数，初始化 ThreadLocalMap 对象的时候就会把 ThreadLocal 对象作为key，把value放到一个 Entry 对象中，并且保存到 table 数组中。

（2）如果这个对象不为空，则直接调用 ThreadLocalMap 的 set方法 ：

```java
private void set(ThreadLocal<?> key, Object value) {

    Entry[] tab = table;
    int len = tab.length;
    int i = key.threadLocalHashCode & (len-1);

    for (Entry e = tab[i]; e != null; e = tab[i = nextIndex(i, len)]) {
        ThreadLocal<?> k = e.get();
        if (k == key) {
            e.value = value;
            return;
        }

        if (k == null) {
            replaceStaleEntry(key, value, i);
            return;
        }
    }

    tab[i] = new Entry(key, value);
    int sz = ++size;
    if (!cleanSomeSlots(i, sz) && sz >= threshold)
        rehash();
}
```

遍历 table 数组，如果当前的 ThreadLocal 对象和其中已经保存的某个 Entry 的 key 相等，则把这个 Entry 的value设置为传进来的值。如果发现key为空（是的，会出现k是null的情况），所以会接着在replaceStaleEntry重新循环寻找相同的key，找到之后再赋值。所以说我们通过 ThreadLocal 传进来的值，其实都保存在了 ThreadLocalMap 的 Entry对象里了。

#### 2. ThreadLocal 的 get 方法

```java
public T get() {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
    return setInitialValue();
}
```
就是在Entry中通过当前 ThreadLocal 对象索引到它对应的值。

getMap 方法上面已经看过了。看看 ThreadLocalMap 的 getEntry 方法：

```java
private Entry getEntry(ThreadLocal<?> key) {
    int i = key.threadLocalHashCode & (table.length - 1);
    Entry e = table[i];
    if (e != null && e.get() == key)
        return e;
    else
        return getEntryAfterMiss(key, i, e);
}
```

很简单，还是遍历 table 数组。

- - -
THE END.
