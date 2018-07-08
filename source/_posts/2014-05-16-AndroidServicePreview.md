---
date: 2014-05-16
title: Service初探
layout: default
category: ANDROID
---

可以说service能够使应用程序更有"内涵"，而不仅仅是在几个Activity之间来回跳转。service看起来比Activity要复杂一点，其实仔细研究一下还是值得的。

## startService方法启动service

#### 实现要素:

1.继承Service实现一个子类MyService,并在`AndroidManifest.xml`中注册

2.intent.setClass(this,MyService.class);

3.startService(intent);

<!--more-->

- - -

## bindService方式启动service,并与Activity进行数据交互

#### 实现要素:

1.同样继承Service实现一个子类MyService,并在MyService中定义一个MyBinder继承自Binder。
  >然后在MyService中实例化一个MyBinder对象myBinder。

另外，可以在MyBinder中自定义方法getMessage()用于向Activity返回数据
```java
public String getMessage(){
  return "I am from Service.Binder";
}
```

2.在MyService中重写onBind方法并返回上述的myBinder对象

```java
public IBinder onBind(Intent intent) {
  return mBinder;
}
```

3.在activity中实例化一个ServiceConnection对象mServiceConnection。并且在它的`onServiceConnected`方法中实现Activty与Service的数据交互：

```java
public void onServiceConnected(ComponentName name, IBinder service) {
    myBinder=(MyService.MyBinder)service;
    mSring=myBinder.getMessage();
}
```

4.要在`AndroidManifest.xml`文件中注册Service:

    <service android:name="com.wxp.service.MyService"></servce>

5.启动service

```java
Intent intent=new Intent(this.MyService.class);
bindService(mIntent,mServiceConnection,Context.BIND_AUTO_CREATE);
```

- - -

## 向Service传递数据

#### 例如，我们在一个BroadcastReceiver中想要向Service传递数据:

```java
public void onReceive(Context context,Intent intent) {
    Intent gointent = new Intent(context, MyService.class);
    gointent.putExtra("info", true);
    context.startService(gointent);
}
```

#### 因为通过startService方法启动service，只有第一次会执行onCreate方法，如果service已经存在，以后只会重新执行onStart方法，所以这里要重写Service的onStart方法：

```java
 public void onStart(Intent intent, int startId) {
    screenOn = intent.getBooleanExtra("info", false);
 }
```

这样每次接受到广播之后都会向Service中发送数据了

- - -

## 创建一个带有通知栏提醒的service

在MyService的onCreate方法中加入

```java
Notification notification = new Notification(R.drawable.ic_launcher,   "有通知到来", System.currentTimeMillis());
    Intent notificationIntent = new Intent(this, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
    notification.setLatestEventInfo(this, "这是通知的标题", "这是通知的内容",pendingIntent);
    startForeground(1, notification);
```
这样每次启动service时通知栏都会出现提醒。

>- 注意:如果是startService启动，则stopService会关闭通知栏提醒
 - 如果是bindService启动，则unbindService会关闭通知栏提醒

值得注意的是
>service是运行在主线程，但是可以在onStartCommand()方法中开启一个新的线程执行耗时任务。也可以在MyBinder中定义的startDownload()方法中开启新线程。

- - -
- - -

## AIDL简单实现 ##

#### 服务端 ####

1.在com.wxp.aidl下新建一个aidl文件ServerAidl.aidl，注意aidl中定义的接口不能加访问权限修饰符。

```java
interface ServerAidl {
    String getMessage();//自定义的方法
}
```

2.在com.wxp.aidl包下创建一个新的服务MyService.java，在MyService内部定义一个MyAidl类继承ServerAidl.Stub，并实现ServerAidl接口中定义的getMessage方法。

```java
pubic class MyAidl extends ServerAidl.Stub {
    public String getMessage()
        {return "i am from service";}
}
```

3.重写MyService的onBind方法，在onBind方法中返回实现一个myAidl对象

```java
public IBinder onBind(Intent intent) {
    return new MyAidl();
}
```

4.在Manifest.xml中注册MyService

```java
<service android:name="com.wxp.aidl.ServerAidl">
     <intent-filter>
        <action  android:name="com.wxp.aidl.MyService" />
        <category android:name="android.intent.category.DEFAULT" />
      </intent-filter>
</service>
```

------------------------------------------------------------------------------------------------------------------------

#### 客户端 ####

1.首先要新建一个和服务器端一样的包名，即com.wxp.adil。并且将服务器端的ServerAidl.aidl文件同时复制过来。不过我实验了一下，好像不这样做也还是可以实现的啊。

2.在Activity中实例化一个ServiceConnection对象mServiceConnection,并重写它的onServiceConnected方法。

```java
ServiceConnection mServiceConnection=new ServiceConnection(){
    public void onServiceConnected(ComponentName comName,IBinder ibinder){
       mIBinder=ServerAidl.Stub.asInterface(ibinder);
    }
    public void onServiceDisconnected(ComponentName name) {
        mIBinder=null;
    }
}
```

3.接着就可以通过bindService（intent,mServiceConnection.Context.BIND_AUTO_CREATE);方法启动服务了。

>值得注意的是，不能直接在bindService之后立刻调用mIBinder.getMessage()。

- - -
THE END.
