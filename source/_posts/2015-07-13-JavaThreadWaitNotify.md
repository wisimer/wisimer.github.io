---
date: 2015-07-14
layout: default
category: JAVA
tags: MultiThread
title: Java线程中wait()方法以及notify()方法的使用
---

1. wait()以及notify()是Object的两个方法。Obj.wait()，与Obj.notify()必须要与synchronized(Obj)一起使用，也就是wait,与notify是针对已经获取了Obj锁进行操作。

2. 从语法角度来说就是Obj.wait(),Obj.notify必须在synchronized(Obj){...}语句块内。

3. 从功能上来说wait就是说线程在获取对象锁后，主动释放对象锁，同时本线程休眠。直到有其它线程调用对象的notify()唤醒该线程，才能继续获取对象锁，并继续执行。

4. 相应的notify()就是对对象锁的唤醒操作。

5. 有一点需要注意的是notify()调用后，并不是马上就释放对象锁的，而是在相应的synchronized(){}语句块执行结束，自动释放锁后，JVM会在wait()对象锁的线程中随机选取一线程，赋予其对象锁，唤醒线程，继续执行。这样就提供了在线程间同步、唤醒的操作。

<!--more-->

#### 来看一个简单的例子，i线程打印到2之后等待，切换到J线程打印，J线程打印完成之后再通知i线程继续打印。

```java
public class MyNotify {
    public static void main(String[] args) {
        MyThread mt1 = new MyThread();
        mt1.start();
        MyNotifyThread mt2 = new MyNotifyThread();
        mt2.start();
    }

    public static  Object o = new Object();
    static class MyThread extends Thread {
        public void run() {
            synchronized(o) {
                for(int i = 0;i<4;i++) {
                    System.out.println("i = "+i);
                    if(i == 2) {
                        System.out.println(currentThread().getName()+".wait()");
                        try {
                        	o.wait();
                        } catch(Exception e) {
                        }
                    }
                }
            }
        }
    }

    static class MyNotifyThread extends Thread {
        public void run() {
            synchronized(o) {
            	  for(int J = 0;J<6;J++) {
                    System.out.println("J = "+J);
                    if(J == 3) {
            	      	System.out.println(currentThread().getName()+".notify()");
               			  o.notify();
                    }
                }

            }
        }
    }
}
```

运行之后的结果：

```
 i = 0
 i = 1
 i = 2
 Thread-0.wait()
 J = 0
 J = 1
 J = 2
 J = 3
 Thread-1.notify()
 J = 4
 J = 5
 i = 3
```

可以看到i线程（也就是 Thread-0）打印到2之后便暂停打印，接着J线程（也就是Thread-1）开始打印，当J为3的时候，J进程调用了notify()方法，此时J线程会继续打印知道完成，最终结束之后i线程才又开始打印。也印证了开头所述的：notify()调用后，并不是马上就释放对象锁的，而是在相应的synchronized(){}语句块执行结束。

#### 再来看一个三线程顺序打印ABC的例子

```java
public class MyPrintABC {
	
  public static void main(String[] args) {
  	Object a = new Object();
  	Object b = new Object();
  	Object c = new Object();
  	PrintThread PA = new PrintThread("A",a,c);
  	PrintThread PB = new PrintThread("B",b,a);
  	PrintThread PC = new PrintThread("C",c,b); 
  	PA.start();
  	try {
  		PA.sleep(1);
  	} catch(Exception e) {}
  	PB.start();
  	try {
  		PB.sleep(1);
  	} catch(Exception e) {}
  	PC.start();
    try {
  		PC.sleep(1);
  	} catch(Exception e) {}
  }
    
  static class PrintThread extends Thread {
    
    private String mName = null;
    private Object mSelf = null;
    private Object mPre = null;
    public PrintThread(String name,Object self,Object pre) {
    	this.mName = name;
    	this.mSelf = self;
    	this.mPre = pre;
    }	
   
    public void run() {
    	int i = 0;
    	while(i < 10) {
    		try {
    	    synchronized(mPre) {
    		    synchronized(mSelf) {
    		      System.out.println(currentThread().getName()+"-->"+mName);
    		      i++;
    		      mSelf.notify();   		     		    
    		    }
    		    mPre.wait(); 
    	    } 
    	  } catch(Exception e) {}    		
    	}
    
    }
  }
}
```

这里一个PrintThread有两个对象锁mSelf和mPre。为了保证打印顺序，一个进程首先要获取mPre对象锁，也就是要等待前一个进程打印完成，释放它对应的自身对象锁。
接着再去获取当前线程自己的mSelf对象锁，同时获取之后，再打印当前进程的名（这里用mName属性表示）。
打印完成之后，调用mSelf.notify()释放自身对象锁，唤醒下一个等待进程。接着调用mPre.wait()释放mPre对象锁，终止当前线程，等待再次被唤醒。

最终打印出来的效果就是：

```java
Thread-0-->A
Thread-1-->B
Thread-2-->C
Thread-0-->A
Thread-1-->B
Thread-2-->C
Thread-0-->A
Thread-1-->B
Thread-2-->C
Thread-0-->A
Thread-1-->B
Thread-2-->C
Thread-0-->A
Thread-1-->B
Thread-2-->C
Thread-0-->A
Thread-1-->B
Thread-2-->C
Thread-0-->A
Thread-1-->B
Thread-2-->C
Thread-0-->A
Thread-1-->B
Thread-2-->C
Thread-0-->A
Thread-1-->B
Thread-2-->C
Thread-0-->A
Thread-1-->B
Thread-2-->C
```

- - -

THE END.