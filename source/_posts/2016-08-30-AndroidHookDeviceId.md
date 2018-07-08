---
date: 2016-08-30
title: Android Hook getDeviceId方法篡改设备id
category: ANDROID
layout: default
---

学习一下如何使用动态代理来Hook Android中Java层的方法，这里拿TelephoneManager的getDeviceId方法来做练手，先来看看如何使用TelephoneManager获取deviceId:

```java
TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
tm.getDeviceId()
```

<!--more-->

### 一、首先来看看 TelephoneManager.getDeviceId()方法的实现：

```java
public String getDeviceId() {
    try {
        return getSubscriberInfo().getDeviceId();
    } catch (RemoteException ex) {
        return null;
    } catch (NullPointerException ex) {
        return null;
    }
}
```


那么这个getSubscriberInfo()方法里面又是什么呢：

```java
private IPhoneSubInfo getSubscriberInfo() {
    return IPhoneSubInfo.Stub.asInterface(ServiceManager.getService("iphonesubinfo"));
}

```

`IPhoneSubInfo`是一个AIDL接口，它定义的方法其中一个就是getDeviceId，而 `PhoneSubInfo` 则 继承了`IPhoneSubInfo.Stub` 并实现了getDeviceId接口。那就顺便看看`PhoneSubInfo`类吧，

```java
@Override
public String getDeviceId() {
    mContext.enforceCallingOrSelfPermission(READ_PHONE_STATE, "Requires READ_PHONE_STATE");
    return mPhone.getDeviceId();
}
```

可以看到这里又返回了PhoneSubInfo的成员变量mPhone.getDeviceId()。其实这里如果要使用反射把自己构造的Phone对象替换掉mPhone成员变量还是比较麻烦的，`CDMAPhone`和`GSMPhone`都是实现了Phone接口的类，而他们的构造函数还要引入其他的类，这样就为hook增加了难度。所以我们暂且没有必要看PhoneSubInfo类了。

### 二、我们可以向前看，来分析一下getSubscriberInfo方法。

1.首先是通过`ServiceManager.getService("iphonesubinfo")`得到一个`BinderProxy`对象。

看看ServiceManager的getService方法，是如何获取"iphonesubinfo"系统服务的，注意这里也是获取系统服务的通用方法：

```java
public static IBinder getService(String name) {
    try {
        IBinder service = sCache.get(name);
        if (service != null) {
            return service;
        } else {
            return getIServiceManager().getService(name);
        }
    } catch (RemoteException e) {
        Log.e(TAG, "error in getService", e);
    }
    return null;
}
```

在ServiceManager中，所有的系统服务对象都存放在 `sCache` 这个Map对象里。每次获取服务都是先从sCachae中去获取。

2.再来看看`IPhoneSubInfo.Stub`这个类是个啥东西

IPhoneSubInfo本身是个AIDL接口，这样系统编译时会自动为它生成一个IPhoneSubInfo.java文件，这个IPhoneSubInfo类实现了`android.os.IInterface`接口，并且它自己本身也是一个Interface。
在IPhoneSubInfo.java里面定义了一个`Stub`内部类，Stub类继承自`android.os.Binder`，并且实现了`IPhoneSubInfo`接口。

在使用系统服务的时候，如果客户端和服务端在同一个进程，调用方法时不会进行跨进程的`transact` 操作，直接调用接口方法实现。而当两者位于不同进程的时候，方法调用会走`transact`过程，这个逻辑又是由Stub的内部类Proxy来实现的。

Proxy这个类同样也实现了也实现了IPhoneSubInfo接口，而Proxy对象则是运行在客户端中的。

3.接着再把Binder对象传入`IPhoneSubInfo.Stub.asInterface`方法

客户端最终就通过asInterface得到一个`IPhoneSubInfo` 的代理对象`IPhoneSubInfo$Stub$Proxy`。以后就可以通过这个代理对象在客户端来执行远程方法了。

