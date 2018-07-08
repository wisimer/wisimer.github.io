---
date: 2015-08-28
layout: default
category: SE
tags: [DesignPattern,JAVA]
title: JAVA设计模式-适配器模式
---
## 适配器模式

## 一、参与者

1. 被适配的对象，就是源对象
2. 适配后的对象，也就是目标对象
3. 适配器

<!--more-->

## 二、实现

1. 定义一个目标接口，包含源对象的行为方法，以及需要适配的新的方法。
2. 适配器类继承源对象，实现目标对象接口。
3. 重写目标接口中新的行为方法，在方法内部实现新的行为。

## 三、效果

客户端通过实例化一个适配器，便可获得一个目标对象，这个目标对象既可以实现源对象的方法，也可以实现适配后新的方法

- - -

## 四、实例

现在已经有了一个可以画方形的类，现在客户端要通过这个画方形的类实现画圆的行为。

#### 1.已有的画方形的类

```java
public class Square
{
    public void drawSquare(int width){
        System.out.println("drawSquare...width is : "+width);
    }
}
```

#### 2.目标接口，包含画圆的方法。注意要定义一个和源对象同样的接口方法。

```java
public interface DrawBitmap
{
    public void drawSquare(int width);
    public void drawCircle(int radius);
}
```

#### 3.基于方形画圆形的适配器

```java
/**
 * 继承Square类，实现DrawBitmap接口
 */
public class DrawCircleAdapter extends Square implements DrawBitmap
{

    /*重载DrawBitmap接口的drawCircle方法,此时画出来的是一个圆*/
    @Override
    public void drawCircle(int radius){
        System.out.println("drawCircle...radius is : "+radius);
    }
}
```

#### 4.最后来测试一下

```java
public class AdapterClient
{
    public static void main(String[] args)
    {
        DrawBitmap draw=new DrawCircleAdapter();
        draw.drawSquare(4);
        draw.drawCircle(2);
    }
}
```

在终端执行：

```
javac  AdapterClient.java
java AdapterClient
```

看一下运行结果：

```
drawSquare...width is : 4
drawCircle...radius is : 2
```

可以看到经过DrawCircleAdapter适配器转换后的DrawBitmap对象，可以画出圆形了。

## 五、应用场景

1. 业务的接口与工作的类不兼容，（比如：类中缺少实现接口的某些方法）但又需要两者一起工作
2. 在现有接口和类的基础上为新的业务需求提供接口

- - -

实例源码：[https://github.com/whisper92/wxpdesignpattern/tree/master/src/java/adapter/AdapterExample](https://github.com/whisper92/wxpdesignpattern/tree/master/src/java/adapter/AdapterExample)

- - -
THE END.
