---
date: 2017-03-30
title: Android Service相关问题
category: ANDROID
layout: default
---

#### 一、Service里面的 `onStartCommand()` 方法详解

通过 `startService()` 多次启动service的时候，onCreate方法只有第一次会调用，onStartCommand和onStart每次都被调用。onStartCommand会告诉系统如何重启服务， 比如会判断是否异常终止后重新启动，以及在何种情况下异常终止Service。

<!--more-->

#### 二、Service两种启动方式的生命周期对比

![Service Lifecycle](/src/imgs/1703/03_ServiceLifecycle.png)

启动服务时依次执行onCreate，onStartCommand；如果在系统显示调用stopService和stopSelf之前终止服务，service再次重启，onStartCommand会被调用。

#### 三、onStartCommand和onStart区别

```java
// This is the old onStart method that will be called on the pre-2.0
// platform. On 2.0 or later we override onStartCommand() so this
// method will not be called.
// 2.0 API level之后，实现onStart等同于重写onStartCommand并返回START_STICKY
@Override
public void onStart(Intent intent, int startId) {
handleCommand(intent);
}

// 2.0 API level之后，onStart()方法被onStartCommand()取代了
@Override
public int onStartCommand(Intent intent, int flags, int startId) {
handleCommand(intent);
// We want this service to continue running until it is explicitly
// stopped, so return sticky.
return START_STICKY;
} 
```

#### 四、onStartCommand返回值

onStartComand使用时，返回的是一个int值。这个整型可以有四个返回值：

1.START_STICKY
> 如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。随后系统会尝试重新创建service，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null。

2.START_NOT_STICKY
> “非粘性的”。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务

3.START_REDELIVER_INTENT
> 重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。 

4.START_STICKY_COMPATIBILITY。
> START_STICKY的兼容版本，但不保证服务被kill后一定能重启。

#### 五、onStartComand参数flags含义

flags表示启动服务的方式：

> Additional data about this start request. Currently either 0, START_FLAG_REDELIVERY, or START_FLAG_RETRY.

1. `START_FLAG_REDELIVERY` ：如果你实现onStartCommand()来安排异步工作或者在另一个线程中工作, 那么你可能需要使用START_FLAG_REDELIVERY 来让系统重新发送一个intent。这样如果你的服务在处理它的时候被Kill掉, Intent不会丢失.

2. `START_FLAG_RETRY`：表示服务之前被设为START_STICKY，则会被传入这个标记。 

#### 六、Service和线程的区别

我们都知道普通的Service是运行在主线程里的，也就是说如果你在Service里编写了非常耗时的代码，程序必定会出现ANR的。

那我要Service又有何用呢？其实大家不要把后台和子线程联系在一起就行了，这是两个完全不同的概念。Android的后台就是指，它的运行是完全不依赖UI的。即使Activity被销毁，或者程序被关闭，只要进程还在，Service就可以继续运行。比如说一些应用程序，始终需要与服务器之间始终保持着心跳连接，就可以使用Service来实现。你可能又会问，前面不是刚刚验证过Service是运行在主线程里的么？在这里一直执行着心跳连接，难道就不会阻塞主线程的运行吗？当然会，但是我们可以在Service中再创建一个子线程，然后在这里去处理耗时逻辑就没问题了。

那既然在Service里也要创建一个子线程，那为什么不直接在Activity里创建呢？这是因为Activity很难对Thread进行控制，当Activity被销毁之后，就没有任何其它的办法可以再重新获取到之前创建的子线程的实例。而且在一个Activity中创建的子线程，另一个Activity无法对其进行操作。但是Service就不同了，所有的Activity都可以与Service进行关联，然后可以很方便地操作其中的方法，即使Activity被销毁了，之后只要重新与Service建立关联，就又能够获取到原有的Service中Binder的实例。因此，使用Service来处理后台任务，Activity就可以放心地finish，完全不需要担心无法对后台任务进行控制的情况。


- - -
THE END.