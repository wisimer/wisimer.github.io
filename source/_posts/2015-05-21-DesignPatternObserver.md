---
date: 2015-05-21
layout: default
category: SE
tags: [DesignPattern,Java]
title: JAVA设计模式-观察者模式
---

## 观察者模式

#### 参与者
1. 抽象观察者
2. 抽象被观察者
3. 具体观察者
4. 具体被观察者

<!--more-->

#### 实现
1. 抽象观察者中定义一个抽象行为方法,观察到变化以后执行这个行为
2. 抽象被观察者要实现一个以观察者为参数的构造方法,并且拥有注册和解除观察者的方法
3. 具体被观察者重写抽象观察者的行为方法,并在这个方法中调用观察者的行为方法.
4. 具体观察者重写抽象观察者的行为方法,并且在这个方法中实现观察到状态变化之后的一些操作.

#### 效果
最后只要在客户端调用被观察者的行为方法,观察者也会产生相对应的行为.

- - -

#### 实例

客户端可以向服务器订阅RSS消息，一旦服务器更新了内容，客户端就可以接受更新的内容了。

假设现在有如下参与者：

1. 抽象被观察者：IRSSServerWatched 接口，定义了添加观察者以及移除观察者的方法还有服务器发布新内容的行为方法。
2. 抽象观察者：IClientWatcher 接口，定义了客户端更新内容的方法。
3. 具体被观察者： ConcretRSSServerWatched.java，这个类实现了IRSSServerWatched接口
4. 具体观察者：ConcretClientWatcher.java，这个类实现了IClientWatcher接口

下面来看一下具体的代码实现

#### 一、抽象被观察者：IRSSServerWatched 接口

```java
//抽象被观察者：RSS订阅服务器接口
public interface IRSSServerWatched {
    public void addWatcher(IClientWatcher watcher);
    public void removeWatcher(IClientWatcher watcher);
    public void publishContent(String content);
}
```

#### 二、抽象观察者：IClientWatcher 接口

```java
//抽象观察者：客户端订阅程序接口
public interface IClientWatcher {
    public void update(String content);
}
```

#### 三、具体被观察者： ConcretRSSServerWatched

```java
//具体被观察者角色,也就是RSS服务器
import java.util.List;
import java.util.ArrayList;
public class ConcretRSSServerWatched implements IRSSServerWatched {

    private List<IClientWatcher> mWatchersList = new ArrayList<IClientWatcher>();
    public void addWatcher(IClientWatcher watcher) {
        //这个方法向服务器添加注册的客户端
        mWatchersList.add(watcher);
    }

    public void removeWatcher(IClientWatcher watcher) {
        //这个方法将客户端从服务器移除
        mWatchersList.remove(watcher);
    }

    public void publishContent(String content) {
       //这个方法是服务器发布新内容，并通知客户端更新内容
       System.out.println("服务器更新内容啦～");
       for(IClientWatcher watcher : mWatchersList) {
            watcher.update(content);
        }
    }

}

```

#### 四、具体观察者：ConcretClientWatcher

```java
//具体观察者角色：RSS订阅客户端
public class ConcretClientWatcher implements IClientWatcher {
    //一旦服务器端有新内容更新，客户端这边即可作出反应
    public void update(String content) {
        System.out.println("客户端更新内容 : "+content);
    }
}
```

#### 五、下面来测试一下

```java

public class RSSTest {
    public static void main(String[] args) {
        IRSSServerWatched server = new ConcretRSSServerWatched();

        IClientWatcher watcherOne = new ConcretClientWatcher();
        IClientWatcher watcherTwo = new ConcretClientWatcher();
        IClientWatcher watcherThree = new ConcretClientWatcher();

        server.addWatcher(watcherOne);
        server.addWatcher(watcherTwo);
        server.addWatcher(watcherThree);
        server.publishContent("更新的是天气预报。。。");//这行代码执行之后，上面这些ConcretClientWatcher的update()也会执行，所以就产生了监听的效果
    }
}
```

编译运行之后：

```
服务器更新内容啦～
客户端更新内容 : 更新的是天气预报。。。
客户端更新内容 : 更新的是天气预报。。。
客户端更新内容 : 更新的是天气预报。。。
```

> 实例源码：[https://github.com/whisper92/wxpdesignpattern/tree/master/src/java/observer/rss](https://github.com/whisper92/wxpdesignpattern/tree/master/src/java/observer/rss)

- - -
THE END.
