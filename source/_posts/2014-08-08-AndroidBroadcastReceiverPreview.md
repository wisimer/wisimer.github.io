---
date: 2014-08-08
layout: default
title: BroadcastReceiver 的简单使用
category: ANDROID
---

和 Activity 以及 Service 一样，BroadcastReceiver同属Android的四大组件。

有时候用 BroadcastReceiver 可以轻易解决很纠结的问题。

#### 1.继承BroadcastReceiver实现一个广播接受者MyReceiver

可以重写onReceive(Context context,Intent intent)方法,在方法里面通过intent.getExtras()方法获取intenet中传递过来的数据

<!--more-->

#### 2.注册广播,有两种方式

(1).动态广播：通过context中的registerRecier方法来注册

```java
MyReceiver myReceiver=new MyReceiver();
IntentFilter intentFilter=new IntentFilter("com.wxp.receiver.MyReceiver");
registerReceiver(myReceiver,intentFilter);
```

> 对于动态广播来说，如果Context被销毁时记得要注销广播：

```java
if(myReceiver!=null) {
  unregisterReceiver(myReceiver);
}
```

(2).静态广播：在AndroidManifest.xml文件中注册

```java
<receiver android:name=".MyReceiver">
    <intent-filter>
        <action android:name="com.wxp.receiver.MyReceiver" />
    </intent-filter>
</receiver>
```

#### 3.发送广播

```java
Intent intent =new Intent();
intent.setAction("com.wxp.receiver.MyReceiver");
intent.putExtra("msg","send by broadcast");
sendBroadcast(intent);
```

- - -
THE END.
