---
date: 2015-03-29
layout: default
category: SE
tags: [DesignPattern,JAVA]
title: JAVA设计模式-策略模式
---

#### 参与者

1. 抽象策略角色
2. 具体策略角色
3. 策略使用场景
4. 客户端

#### 实现

1. 抽象策略角色通过接口或者抽象类来定义要实现的业务逻辑方法
2. 具体策略角色实现抽象策略角色所定义的方法，不同的具体策略角色，他们实现的内容并不一样
3. 在场景里面的构造方法中传入一个策略角色并作为成员变量。定义一个执行方法，在方法内部调用抽象策略角色的业务逻辑方法
4. 在客户端最终的目的是完成抽象策略所定义的业务逻辑，所以只要通过使用场景来完成业务逻辑即可

<!--more-->
#### 效果

对于不同的具体策略，客户端并不关心他们的内部实现，只要根据需求将不同的具体策略应用到场景中都可以实现业务逻辑。

- - -

#### 实例

现在客户端需要一个搜索算法。能够查找当前数组中的某个元素的位置。由于客户端想要根据不同的需求采用不同的搜索算法。于是我们定义以下几个参与者：

1. 抽象策略角色：FindElement接口，用来定义搜索方法find方法
2. 具体策略角色1：直接查找法，实现FindElement接口，并重写它的find方法 ； 具体策略角色2：二分查找法，同样也要实现FindElement接口，并重写它的find方法
3. 策略使用场景： 提供给客户端，并用场景来执行查找行为
4. 客户端

#### 一、首先定义FindElement接口

```java
public interface FindElement {
    //查找元素e在数组t中的位置
    public int find(int[] t,int e);
}
```

#### 二、定义具体策略角色1：直接查找法

```java
public class FindElementDirectly implements FindElement{

    @Override
    public int find(int[] array,int e) {
        int pos = -1;
        for (int i = 0; i<array.length;i++) {
            if (e == array[i]) {
              pos = i;
            }
        }
        return pos;
    }
}

```

#### 三、定义具体策略角色2：二分查找法

```java
public class FindElementBinary implements FindElement {

    @Override
    public  int find(int[] array,int e) {
        int pos = -1;
        int length = array.length;
        int low = 0;
        int high = length-1;
        while ( low<=high && low<=(length-1) && high<=(length-1)) {
          int mid = (low+high)/2;
          if ( e == array[mid]) {
            pos = mid;
            return pos;
          } else if ( e < array[mid]) {
            high = mid -1;
          } else {
            low = mid+1;
          }
        }
        return pos;

    }
}

```

#### 四、定义算法的使用场景

```java
public class FindContext {

    private FindElement findElement;
    public FindContext(FindElement findElement) {
        this.findElement = findElement;
    }

    public  int executeFind(int[] t,int e) {
        return this.findElement.find(t,e);
    }
}
```

可以看到FindContext有一个FindElement成员变量，它的执行搜索方法内部其实是调用了这个FindElement的find方法，
所以只要给FindContext的构造函数传入不同的FindElement对象，场景就可以执行不同的搜索算法，当然最终结果都是一样的。

#### 五、客户端使用场景来调用搜索算法

```java
public class Client {
    public static void main(String[] args) {
        FindContext contextDirectly = new FindContext(new FindElementDirectly());
        FindContext contextBinary = new FindContext(new FindElementBinary());
        int[] array = new int[]{1,2,3,4,5,6,7};
        int posDirectly = contextDirectly.executeFind(array,7);
        int posBinary = contextBinary.executeFind(array,7);

        System.out.println("Directly --> Pos :" + posDirectly);
        System.out.println("Binary --> Pos :" + posBinary);
    }
}
```

最后来测试一下

```bash
javac Client.java
java Client
```

看一下运行结果：

```
Directly --> Pos :6
Binary --> Pos :6
```

> 实例源码：[https://github.com/whisper92/wxpdesignpattern/tree/master/src/java/strategy/StrategyExample](https://github.com/whisper92/wxpdesignpattern/tree/master/src/java/strategy/StrategyExample)

- - -
THE END.