```java
public static com.android.internal.telephony.IPhoneSubInfo asInterface(android.os.IBinder obj) {
    if ((obj == null)) {
        return null;
    }
    android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR); // Hook点
    if (((iin != null) && (iin instanceof com.android.internal.telephony.IPhoneSubInfo))) {
        return ((com.android.internal.telephony.IPhoneSubInfo) iin);
    }
    return new com.android.internal.telephony.IPhoneSubInfo.Stub.Proxy(obj);
}

```

### 三、开始实现Hook

由上面可以知道，所有的系统服务对象都存在sCache中，因此我们可以将我们自己构造PhoneSubInfo代理服务对象再放到sCache中，这样ServiceManager每次调用getService("iphonesubinfo")时获取到的都是我们放进去的PhoneSubInfo对象。

1.由于asInterface中要传入一个IBinder对象，先要拿到一个原始的IBinder对象

直接根据ServiceManager.getService("iphonesubinfo")来通过反射得到原始的IBinder对象。

```java
final String TELEPHONE_SERVICE = "iphonesubinfo";
Class sm = Class.forName("android.os.ServiceManager");
Method getServiceMethod = sm.getDeclaredMethod("getService", String.class);
IBinder binder = (IBinder) getServiceMethod.invoke(null, TELEPHONE_SERVICE);
```

注意此时由于是在客户端，所以得到的IBinder对象是一个代理对象BinderProxy。

2.再根据上面拿到的BinderProxy对象手动实现一个代理对象

```java
IBinder hookedBinder = (IBinder) Proxy.newProxyInstance(sm.getClassLoader(), new Class<?>[]{IBinder.class}, new TelephoneProxyHookDiHandler(binder));
```

来看看TelephoneProxyHookDiHandler类：

```java
public class TelephoneProxyHookDiHandler implements InvocationHandler {
    IBinder base;
    Class<?> stubClass;
    Class<?> iinterface;
    public TelephoneProxyHookDiHandler(IBinder base) {
        this.base = base;
        try {
            this.iinterface = Class.forName("com.android.internal.telephony.IPhoneSubInfo");
            this.stubClass = Class.forName("com.android.internal.telephony.IPhoneSubInfo$Stub");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("queryLocalInterface".equals(method.getName())) {
            return Proxy.newProxyInstance(proxy.getClass().getClassLoader(),new Class[]{this.iinterface},new TelephoneHookDiHandler(base, stub));
        }
        return method.invoke(base, args);
    }
}
```

首先在构造函数中实例化一个IPhoneSubInfo对象和一个IPhoneSubInfo$Stub对象,然后在invoke方法中，让queryLocalInterface方法每次都返回我们将要构造的Proxy对象，

这里我们看到Proxy.newProxyInstance方法的第二个参数传递进去的是一个IPhoneSubInfo对象，表明我们获取的代理对象同意可以执行IPhoneSubInfo里面定义的所有方法。

3.看看TelephoneHookDiHandler类

在构造函数中将上面得到的IBinder代理对象BinderProxy以及Stub对象IPhoneSubInfo$Stub传入TelephoneHookDiHandler类。通过反射调用IPhoneSubInfo$Stub.asInterface得到一个IPhoneSubInfo$Stub$Proxy代理对象，然后在invoke方法中对getDeviceId做处理。

```java
public class TelephoneHookDiHandler implements InvocationHandler {
    Object base;
    public TelephoneHookDiHandler(IBinder base, Class<?> stubClass) {
        try {
            Method asInterfaceMethod = stubClass.getDeclaredMethod("asInterface", IBinder.class);
            this.base = asInterfaceMethod.invoke(null, base);
        } catch (Exception e) {
            throw new RuntimeException("hooked failed!");
        }
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("getDeviceId".equals(method.getName())) {
            return "1234567890";
        }
        return method.invoke(base, args);
    }
}
```

4.处理完上面的事情我们就可以把第二步得到的hookedBinder对象放入sCache中了：

```
Field field = sm.getDeclaredField("sCache");
field.setAccessible(true);
Map<String, IBinder> map = (Map<String, IBinder>) field.get(null);
map.put(TELEPHONE_SERVICE, hookedBinder);
```

大功告成，以后在程序里调用TelephoneManager.getDeviceId()得到的都是"1234567890"
- - -
THE END.
