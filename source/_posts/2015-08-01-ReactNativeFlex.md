---
date: 2015-08-01
layout: default
category: ANDROID
tags: ReactNative
title: ReactNative之Flexbox布局
---

[Flexbox](http://www.w3school.com.cn/cssref/index.asp#flexbox)是[CSS3](http://www.w3school.com.cn/css3/)中引入的布局模型－弹性盒子模型，旨在通过弹性的方式来对齐和分布容器中内容的空间，使其能够适应不同屏幕的宽度。

> Flexbox并不是作为一个单一的属性，而是作为一系列属性的集合。ReactNative中的Flexbox是这个规范的一个子集。

<!--more-->


### Flexbox属性

对于Flexbox，会有一个`Flex container`作为容器，里面包含了一些了Flex元素`Flex item`。
Flexbox中的有些属性是针对于`Flex container`，而另外有些属性是针对于`Flex item`的。

#### 一、针对`Flex container`的属性

1. flexDirection:有两个可选值，'column'（默认值）和'row'。 row 表示主轴的方向是水平的。可以看一下两种不同的排列方式：

    ![0815_flexDirection.png](/src/imgs/1508/0815_flexDirection.png)

2. flexWrap:有两个可选值，'nowrap'（默认值）和'wrap'。默认情况下，ReactNative中Flexbox会将所有元素排列在一行，
如果想要元素达到容器边缘之后换行则要设置为wrap。看一下两种不同的包裹方式：

    ![0815_flexWrap.png](/src/imgs/1508/0815_flexWrap.png)

    > 如果设置了 flexWrap 属性为'wrap'，要使其生效，需要设置容器的宽度为固定值

3. alignItems:有四个可选值，'flex-start','flex-end','center'和'stretch'。具体排列方式依次如图：

    ![0815_alignItems.png](/src/imgs/1508/0815_alignItems.png)

    > 如果设置了 alignItems 属性为'stretch'，要使其生效就不能设置item的高度为固定值

4. justifyContent:有五个可选值，'flex-start','flex-end','center','space-between'和'space-around'。具体排列方式如图：

    ![0815_justifyContent.png](/src/imgs/1508/0815_justifyContent.png)

    > 若要使的 justifyContent 属性生效，需要设置容器的宽度为固定值

#### 值得注意的是`alignItems`属性是针对于纵轴方向上的排布，而`justifyContent`则是针对于横轴方向上的排布，同时将两个属性设置为'center'即可实现内容的水平垂直居中显示

#### 二、针对`Flex item`的属性

1. flex:表示flex元素在布局中所占的比重。我们这里先将第一个容器内五个元素的flex属性值依次设置为1，2，3，2，1。
接着将第二个容器内的五个元素的flex属性值依次设置为2，2，2，2，2。来看一下最后的效果：

    ![0815_flex.png](/src/imgs/1508/0815_flex.png)

2. alignSelf:有五个可选值，'auto','flex-start','flex-end','center'和'stretch'。用来控制单个flex元素在Flex container中的位置。
我们来看一下他们各自的效果

    ![0815_alignSelf.png](/src/imgs/1508/0815_alignSelf.png)

#### 值得注意的是`justifyContent`属性控制的是整个容器中的元素相对于容器本身的位置，`alignSelf`属性控制的是单个元素本身相对于它的父容器的位置。

- - -

通过使用Flexbox可以很方便的解决如下几个问题：

1. 浮动布局
2. 不同宽度屏幕的适配
3. 宽度自动分配
4. 水平垂直居中

- - -
THE END.
