---
date: 2015-05-17
layout: default
category: SE
tags: [DesignPattern,Java]
title: JAVA设计模式-模板方法模式
---

## 模板方法模式

#### 概述

定义一个操作中的算法的骨架，而将步骤延迟到子类中。模板方法使得子类可以不改变一个算法的结构即可重定义算法的某些特定步骤。

#### 参与者
1. 抽象模板
2. 具体模板

<!--more-->

#### 实现
1. 抽象模板定义一些了抽象模板方法，而这些方法交由子类去实现
2. 具体模板继承抽象模板，并重写抽象模板的模板方法

#### 效果
客户端继承抽象模板，实现一个它的一个子类具体模板，并根据具体需求重写抽象模板的模板方法

- - -

#### 实例

现在有一个算法框架，定义了排序和搜素元素两个抽象方法，而具体的实现则交由客户端实现继承它的子类再去具体实现，至于子类采用什么样的排序和搜索算法，会由客户端自己决定。

现有如下几个角色：

1. 抽象模板：AbstractAlgorithmTemplate，定义排序方法sortContent和搜索方法findElement，还有一个showResult方法，并且在这个方法内部规定了先排序，后搜索的操作。
2. 具体模板：ConcretAlgorithm,继承父类AbstractAlgorithmTemplate，并实现排序和搜索方法

下面来看一下具体的代码实现

#### 一、抽象模板：AbstractAlgorithmTemplate.java

```java
public abstract class AbstractAlgorithmTemplate {

    protected abstract void sortContent(int[] array);
    protected abstract int findElement(int[] array,int e);
    public void showResult(int[] array) {
        System.out.println("排序开始 ：");
        sortContent(array);
        int pos = findElement(array,2);
        System.out.println("搜索元素 ： 2"+" ； 位置为 ： "+pos);
    }

}

```

#### 二、具体模板：ConcretAlgorithm.java

```java

public class ConcretAlgorithm extends AbstractAlgorithmTemplate {

    @Override
    protected void sortContent(int[] a) {
        //这里采用选择排序
        int minIndex=0;
        int temp=0;
        if((a==null)||(a.length==0))
            return;
        for (int i=0;i<a.length-1;i++) {
            minIndex=i;//无序区的最小数据数组下标
            for (int j=i+1;j<a.length;j++) {
                //在无序区中找到最小数据并保存其数组下标
                if(a[j]<a[minIndex])
                {
                    minIndex=j;
                }
            }
            if(minIndex!=i) {
                //如果不是无序区的最小值位置不是默认的第一个数据，则交换之。
                temp=a[i];
                a[i]=a[minIndex];
                a[minIndex]=temp;
            }
            printArray(a);
            System.out.println("");
        }

    }

    protected int findElement(int[] array,int e) {
        //这里采用二分搜索
        int l = 0,h = array.length-1;
        int m = (l+h)/2;
        int pos = -1;
        while (l<h) {
            m = (l+h)/2;
            if(array[m]<e) {
                l = m+1;
            } else if (array[m]>e) {
                h = m-1;
            } else {
                return m;
            }
        }

        return pos;
    }

    private static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++){
            System.out.printf("%3s", array[i]);
        }
    }
}
```

#### 三、下面来测试一下

```java

/**
 * 测试：模版方法模式
 * @author wxp
 *
 */
public class TestTemplate
{
  public static void main(String[] args)
  {
    int[] array = new int[]{1,3,2,4,6,7,5};
    AbstractAlgorithmTemplate abstractQuadrant = new ConcretAlgorithm();
    abstractQuadrant.showResult(array);
  }
}
```

编译运行结果：

```
排序开始 ：
  1  3  2  4  6  7  5
  1  2  3  4  6  7  5
  1  2  3  4  6  7  5
  1  2  3  4  6  7  5
  1  2  3  4  5  7  6
  1  2  3  4  5  6  7
搜索元素 ： 2 ； 位置为 ： 1
```

> 实例源码：[https://github.com/whisper92/wxpdesignpattern/tree/master/src/java/template](https://github.com/whisper92/wxpdesignpattern/tree/master/src/java/template)

- - -
THE END.
