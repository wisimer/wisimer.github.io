---
date: 2015-04-11
layout: default
category: ANDROID
title: 由BroadcastReceiver所引发的事件处理的一些想法
---

有些情况下，一旦接受到广播，需要在Activity或者Fragment中处理相关业务逻辑。将广播接收者作为内部类可以很方便的解决这个问题。
但是如果，广播接受者作为一个独立的外部类。如何实现这种需求呢。

<!--more-->

> 简单来说就是在广播接受者里面新建一个回调接口，在Activity中实现这个接口。

#### 一、创建一个广播接受者，并在AndroidManifest.xml中注册

```java
public class MyReceiver extends BroadcastReceiver {

    static ExecuteCallback mCallback;
    public void setCallback(ExecuteCallback callback) {
        mCallback = callback;
        Log.e("wxp","setCallback");
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("myreceiver")) {
            String msg = intent.getStringExtra("msg");
            if (msg == null) msg = "HELLO";
            if (mCallback != null) {
                //一旦接受到广播，就执行这个回调方法
                mCallback.execute(msg);
            } else {
                Log.e("wxp","mCallback = null");
            }

        }
    }
}

interface ExecuteCallback{
    public void execute(String msg);
}
```

#### 二、在Fragment中实现ExecuteCallback接口，重写它的execute方法

```java
public class MyBroadcastReceiverEventFragment extends Fragment implements ExecuteCallback{

    ......
    @Override
    public void execute(String msg) {
        mTextview.setText(msg);
    }
    ......
}
```

这样一旦接受到广播，Fragment中的mTextView就会更新内容了。

- - -
THE END.
