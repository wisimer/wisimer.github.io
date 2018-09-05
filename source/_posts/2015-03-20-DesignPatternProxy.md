---
date: 2015-03-20
layout: default
category: SE
tags: [DesignPattern,Java]
title: JAVA设计模式-代理模式
---

#### 参与者
1. 行为接口
2. 目标对象，也就是被代理的对象
3. 代理对象
4. 客户端

#### 实现
1. 代理对象和目标对象均实现一个相同的行为接口;
2. 在各自的类里面重写接口中的行为方法.
3. 在代理对象的无参构造函数中实例化一个被代理者对象;
4. 在代理者的行为方法中调用被代理者的行为方法.
5. 客户端想要调用目标对象的那个行为，只能通过调用代理对象的行为方法来实现。

<!--more-->

#### 效果
客户端需要调用的是目标对象(被代理者)的行为方法;
现在通过代理模式用代理对象代理了目标对象并调用他的行为方法,达到了同样的目的.
并且还可以在代理者中还可以定义其他的一些方法

- - -

#### 实例

举个例子，“有时候”不能访问谷歌，所以只能翻墙，Ubuntu环境下，使用GoAgent来实现翻墙还是比较方便的。

于是现在就可以通过代理模式来解决上面这中情形下产生的问题

假设现在有下面几个参与者：

1. 行为接口:Search.java
2. 客户端:Geek.java
3. 代理对象:GoAgent.java
4. 目标对象:GAE.java(Google App Engine)

#### 一、首先定义一个抽象的行为接口`googlesearch`

```java
public interface Search{
    public String googlesearch(String keyword);
}
```

#### 二、还有一个客户端,也就是它要使用代理对象

```java
public class Geek{
    public static void main(String[] args){
        Search staticsearch = new GoAgent();
        System.out.println("Result is :"+staticsearch.googlesearch("wxp"));
    }
}
```

由于用户（客户端）并不能直接访问google，也就是不能实例化一个GAE对象，所以只能通过实例化一个GoAgent代理对象来实现。
再想一下，实际访问google的是GAE,所以在GoAgent内部一定会实例化一个GAE对象来达到访问google的目的。

#### 三、让GAE实现Search这个行为接口

```java
public class GAE implements Search{
    public void googlesearch(){
        System.out.println("Search result from google...keyword is : "+kw);
        return "pcx : "+System.currentTimeMillis();
    }
}

```
#### 四、因为用户就是要通过代理对象来实现搜索，所以GoAgent也要实现Search接口

> 根据上面的分析，代理对象有一个目标对象作为它的成员变量，并且在代理对象的无参构造函数中实例化这个目标对象。接着在代理对象的行为方法中调用目标对象的行为方法

```java
public class GoAgent implements Search{
    Search mSearch;
    public GoAgent(){
        mSearch = new GAE();
    }

    public String googlesearch(String kw){
        System.out.println("Use GoAgent to access google search...");
        String res = mSearch.googlesearch(kw);
        System.out.println("Successfully access google");
        return res;
    }
}
```

最后来测试一下

```bash
javac Geek.java
java Geek
```

看一下运行结果：

```
Use GoAgent to access google search...
Search result from google...keyword is : wxp
Successfully access google
Result is :pcx : 1426746484614
```

#### 五、应用场景:一个客户端不能或者不想直接实例化一个对象。于是通过一个代理对象作为中介来实现与服务端的交流。


1. 授权机制：不同等级的用户对某一个对象有不同的访问权限，通过代理模式来进行访问权限的控制。
2. 客户端暂时无法获取服务器端比较大的对象，可以通过代理模式用一个代理对象暂时代替真正的对象。

- - -

## 动态代理

可以发现，上面这种代理方式，必须给每个目标对象类都实现一个代理对象类，如果客户端要使用目标对象，只要创建代理对象再调用代理类的方法即可。

但是，如果现在有许多目标对象类，那就必须要有许多代理类才可以，这样显然不合理。所以我们可以采用动态代理，只用一个动态代理类就可以适用所有的目标对象。

要实现动态代理，一般涉及到一下两个类：

#### 一、java.lang.reflect.InvocationHandler

InvocationHandler（调用处理程序）是一个接口，我们必须新建一个自己的子类DynamicProxyHandler并实现这个接口，
然后重写它的invoke方法:

```java
public class DynamicProxyHandler implements InvocationHandler {
private Object mProxied;
    public DynamicProxyHandler(Object obj){
      this.mProxied = obj;
    }

    @Override
    public Object invoke(Object proxy,Method method,Object[] args) throws Throwable{
      System.out.println("invoke : Object.getClass() --> "+proxy.getClass());
      System.out.println("invoke : Method --> "+method);
      System.out.println("invoke : Object[] --> "+args);

      if (args != null){
        for (Object arg : args) {
          System.out.println("args : "+args);
        }
      }

      return method.invoke(mProxied,args);
    }
}
```

- invoke的第一个参数proxy就是下面会提到的Java内建的代理类Proxy类的一个实例
- 第二个参数method是被代理的方法，也就是上面的googlesearch方法
- 第三个参数是googlesearch方法的参数组，如果method方法没有参数，那第三个参数就为null

#### 二、java.lang.reflect.Proxy

Proxy即为代理类，作用类似于上面的GoAgent类，它有一个 `Static Object newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler h)` 方法返回代理类的一个实例，返回后的代理类，可以当作被代理类使用，也就是可以使用googlesearch方法。

- 第一个参数是代理类的类加载器
- 第二个参数是抽象行为方法数组,可以直接通过search.getClass().getInterfaces()来获取
- 第三个参数是上面提到的调用处理程序DynamicProxyHandler的一个实例

#### 三、我们在客户端如何使用动态代理对象呢

```java
Search gae = new GAE();
DynamicProxyHandler dynamicproxyhandler = new DynamicProxyHandler(gae);
Search dynamicproxysearch = (Search)Proxy.newProxyInstance(
    Search.class.getClassLoader(),
    gae.getClass().getInterfaces(),
    dynamicproxyhandler);
System.out.println("Result is :"+dynamicproxysearch.googlesearch("wxp"));
```

当我们调用dynamicproxysearch的googlesearch方法时，jvm会将该方法的调用指派给它的调用处理程序 dynamicproxyhandler
最终调用的其实是metho.invoke()方法。
来看一下运行结果：

```
invoke : Object.getClass() --> class com.sun.proxy.$Proxy0
invoke : Method --> public abstract java.lang.String Search.googlesearch(java.lang.String)
invoke : Object[] --> [Ljava.lang.Object;@3852fdeb
args : [Ljava.lang.Object;@3852fdeb
Search result from google...keyword is : wxp
Result is :pcx : 1426746484620
```

> Proxy上的任何方法调用都会被传入InvocationHandler类,InvocationHandler控制对目标对象的访问。

可以看到在实例化DynamicProxyHandler对象时，只要传入一个目标对象的实例即可。
这样我们就不必为每个目标对象都实现一个代理类了，只要通过动态代理来生成一个代理对象，然后调用它的代理方法即可。

- - -

实例源码：[https://github.com/whisper92/wxpdesignpattern/tree/master/src/java/proxy/ProxyExample](https://github.com/whisper92/wxpdesignpattern/tree/master/src/java/proxy/ProxyExample)

另外附送通过动态代理实现不同访问权限的用户的例子：

[https://github.com/whisper92/wxpdesignpattern/tree/master/src/java/proxy/ControlAccess](https://github.com/whisper92/wxpdesignpattern/tree/master/src/java/proxy/ControlAccess)

- - -
THE END.
